package cn.csg.ucs.bi.business.mapper.require;

import cn.csg.ucs.bi.business.entity.P_SELF_CLIENT_SUMMARY;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.business.entity.dto.BatchSubmitDTO;
import cn.csg.ucs.bi.business.entity.dto.ReportSummaryDTO;
import cn.csg.ucs.bi.business.mapper.require.provider.ClientSideProvider;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClientSideMapper {

    //增
    @InsertProvider(type = ClientSideProvider.class, method = "insertBatchProcess")
    int insertBatchProcess(List<S_STATISTICS_CONFIG> statisticsConfigList,String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues, String projectSide);

    @InsertProvider(type = ClientSideProvider.class, method = "insertBatchContractProcess")
    int insertBatchContractProcess(List<S_STATISTICS_CONFIG> statisticsConfigList,String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues);

    @InsertProvider(type = ClientSideProvider.class, method = "insertBatchReportInfo")
    int insertBatchReportInfo(@Param("list") List<R_REPORT_INFO> list);

    //删
    @DeleteProvider(type = ClientSideProvider.class, method = "deleteReportInfo")
    int deleteReportInfo(String theMonth, String company, String codeValues, String projectSide);

    @DeleteProvider(type = ClientSideProvider.class, method = "deleteSelfClientSummary")
    int deleteSelfClientSummary(String theMonth, String company, String codeValues, String projectSide);

    @DeleteProvider(type = ClientSideProvider.class, method = "deleteContractSummary")
    int deleteContractSummary(String theMonth, String company, String codeValues);

    //改
    @UpdateProvider(type = ClientSideProvider.class, method = "updateReportInfo")
    int updateReportInfo(String theMonth, String company, String codeValues, String projectSide, S_USER_INFO userInfo);

    @UpdateProvider(type = ClientSideProvider.class, method = "updateAllCompanyReportInfo")
    int updateAllCompanyReportInfo(String theMonth, String company, String projectSide, S_USER_INFO userInfo);

    @UpdateProvider(type = ClientSideProvider.class, method = "updateSubmitState")
    int updateSubmitState(String theMonth, String companys, String codeValue, String projectSide, S_USER_INFO userInfo);

    @UpdateProvider(type = ClientSideProvider.class, method = "reportReturn")
    int reportReturn(String theMonth, String company, String codeValues, String projectSide, String returnOpinion, S_USER_INFO userInfo);

    @UpdateProvider(type = ClientSideProvider.class, method = "updateDataState")
    int updateDataState(@Param("theMonth") String theMonth, @Param("companys") String companys,String tableName,@Param("codeValues")  String codeValues, String projectSide,boolean isLock);

    //查
//    @Select("SELECT DECODE(r.REPORT_ID, null, sys_guid(), r.REPORT_ID) AS REPORT_ID, s.CODE_NAME, s.CODE_VALUE, #{theMonth} THEMONTH, r.STATISTICS_DATE, r.SUBMIT_OPERATOR, r.SUBMIT_DATE, r.RETURN_OPINION, " +
//            "DECODE(r.STATISTICS_STATE,null,0,r.STATISTICS_STATE) STATISTICS_STATE, DECODE(r.SUBMIT_STATE,null,0,r.SUBMIT_STATE) SUBMIT_STATE, " +
//            "r.STATISTICS_OPERATOR FROM (SELECT * FROM S_CODE_INFO  WHERE CODE_TYPE = #{codeType}) s LEFT JOIN " +
//            "(SELECT * FROM R_REPORT_INFO WHERE COMPANY = #{company} AND TO_CHAR(THEMONTH, 'yyyy-MM') = #{theMonth} AND PROJECT_SIDE = #{projectSide}) r " +
//            "ON s.CODE_VALUE = r.REPORT_CODE ORDER BY s.CODE_ORDER")
    @SelectProvider(type = ClientSideProvider.class, method = "getReportSummary")
    @Results({
            @Result(column = "REPORT_ID", property = "reportId"),
            @Result(column = "CODE_NAME", property = "reportName"),
            @Result(column = "SMART_ID", property = "smartId"),
            @Result(column = "CODE_VALUE", property = "codeValue"),
            @Result(column = "THEMONTH", property = "theMonth"),
            @Result(column = "STATISTICS_STATE", property = "statisticsState"),
            @Result(column = "SUBMIT_STATE", property = "submitState"),
            @Result(column = "NO_SUBMIT", property = "noSubmit"),
            @Result(column = "YES_SUBMIT", property = "yesSubmit"),
            @Result(column = "STATISTICS_OPERATOR", property = "statisticsOperator"),
            @Result(column = "STATISTICS_DATE", property = "statisticsDate"),
            @Result(column = "SUBMIT_OPERATOR", property = "submitOperator"),
            @Result(column = "SUBMIT_DATE", property = "submitDate"),
            @Result(column = "RETURN_OPINION", property = "returnOpinion"),
            @Result(column = "PROJECT_SIDE", property = "projectSide")
    })
    List<ReportSummaryDTO> getReportSummary(String codeType, String projectSide, String company, String theMonth);

    @SelectProvider(type = ClientSideProvider.class, method = "queryByCodeValue")
    @Results({
            @Result(column = "CONFIG_ID", property = "configId"),
            @Result(column = "PROJECT_CODE", property = "projectCode"),
            @Result(column = "STATISTICS_CATEGORY", property = "statisticsCateGory"),
            @Result(column = "TABLE_NAME", property = "tableName"),
            @Result(column = "COLUMN_NAME", property = "columnName"),
            @Result(column = "STATISTICS_TYPE", property = "statisticsType")
    })
    List<S_STATISTICS_CONFIG> getStatisticsConfigList(String codeType,String codeValues);

    @SelectProvider(type = ClientSideProvider.class, method = "queryTableByCodeValues")
    List<String> getTableByCodeValues(@Param("codeType") String codeType, @Param("codeValues") String codeValues);

    @SelectProvider(type = ClientSideProvider.class, method = "getSelfClientSummaryByParams")
    @Results({
            @Result(column = "GUID", property = "guid"),
            @Result(column = "THEMONTH", property = "theMonth"),
            @Result(column = "PROJECT_CODE", property = "projectCode"),
            @Result(column = "STATISTICS_CATEGORY", property = "statisticsCategory"),
            @Result(column = "MONTH_VALUE", property = "monthValue"),
            @Result(column = "TOTAL_VALUE", property = "totalValue"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "OPERATE_DATE", property = "operateDate"),
            @Result(column = "OPERATOR_COMPANY", property = "operatorCompany"),
            @Result(column = "COMPANY", property = "company")
    })
    List<P_SELF_CLIENT_SUMMARY> getSelfClientSummaryByParams(@Param("theMonth") String theMonth, @Param("company") String company, @Param("codeValues") String codeValues, @Param("codeType") String codeType);

//    @Select("SELECT r.* FROM R_REPORT_INFO r where r.REPORT_CODE in (#{codeValues}) and r.COMPANY = '#{company}' and to_char(r.THEMONTH,'yyyy-mm') = '#{theMonth}' ")
    @SelectProvider(type = ClientSideProvider.class, method = "getReportInfoByParams")
    @Results({
            @Result(column = "REPORT_ID", property = "reportId"),
            @Result(column = "REPORT_CODE", property = "reportCode"),
            @Result(column = "COMPANY", property = "company"),
            @Result(column = "STATISTICS_DATE", property = "statisticsDate"),
            @Result(column = "STATISTICS_OPERATOR", property = "statisticsOperator"),
            @Result(column = "SUBMIT_DATE", property = "submitDate"),
            @Result(column = "SUBMIT_OPERATOR", property = "submitOperator"),
            @Result(column = "STATISTICS_STATE", property = "statisticsState"),
            @Result(column = "SUBMIT_STATE", property = "submitState"),
            @Result(column = "RETURN_OPINION", property = "returnOpinion"),
            @Result(column = "THEMONTH", property = "theMonth"),
            @Result(column = "PROJECT_CATEGORY", property = "projectCategory")
    })
    List<R_REPORT_INFO> getReportInfoByParams(@Param("theMonth")String theMonth, @Param("company")String company, @Param("codeValues")String codeValues, @Param("projectSide")String projectSide);

    @SelectProvider(type = ClientSideProvider.class, method = "getStatisticsedCompany")
    List<String> getStatisticsedCompany(String theMonth, String codeValue, String projectSide, String stateType, String stateValue);

    @SelectProvider(type = ClientSideProvider.class, method = "getNoSubmitNextCompany")
    @Results({
            @Result(column = "REPORT_ID", property = "reportId"),
            @Result(column = "CODE_VALUE", property = "codeValue"),
            @Result(column = "ORG_NAME", property = "orgName"),
            @Result(column = "ORG_SHORT_NAME", property = "orgShortName"),
            @Result(column = "STATISTICS_STATE", property = "statisticsState"),
            @Result(column = "SUBMIT_STATE", property = "submitState")
    })
    List<BatchSubmitDTO> getNoSubmitNextCompany(String theMonth, String codeValue, String projectSide, String company);

    @SelectProvider(type = ClientSideProvider.class, method = "getNoStatistAndSubmit")
    List<String> getNoStatistAndSubmitCodeValue(String theMonth, String company, String codeType, String projectSide);

    @SelectProvider(type = ClientSideProvider.class, method = "getNoSubmitAllNextCompany")
    List<String> getNoSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide, boolean isStatistics);

    @SelectProvider(type = ClientSideProvider.class, method = "getSubmitAllNextCompany")
    List<String> getSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide);

}
