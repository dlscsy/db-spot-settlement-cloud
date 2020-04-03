package cn.csg.ucs.bi.core.login.controller;

import cn.csg.core.common.utils.CommonUtils;
import cn.csg.core.common.utils.JWTUtils;
import cn.csg.core.common.utils.MD5PasswordEncoder;
import cn.csg.core.common.utils.RSAUtils;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@EnableScheduling
@RequestMapping("/loginController")
public class LoginController {

    // 登录账户与token信息的映射关系
    public static Map<String, Map<String, Object>> ACCOUNT_TOKEN_MAP = new HashMap<>();
    public static Map<String, String> RSA_KEY_CACHE = new HashMap<>();

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService userMgtService;

    @Autowired
    private LogService logService;

    @PostMapping("/login")
    public @ResponseBody
    Object login(String params, HttpServletRequest req)
            throws Exception {
        JSONObject jsonData = JSONObject.parseObject(params);
        String account = RSAUtils.decode(jsonData.getString("account"), RSA_KEY_CACHE.get(RSAUtils.PRIVATE_KEY_NAME));
        String password = RSAUtils.decode(jsonData.getString("password"), RSA_KEY_CACHE.get(RSAUtils.PRIVATE_KEY_NAME));

//        if (ACCOUNT_TOKEN_MAP.containsKey(account)) {
//            return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INVALID_TOKEN, "该账号在别处登录！");
//        }
        S_USER_INFO user = null;
        try {
            user = userMgtService.getUserByAccount(account);
        }catch (IndexOutOfBoundsException e){
            return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INFO_WRONG, "账号不存在！");
        }

//        String tokenExpDateStr = user.getTokenExpDate();
        String passwordInDb = (user != null) ? user.getPwd() : "";
        MD5PasswordEncoder passwordEncoder = new MD5PasswordEncoder();
        JSONObject jo = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar nowTime = Calendar.getInstance();
        Date now = sdf.parse(sdf.format(nowTime.getTime()));
        //判断账号密码是否正确、账号是否锁定
        if (!"".equals(passwordInDb) && passwordEncoder.matches(password, passwordInDb) && "0".equals(user.getLockState())) {
            String token = JWTUtils.createJWT(account, null, null);
            nowTime.add(Calendar.MINUTE, 30);

            // 登录成功后更新用户账户信息
            userMgtService.updateUserInfoAfterLoginSuccess(account, token, sdf.format(nowTime.getTime()),
                    user.getUserName(), sdf.format(now), user.getOrgCode());
            req.setAttribute("LOGIN_USER", user);
            // 登录成功，在缓存中设置该账户对应的token信息
            jo.put("TOKEN", token);
            jo.put("LOGIN_USER", user);
            jo.put("EXP_DATE", sdf.format(nowTime.getTime()));
            ACCOUNT_TOKEN_MAP.put(account, jo);

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_LOGIN,"登录成功","",user, CommonUtil.getLoginIp(req));

            return JSONResponseBody.createSuccessResponseBody("", jo);
        } else {
            //判断账号是否已锁定
            if ("1".equals(user.getLockState())) {
                //获取最后操作时间
                String lastOperationTimeStr = user.getOperateDate();
                Date lastOperationTime = CommonUtils.convertStringToDate("yyyy-MM-dd HH:mm:ss",lastOperationTimeStr);
                //获取当前时间与最后操作时间差值
                Long timeSlot = new Date().getTime() - lastOperationTime.getTime();
                //判断差值是否大于30分钟
                if(timeSlot > 1800000){
                    if(!"".equals(passwordInDb) && passwordEncoder.matches(password, passwordInDb)){
                        String token = JWTUtils.createJWT(account, null, null);
                        nowTime.add(Calendar.MINUTE, 30);
                        userMgtService.updateUserInfoAfterLoginSuccess(account, token, sdf.format(nowTime.getTime()),
                                user.getUserName(), sdf.format(now), user.getOrgCode());
                        req.setAttribute("LOGIN_USER", user);
                        jo.put("TOKEN", token);
                        jo.put("LOGIN_USER", user);
                        return JSONResponseBody.createSuccessResponseBody("", jo);
                    }else{
                        userMgtService.unlockedUser(account, user.getUserName(), sdf.format(nowTime.getTime()));
                        String wrongNum = userMgtService.getWrongNumByAccount(account);
                        wrongNum = String.valueOf(Integer.valueOf(wrongNum) + 1);
                        userMgtService.updateWrongNum(account, wrongNum, user.getUserName(), sdf.format(nowTime.getTime()));
                        if ("5".equals(wrongNum)) {
                            userMgtService.lockedUser(account, user.getUserName(), sdf.format(nowTime.getTime()));
                        }
                        return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INFO_WRONG, "密码输入错误！");
                    }

                }else{

                    // 异步插入日志数据库
                    logService.addLog(S_LOGIN_LOG.LOGTYPE_LOGIN,"账号已锁定，锁定时间30分钟","",user, CommonUtil.getLoginIp(req));
                    return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INFO_WRONG, "密码错误输入已达5次，该登录账户已被锁定，"+ (30-Math.ceil((double)timeSlot/60000)) + "分钟后再尝试！");
                }
            } else {
                //获取账号输错次数
                String wrongNum = userMgtService.getWrongNumByAccount(account);
                //输错次数+1
                wrongNum = String.valueOf(Integer.valueOf(wrongNum) + 1);
                //更新输错次数
                userMgtService.updateWrongNum(account, wrongNum, user.getUserName(), sdf.format(nowTime.getTime()));
                //判断账号输错次数是否达到5次
                if ("5".equals(wrongNum)) {
                    //锁定账号
                    userMgtService.lockedUser(account, user.getUserName(), sdf.format(nowTime.getTime()));
                }
                return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INFO_WRONG, "密码输入错误！");
            }
        }
    }

    @PostMapping("/logout")
    public @ResponseBody
    Object logout(String params,HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject jsonData = JSONObject.parseObject(params);
        S_USER_INFO userInfo = userMgtService.getUserByAccount(jsonData.getString("account"));
        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_LOGIN,"登出成功","",userInfo, CommonUtil.getLoginIp(req));
        userMgtService.updateUserInfoAfterLogOutSuccess(jsonData.getString("account"),
                jsonData.getString("userName"), sdf.format(new Date()), jsonData.getString("orgCode"));

        return JSONResponseBody.createSuccessResponseBody("", null);
    }

    @PostConstruct
    public void initKeys() {
        Map<String, String> map = RSAUtils.createRSAKeys();
        RSA_KEY_CACHE.putAll(map);
    }

    @GetMapping("/getPublicKey")
    public @ResponseBody
    Object getPublicKey() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("publicKey", RSA_KEY_CACHE.get(RSAUtils.PUBLIC_KEY_NAME));
        return JSONResponseBody.createSuccessResponseBody("", jo);
    }

    @PostMapping("/changePassword")
    public @ResponseBody
    Object changePassword(String params, HttpServletRequest req)
            throws Exception {
        JSONObject jsonData = JSONObject.parseObject(params);
        String account = RSAUtils.decode(jsonData.getString("account"), RSA_KEY_CACHE.get(RSAUtils.PRIVATE_KEY_NAME));
        String oldPsd = RSAUtils.decode(jsonData.getString("oldPsd"), RSA_KEY_CACHE.get(RSAUtils.PRIVATE_KEY_NAME));
        String newPsd = RSAUtils.decode(jsonData.getString("newPsd"), RSA_KEY_CACHE.get(RSAUtils.PRIVATE_KEY_NAME));
        S_USER_INFO user = userMgtService.getUserByAccount(account);
        String passwordInDb = (user != null) ? user.getPwd() : "";
        MD5PasswordEncoder passwordEncoder = new MD5PasswordEncoder();
        if (!"".equals(oldPsd) && passwordEncoder.matches(oldPsd, passwordInDb)) {

            String newPassword = passwordEncoder.encode(newPsd);
            userMgtService.changePassword(account, newPassword);

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_LOGIN,"修改密码成功","",user, CommonUtil.getLoginIp(req));
            return JSONResponseBody.createSuccessResponseBody("修改成功", null);
        } else {
            return JSONResponseBody.createFailResponseBody(BaseCode.LOGIN_INFO_WRONG, "原密码错误！");
        }
    }

}