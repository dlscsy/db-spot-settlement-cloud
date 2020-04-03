package cn.csg.ucs.bi.business.mapper.require;

import cn.csg.ucs.bi.business.entity.B_SAVE_SERVICE;
import cn.csg.ucs.bi.business.entity.S_PROJECT_OPTION_CONFIG;
import cn.csg.ucs.bi.business.entity.dto.ProjectInfoSummaryDTO;
import cn.csg.ucs.bi.business.mapper.require.provider.CProjectInfoSummaryProvider;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.structure.DropDown;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CProjectInfoSummaryMapper {

    @SelectProvider(type = CProjectInfoSummaryProvider.class, method = "getReportInfoSummary")
    @Results({
            @Result(column = "ORG_CODE", property = "orgCode"),
            @Result(column = "ORG_NAME", property = "orgName"),
            @Result(column = "MONTH_VALUE", property = "monthValue"),
            @Result(column = "TOTAL_VALUE", property = "totalValue")
    })
    List<ProjectInfoSummaryDTO> getReportInfoSummary(String company, String theMonth, S_PROJECT_OPTION_CONFIG projectOptionConfig);

    @SelectProvider(type = CProjectInfoSummaryProvider.class, method = "getCategoryDropDown")
    @Results({
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "OPTION_NAME", property = "optionName"),
            @Result(column = "PROJECT_CODE", property = "projectCode"),
            @Result(column = "STATISTICS_CATEGORY", property = "statisticsCategory"),
            @Result(column = "CATEGORY", property = "category"),
            @Result(column = "CALIBER_TYPE", property = "caliberType")
    })
    List<S_PROJECT_OPTION_CONFIG> getCategoryDropDown(String projectCategory);
}
