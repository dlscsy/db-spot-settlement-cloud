package cn.csg.ucs.bi.business.controller.calculate;

import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.CalculateSideSummaryDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.service.calculate.impl.CalculateSideServiceImpl;
import cn.csg.ucs.bi.business.service.require.impl.ClientSideServiceImpl;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
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
import java.util.List;
import java.util.Map;

/**
 * 计量侧报表汇总Controller
 * @author G.A.N
 * @Date 2019-12-02
 */
@CrossOrigin
@RestController
@RequestMapping("/calculateSideSummary")
public class CalculateSideSummaryController {

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService userMgtService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CalculateSideServiceImpl calculateSideService;

    //查询汇总情况
    @GetMapping("/search")
    public @ResponseBody
    Object getSummary(String params) throws Exception {

        //请求参数转换为json对象
        JSONObject jsonData = JSONObject.parseObject(params);
        //获取统计年月
        String theMonth = jsonData.getString("theMonth");
        //获取所属单位
        String company = jsonData.getString("company");
        //判断参数是否为空
        if(StringUtils.isBlank(theMonth) || StringUtils.isBlank(company)){
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询失败！");
        }
        //报表汇总查询
        List<CalculateSideSummaryDTO> resultList = calculateSideService.getReportSummary(jsonData);
        JSONObject result = new JSONObject();
        result.put("rows", resultList);
        if(resultList != null && resultList.size() > 0){
            return JSONResponseBody.createSuccessResponseBody("查询成功！", result);
        }
        return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "查询失败！");
    }

}
