package cn.csg.ucs.bi.business.mapper.calculate;

import cn.csg.ucs.bi.business.entity.dto.CalculateSideSummaryDTO;
import cn.csg.ucs.bi.business.mapper.calculate.provider.CalculateSideProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CalculateSideMapper {


    @SelectProvider(type = CalculateSideProvider.class, method = "getReportSummary")
    @Results({
            @Result(column = "REPORT_ID", property = "reportId"),
            @Result(column = "CODE_NAME", property = "reportName"),
            @Result(column = "SMART_ID", property = "smartId"),
            @Result(column = "CODE_VALUE", property = "codeValue"),
            @Result(column = "THEMONTH", property = "theMonth"),
            @Result(column = "STATISTICS_STATE", property = "statisticsState"),
            @Result(column = "NO_SUBMIT", property = "noSubmit"),
            @Result(column = "YES_SUBMIT", property = "yesSubmit"),
            @Result(column = "STATISTICS_OPERATOR", property = "statisticsOperator"),
            @Result(column = "STATISTICS_DATE", property = "statisticsDate")
    })
    List<CalculateSideSummaryDTO> getReportSummary(JSONObject jo);
}
