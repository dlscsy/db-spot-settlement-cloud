package cn.csg.ucs.bi.core.config.filter;

import cn.csg.core.common.utils.JWTUtils;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Configuration
@WebFilter(filterName = "authFilter")
public class AuthFilter implements Filter {

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService userMgtService;

    // 请求URL白名单
    private static ArrayList<String> REQUEST_WHITE_URL_LIST = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            add(".ico");
            add(".jpg");
            add(".png");
            add(".pdf");
            add(".css");
            add(".js");
            add("loginController/");
            add("authRedirectController/");
        }
    };


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String requestURI = req.getRequestURI();
        String token = req.getHeader("Auth-Token");
        if (requestURI.indexOf("download") > 0) {
            token = JSONObject.parseObject(req.getParameter("params")).getString("Auth-Token");
        } else {
            token = req.getHeader("Auth-Token");
        }
//        System.out.println("===================================================================================");
//        System.out.println(requestURI + ":" + token);
        if (!ifWhiteRequest(requestURI) && !"OPTIONS".equals(req.getMethod())) {
            if (StringUtils.isEmpty(token)) {
                req.getRequestDispatcher("/authRedirectController/tokenInvalid").forward(req, res);
            } else {
                Calendar nowTime = Calendar.getInstance();
                SimpleDateFormat sdf = null;
                Claims claims = null;
                String tokenId = null;
                S_USER_INFO userInfo = null;
                String accountTokenInDb = null;
                Date expDate = null;
                Date now = nowTime.getTime();
                try {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    claims = JWTUtils.parseJWT(token);
                    tokenId = claims.getId();// 此tokenId即为登录账号
                    userInfo = userMgtService.getUserByAccount(tokenId);
                    accountTokenInDb = userInfo.getToken();
                    expDate = sdf.parse(userInfo.getTokenExpDate());
//                    System.out.println("数据库的超时时间：" + sdf.format(expDate));
//                    now = sdf.parse(sdf.format(nowTime.getTime()));
                } catch (Exception e) {
                    req.getRequestDispatcher("/authRedirectController/tokenParseException").forward(req, res);
                }

                if (!token.equals(accountTokenInDb)) {// 同一账户只能同时在线一个判断
//                    System.out.println("数据库的：" + accountTokenInDb + "请求的：" + token);
                    req.getRequestDispatcher("/authRedirectController/tokenOnLineInvalid").forward(req, res);
                } else if (now.compareTo(expDate) >= 1) {// 超时判断
//                    System.out.println("当前时间：" + sdf.format(nowTime.getTime()));
                    req.getRequestDispatcher("/authRedirectController/tokenExpire").forward(req, res);
                } else {
                    req.setAttribute("LOGIN_USER", userInfo);
                    nowTime.add(Calendar.MINUTE, 30);
//                    System.out.println("当前时间：" + sdf.format(now));
//                    System.out.println("新的超时时间：" + sdf.format(nowTime.getTime()));
                    userMgtService.updateTokenExpDate(sdf.format(nowTime.getTime()), tokenId, userInfo.getUserName(), sdf.format(now), userInfo.getOrgCode());
                    filterChain.doFilter(req, res);
                }
            }
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 判断是否在请求URL白名单中
     *
     * @param requestUrl
     * @return 是否在白名单中
     */
    private boolean ifWhiteRequest(String requestUrl) {
        for (String whiteUrl : REQUEST_WHITE_URL_LIST) {
            if (requestUrl.contains(whiteUrl)) {
                return true;
            }
        }
        return false;
    }

}
