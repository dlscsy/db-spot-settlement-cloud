package cn.csg.ucs.bi.business.controller.require;

import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.service.require.impl.ClientSideServiceImpl;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.service.LogService;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import cn.csg.ucs.bi.utils.CommonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自身/客户侧报表汇总Controller
 * @author G.A.N
 * @Date 2019-11-12
 */
@CrossOrigin
@RestController
@RequestMapping("/clientSideSummary")
public class ClientSideSummaryController {

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService userMgtService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ClientSideServiceImpl clientSideService;

    @Autowired
    private LogService logService;

    //查询汇总情况
    @GetMapping("/search")
    public @ResponseBody
    Object getSummary(String params) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //获取编码类型
        String codeType = "0".equals(projectSide) ? "7" : "8";
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询失败！");
        }
        //报表汇总查询
        List<ReportSummaryDTO> resultList = clientSideService.getReportSummary(codeType,projectSide,company,theMonth);
//        //自身侧报表去掉橙色部分
//        List<ReportSummaryDTO> tempList = new ArrayList<ReportSummaryDTO>();
//        if("0".equals(projectSide)){
//            for(ReportSummaryDTO item : resultList){
//                if(Integer.parseInt(item.getCodeValue()) < 6 || Integer.parseInt(item.getCodeValue()) > 12){
//                    tempList.add(item);
//                }
//            }
//        }else{
//            tempList = resultList;
//        }
        JSONObject result = new JSONObject();
        result.put("rows", resultList);
        if(resultList != null && resultList.size() > 0){
            return JSONResponseBody.createSuccessResponseBody("查询成功！", result);
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询失败！");
    }

    //报表统计
    @PostMapping("/count")
    public @ResponseBody
    Object countReport(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        JSONArray jsonArray = jsonData.getJSONArray("codeValues");
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计的报表编码集合
        List<String> codeValues = JSONObject.parseArray(jsonArray.toJSONString(), String.class);
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //获取用户信息
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)
                || codeValues.size() == 0  || userInfo == null){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "统计失败，参数缺失！");
        }
        int result = (int)clientSideService.statisticsInfo(projectSide, company, theMonth, codeValues, userInfo);
        if(result == -1){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "统计失败，数据库无配置此类报表！");
        }

        // 异步插入日志数据库
        logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"统计成功","",userInfo, CommonUtil.getLoginIp(req));

        return JSONResponseBody.createSuccessResponseBody("统计成功！", null);
    }

    //报表上报
    @PostMapping("/submit")
    public @ResponseBody
    Object submitReport(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        JSONArray jsonArray = jsonData.getJSONArray("codeValues");
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计的报表编码集合
        List<String> codeValues = JSONObject.parseArray(jsonArray.toJSONString(), String.class);
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //获取用户信息
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)
                || codeValues.size() == 0  || userInfo == null){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "上报失败！");
        }
        int result = clientSideService.submitReport(theMonth,company,codeValues,projectSide,userInfo);
        if(result == 1){

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"上报成功","", userInfo, CommonUtil.getLoginIp(req));

            return JSONResponseBody.createSuccessResponseBody("上报成功！", null);
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "上报失败！");
    }

    //获取未上报的的下级所属单位（批量上报功能查询）
    @PostMapping("/searchNoSubmit")
    public @ResponseBody
    Object searchNoSubmit(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取报表编码
        String codeValue = jsonData.getString("codeValue");
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询下级所属单位失败！");
        }
        Integer page = jsonData.getInteger("page");
        Integer limit = jsonData.getInteger("limit");
        PageHelper.startPage(page, limit);
        List<BatchSubmitDTO> resultList = clientSideService.getNoSubmitNextCompany(theMonth,codeValue,projectSide,company);
        JSONObject result = new JSONObject();
        result.put("rows", resultList);
        result.put("total", new PageInfo<BatchSubmitDTO>(resultList).getTotal());
        if(resultList != null && resultList.size() > 0){
            return JSONResponseBody.createSuccessResponseBody("查询成功！", result);
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询下级所属单位失败！");
    }

    //判断是否有下级所属单位（批量上报功能查询）
    @GetMapping("/isNextCompany")
    public @ResponseBody
    Object isNextCompany(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取所属单位
        String company = jsonData.getString("company");
        //判断参数是否为空
        if(StringUtils.isBlank(company)){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询下级所属单位失败！");
        }
        //判断是否是最后一级所属单位
        List<String> nextCompanyList = commonService.getAllSubOrgCodes(company);
        if(nextCompanyList == null || nextCompanyList.size() == 0) {
            return JSONResponseBody.createSuccessResponseBody("查询失败！", false);
        }
        return JSONResponseBody.createSuccessResponseBody("查询成功！", true);
    }

    //批量上报
    @PostMapping("/batchSubmit")
    public @ResponseBody
    Object batchSubmitReport(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //获取用户信息
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        JSONArray jsonArray = jsonData.getJSONArray("codeValues");
        List<String> codeValues = JSONObject.parseArray(jsonArray.toJSONString(), String.class);
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)
                || userInfo == null){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "上报失败！");
        }
        int result = clientSideService.batchSubmitReport(theMonth,company,codeValues,projectSide,userInfo);
        if(result == 1){

            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"批量上报成功","", userInfo, CommonUtil.getLoginIp(req));

            return JSONResponseBody.createSuccessResponseBody("批量上报成功！", null);
        }else if(result == 2){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "批量上报失败，下级单位已全部上报！");
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "批量上报失败！");
    }

    //报表回退
    @PostMapping("/reportReturn")
    public @ResponseBody
    Object reportReturn(String params, HttpServletRequest req) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取项目所属侧
        String projectSide = jsonData.getString("projectSide");
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //获取回退的报表编码
        JSONArray jsonArray = jsonData.getJSONArray("codeValues");
        List<String> codeValues = JSONObject.parseArray(jsonArray.toJSONString(), String.class);
        //获取回退意见
        String returnOpinion = jsonData.getString("returnOpinion");
        //获取用户信息
        S_USER_INFO userInfo = (S_USER_INFO)req.getAttribute("LOGIN_USER");
        //判断参数是否为空
        if(StringUtils.isBlank(projectSide) || StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)
                || userInfo == null || codeValues == null){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "回退失败！");
        }
        Map<String, String> paramsMap = JSONObject.parseObject(jsonData.toJSONString(), new TypeReference<Map<String, String>>(){});
        paramsMap.put("userName",userInfo.getUserName());
        int result = clientSideService.reportReturn(theMonth,company,codeValues,projectSide,returnOpinion,userInfo);
        if(result == 1){
            // 异步插入日志数据库
            logService.addLog(S_LOGIN_LOG.LOGTYPE_OPERTION,"报表回退成功","", userInfo, CommonUtil.getLoginIp(req));
            return JSONResponseBody.createSuccessResponseBody("报表回退成功！", null);
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "报表回退失败！");
    }

}
