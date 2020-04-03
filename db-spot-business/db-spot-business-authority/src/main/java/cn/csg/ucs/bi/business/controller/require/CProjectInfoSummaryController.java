package cn.csg.ucs.bi.business.controller.require;

import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.S_PROJECT_OPTION_CONFIG;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.ProjectInfoSummaryDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.service.require.CProjectInfoSummaryService;
import cn.csg.ucs.bi.business.service.require.impl.ClientSideServiceImpl;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目信息汇总Controller
 * @author G.A.N
 * @Date 2019-12-19
 */
@CrossOrigin
@RestController
@RequestMapping("/projectInfoSummary")
public class CProjectInfoSummaryController {

    private Logger logger = LoggerFactory.getLogger(CProjectInfoSummaryController.class);

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService userMgtService;

    @Autowired
    private CProjectInfoSummaryService cProjectInfoSummaryService;

    @Autowired
    private CommonService commonService;

    //根据下拉选项查询汇总报表情况
    @GetMapping("/search")
    public @ResponseBody
    Object getSummary(String params) throws Exception  {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
//        String theMonth = "2019-12";
        //获取所属单位
        String company = jsonData.getString("company");
//        String company = "03";
        //获取编码类型
        S_PROJECT_OPTION_CONFIG projectOptionConfig = jsonData.getObject("projectOptionConfig",S_PROJECT_OPTION_CONFIG.class);
//        S_PROJECT_OPTION_CONFIG projectOptionConfig = new S_PROJECT_OPTION_CONFIG();
//        projectOptionConfig.setProjectCode("36");
//        projectOptionConfig.setProjectType("8");
//        projectOptionConfig.setStatisticsCategory("3");
//        projectOptionConfig.setCategory("4,5");
//        projectOptionConfig.setCaliberType("0");
        //判断参数是否为空
        if(StringUtils.isBlank(theMonth) || StringUtils.isBlank(company) || projectOptionConfig == null){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "参数缺失！");
        }
        List<ProjectInfoSummaryDTO> result = cProjectInfoSummaryService.getReportInfoSummary(company,theMonth,projectOptionConfig);
        //报表汇总查询
        return JSONResponseBody.createSuccessResponseBody("查询成功！", result);

    }

    //根据下拉选项查询汇总报表情况
    @GetMapping("/getCategoryDropDown/{projectCategory}")
    @ResponseBody
    public JSONResponseBody getCategoryDropDown(@PathVariable("projectCategory") String projectCategory) throws Exception {

        logger.info("进入获取分类下拉框，参数："+projectCategory);

        //判断参数是否为空
        if(StringUtils.isBlank(projectCategory)){
            logger.error("进入获取分类下拉框，参数为空");
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "参数缺失！");
        }

        List<DropDown> list = cProjectInfoSummaryService.getCategoryDropDown(projectCategory);

        //报表汇总查询
        return JSONResponseBody.createSuccessResponseBody("获取成功",list);

    }

    @ResponseBody
    @GetMapping("/getDropDownCodesByCodeType")
    public JSONResponseBody getDropDownCodesByCodeType() {
        List<DropDown> values = commonService.getDropDownCodesByCodeType("1");
        values.addAll(commonService.getDropDownCodesByCodeType("23"));
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

}
