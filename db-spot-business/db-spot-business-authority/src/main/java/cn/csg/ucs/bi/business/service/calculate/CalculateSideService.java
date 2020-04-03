package cn.csg.ucs.bi.business.service.calculate;

import cn.csg.ucs.bi.business.entity.dto.CalculateSideSummaryDTO;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface CalculateSideService {

    /**
     * 根据所属单位、统计年月获取报表信息
     * @param jo 封装所属单位、统计年月的json对象
     * @return 报表汇总信息List
     */
    List<CalculateSideSummaryDTO> getReportSummary(JSONObject jo);

}
