package cn.csg.ucs.bi.business.controller.require;

import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.service.require.CProjectMgtService;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.formula.functions.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@CrossOrigin
@Transactional
@RestController
@RequestMapping("/cProjectMgtController")
public class CProjectMgtController {

    private Logger logger = LoggerFactory.getLogger(CProjectMgtController.class);

    @Autowired
    private CProjectMgtService cProjectMgtService;

    @Autowired
    private LogService logService;

    /**
     * @return cn.csg.ucs.bi.base.structure.JSONResponseBody
     * @Author chengzhifeng
     * @Description 获取节能服务
     * @Date 10:25 2019/11/27
     * @Param [params]
     **/
    @ResponseBody
    @GetMapping("/getCProjectMgtInfo")
    public JSONResponseBody getEnergyConservationInfo(String params) {
        logger.info("进入获取项目信息接口,参数：{}", params);
        JSONObject json = JSONObject.parseObject(params);
        Integer page = json.getInteger("page");
        Integer limit = json.getInteger("limit");
        String company = json.getString("company");
        String theMonth = json.getString("theMonth");
        String projectCategory = json.getString("projectCategory");
        if (page != null && limit != null && !"1".equals(projectCategory)) {
            json.remove("page");
            json.remove("limit");
            PageHelper.startPage(page, limit);
        }

        if (StringUtil.isEmpty(company) || StringUtil.isEmpty(theMonth) || StringUtil.isEmpty(projectCategory)) {
            return JSONResponseBody.createFailResponseBody(BaseCode.PARAM_ERROE, "参数错误");
        }

        JSONObject resultData = new JSONObject();
        switch (projectCategory) {
            case "1":
                resultData = cProjectMgtService.getSaveServiceInfo(json);
                break;
            case "2":
                resultData = cProjectMgtService.getDiagnosisInfo(json);
                break;
            case "3":
                resultData = cProjectMgtService.getHLTransformer(json);
                break;
            case "4":
                resultData = cProjectMgtService.getPromoteLEDInfo(json);
                break;
            case "5":
                resultData = cProjectMgtService.getIncentiveInfo(json);
                break;
            case "6":
                resultData = cProjectMgtService.getClientSideInfo(json);
                break;
            case "7":
                resultData = cProjectMgtService.getContractInfo(json);
                break;
            case "8":
                resultData = cProjectMgtService.getUnContractInfo(json);
                break;
            default:
                return JSONResponseBody.createFailResponseBody(BaseCode.PARAM_ERROE, "参数错误，项目类型错误");
        }

        JSONResponseBody result = JSONResponseBody.createSuccessResponseBody("查询成功", resultData);
        return result;
    }

    /**
     * @return cn.csg.ucs.bi.base.structure.JSONResponseBody
     * @Author chengzhifeng
     * @Description 新增统一接口
     * @Date 9:59 2019/11/29
     * @Param [params]
     **/
    @ResponseBody
    @PostMapping("/add")
    public JSONResponseBody add(String params, HttpServletRequest req) {

        logger.info("进入新增统一接口,参数：{}", params);
        JSONResponseBody result;
        JSONObject json = JSONObject.parseObject(params);
        //获取用户信息
        S_USER_INFO userInfo = (S_USER_INFO) req.getAttribute("LOGIN_USER");
        int add = cProjectMgtService.addObject(json, userInfo);
        if (add > 0) {

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"新增记录成功",getProjectCategoryName(json.getString("projectCategory")),userInfo, CommonUtil.getLoginIp(req));

            result = JSONResponseBody.createSuccessResponseBody("新增成功", null);
        } else {
            result = JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "数据库新增记录失败");
        }
        return result;

    }

    @ResponseBody
    @PostMapping("/validateExist")
    public JSONResponseBody validateExist(String params) {
        Boolean result = false;
        JSONObject jo = JSONObject.parseObject(params);
        List<Map<String, String>> list = cProjectMgtService.queryExistsForValidate(jo);
        String actionType = jo.getString("actionType");
        String projectCategory = jo.getString("projectCategory");
        if (actionType.equals("update") && !CollectionUtils.isEmpty(list)) {
            if (projectCategory.equals("7") || projectCategory.equals("8")) {
                List<String> listKeys = list.stream().map(p -> p.get("GUID")).collect(Collectors.toList());
                JSONObject json = jo.getJSONArray("objArr").getJSONObject(0);
                List<String> guids = new ArrayList<>();
                guids.add(json.getString("guid"));
                guids.add(json.getString("guid1"));
                listKeys.addAll(guids);
                result = list.size() <= 2 && listKeys.stream().distinct().collect(Collectors.toList()).size() == guids.size() ? true : false;
            } else {
                result = list.size() == 1 && list.get(0).get("GUID").equals(jo.getString("guid")) ? true : false;
            }
        } else {
            result = CollectionUtils.isEmpty(list) ? true : false;
        }
        return JSONResponseBody.createSuccessResponseBody("", result);
    }

    @ResponseBody
    @PostMapping("/del")
    public JSONResponseBody del(String params,HttpServletRequest req) {
        JSONObject json = JSONObject.parseObject(params);
        JSONResponseBody result = new JSONResponseBody();
        int del = cProjectMgtService.delByPrimaryKeys(json);
        if (del > 0) {
            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"删除记录成功",getProjectCategoryName(json.getString("projectCategory")),(S_USER_INFO) req.getAttribute("LOGIN_USER"), CommonUtil.getLoginIp(req));

            result = JSONResponseBody.createSuccessResponseBody("删除成功", null);
        } else {
            result = JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "数据库删除记录失败");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/update")
    public JSONResponseBody update(String params, HttpServletRequest req) {
        JSONObject json = JSONObject.parseObject(params);
        JSONResponseBody result = new JSONResponseBody();
        S_USER_INFO userInfo = (S_USER_INFO) req.getAttribute("LOGIN_USER");
        int update = cProjectMgtService.updateObject(json, userInfo);
        if (update > 0) {

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"更新记录成功",getProjectCategoryName(json.getString("projectCategory")),userInfo, CommonUtil.getLoginIp(req));

            result = JSONResponseBody.createSuccessResponseBody("更新成功", null);
        } else {
            result = JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "数据库更新记录失败");
        }
        return result;
    }

    @PostMapping("/getExcelTemplate")
    public void getExcelTemplate(String params, HttpServletRequest request, HttpServletResponse response) {
        File file = null;
        FileInputStream in = null;
        OutputStream out = null;
        try {
            JSONObject json = JSONObject.parseObject(params);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            String fileName = "模板.xls";
            S_USER_INFO userInfo = (S_USER_INFO) request.getAttribute("LOGIN_USER");
            //设置参数 使前端可以获取返回头参数
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            file = cProjectMgtService.generateTemplate(fileName, userInfo, json.getString("projectCategory"));
            ;
            //2.设置文件头：最后一个参数是设置下载文件名
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1")); //中文文件名
            //获得File对象
            in = new FileInputStream(file);
            //3.通过response获取OutputStream对象(out)
            out = new BufferedOutputStream(response.getOutputStream());

            int b = 0;
            byte[] buffer = new byte[2048];
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b); //4.写到输出流(out)中
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (null != in)
                    in.close();
                if (null != out)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete();//删除文件
        }
    }

    @ResponseBody
    @PostMapping("/batchAdd")
    public JSONResponseBody batchAdd(String params, HttpServletRequest req) throws ClassNotFoundException {
        JSONObject json = JSONObject.parseObject(params);
        JSONResponseBody result = new JSONResponseBody();
        S_USER_INFO userInfo = (S_USER_INFO) req.getAttribute("LOGIN_USER");
        result = cProjectMgtService.batchAdd(json, userInfo);

        if(BaseCode.BUSINESS_SUCCESS.equals(result.getBusinessCode())){
            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"导入成功",getProjectCategoryName(json.getString("projectCategory")),userInfo,CommonUtil.getLoginIp(req));
        }

        return result;
    }

    private String getProjectCategoryName(String projectCategory){

        String projectCategoryName = "";

        switch (projectCategory){
            case "1":
                projectCategoryName = "节能服务项目";
                break;
            case "2":
                projectCategoryName = "节能诊断明细项目";
                break;
            case "3":
                projectCategoryName = "高损变压器项目";
                break;
            case "4":
                projectCategoryName = "推广LED项目";
                break;
            case "5":
                projectCategoryName = "激励措施或新设备新技术降低负荷项目";
                break;
            case "6":
                projectCategoryName = "客户侧节能改造发电项目";
                break;
            case "7":
                projectCategoryName = "合同能源管理项目";
                break;
            case "8":
                projectCategoryName = "非合同能源管理项目";
                break;

        }

        return projectCategoryName;
    }

}