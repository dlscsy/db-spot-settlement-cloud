package cn.csg.ucs.bi.business.mapper.require.provider;

import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import org.apache.commons.lang.StringUtils;
import java.util.List;

/**
 * @description: 客户侧报表汇总provider
 * @author: G.A.N
 * @create: 2019-11-15
 */
public class ClientSideProvider {

    //统计类别：0
    private static final String STATISTICSCATEGORY_0 = "0";
    //统计类别：1
    private static final String STATISTICSCATEGORY_1 = "1";
    //统计类别：1
    private static final String STATISTICSCATEGORY_18 = "18";
    //统计类别：8
    private static final String STATISTICSCATEGORY_8 = "8";
    //统计方式：0
    private static final String STATISTICSTYPE_0 = "0";
    //统计方式：1
    private static final String STATISTICSTYPE_1 = "1";
    //统计方式：2
    private static final String STATISTICSTYPE_2 = "2";
    //上报状态：1
    private static final String SUBMITSTATE_1 = "1";
    //自身侧：0
    private static final String SELF_SIDE = "0";
    //客户侧：1
    private static final String CLIENT_SIDE = "1";
    //合同：35（S_STATISTICS_CONFIG中project_code值）
    private static final String CONTRACT = "35";
    //非合同：36（S_STATISTICS_CONFIG中project_code值）
    private static final String UNCONTRACT = "36";
    //节能服务项目编码（19、20、21、22）
    private static final String SERVICE_CODE = "19,20,21,22";
    //上报状态
    private static final String SUBMIT_1 = "1";
    //统计配置表中高损变压器项目编码
    private static final String TRANSFORMER_CODE = "31";
    //统计状态：0未统计
    private static final String STATISTICS_STATE_0 = "0";
    //统计状态：1已统计
    private static final String STATISTICS_STATE_1 = "1";

    /**
     * @param codeType    编码类别（7：自身侧报表编码 8：客户侧编码）
     * @param projectSide 项目所属侧(0:自身侧 1：客户侧)
     * @param theMonth    统计年月
     * @param company     所属单位
     * @return 封装的sql
     * @description: 根据编码类别和报表编码获取统计配置类
     */
    public String getReportSummary(String codeType, String projectSide, String company, String theMonth) {

        //获取传入单位的下级单位个数sql
        String allNextCompanyCountSql = "(select count(s.org_code) from S_ORG_CODE_INFO s where s.parent_org_code = '" + company + "')";
        //获取传入单位的下级单位信息sql
        String allNextCompanySql = "(select * from S_ORG_CODE_INFO s where s.parent_org_code = '" + company + "')";
        //获取下级已统计个数
        String yesSubmitSql = "to_char((select count(t.org_code) from " + allNextCompanySql + " t where t.org_code in (select r.company from R_REPORT_INFO r where r.SUBMIT_STATE = '1' and to_char(r.themonth, 'yyyy-mm') = '" + theMonth +
                "' and r.PROJECT_SIDE = '" + projectSide + "' and r.report_code = s.CODE_VALUE)))";
        //获取下级未统计个数
        String noSubmitSql = "to_char((select count(t.org_code) from " + allNextCompanySql + " t where t.org_code not in (select r.company from R_REPORT_INFO r where r.SUBMIT_STATE = '1' and  to_char(r.themonth, 'yyyy-mm') = '" + theMonth +
                "' and r.PROJECT_SIDE = '" + projectSide + "' and r.report_code = s.CODE_VALUE)))";
        String columnSql = "DECODE(r.REPORT_ID, null, sys_guid(), r.REPORT_ID) AS REPORT_ID, s.CODE_NAME, s.CODE_VALUE, '" +
                theMonth + "' AS THEMONTH, r.STATISTICS_DATE, r.SUBMIT_OPERATOR, r.SUBMIT_DATE, r.RETURN_OPINION, " +
                "DECODE(r.STATISTICS_STATE,null,0,r.STATISTICS_STATE) STATISTICS_STATE, DECODE(r.SUBMIT_STATE,null,0,r.SUBMIT_STATE) SUBMIT_STATE, " +
                "r.STATISTICS_OPERATOR, CASE WHEN " + allNextCompanyCountSql + " > 0 THEN " + yesSubmitSql + " else (case SUBMIT_STATE when '1' then '1' else '0' end) end AS yes_submit," +
                "CASE WHEN " + allNextCompanyCountSql + " > 0 THEN " + noSubmitSql + " else (case SUBMIT_STATE when '1' then '0' else '1' end) end AS no_submit, rel.SMART_ID ";

        String table1 = "(SELECT * FROM S_CODE_INFO  WHERE CODE_TYPE = " + codeType + ") s ";
        String table2 = "(SELECT * FROM R_REPORT_INFO WHERE COMPANY = '" + company + "' AND TO_CHAR(THEMONTH, 'yyyy-MM') = '" + theMonth + "' AND PROJECT_SIDE = '" + projectSide + "') r ";
        String onSql = "ON s.CODE_VALUE = r.REPORT_CODE ";
        StringBuffer searchsql = new StringBuffer("SELECT ").append(columnSql).append(" FROM ");
        searchsql.append(table1).append(" LEFT JOIN ");
        searchsql.append(table2).append(onSql);
        searchsql.append("LEFT JOIN ");
        searchsql.append("(SELECT * FROM S_SMARTBI_REPORT_REL) rel ");
        searchsql.append("ON s.CODE_TYPE = rel.REPORT_TYPE AND s.CODE_VALUE = rel.REPORT_CODE ORDER BY s.CODE_ORDER");
        return searchsql.toString();
    }

    /**
     * @param codeType   编码类别(0:自身侧 1：客户侧)
     * @param codeValues 报表编码集合
     * @return 封装的sql
     * @description: 根据编码类别和报表编码获取统计配置类
     */
    public String queryByCodeValue(String codeType, String codeValues) {

        StringBuffer sbf = new StringBuffer("SELECT * FROM S_STATISTICS_CONFIG s WHERE s.PROJECT_CODE IN " +
                "(SELECT t1.CODE_VALUE FROM S_CODE_INFO t1, S_CODE_INFO t2 WHERE t1.PARENT_CODE_ID = t2.CODE_ID ");
        if (codeValues != "" && !"".equals(codeValues)) {
            sbf.append("AND t2.CODE_VALUE in (" + codeValues + ") ");
        }
        sbf.append("AND t2.CODE_TYPE = " + codeType + ") ");
        sbf.append("ORDER BY s.STATISTICS_CATEGORY");
        return sbf.toString();
    }

    /**
     * @param codeType   编码类别(0:自身侧 1：客户侧)
     * @param codeValues 报表编码集合
     * @return 封装的sql
     * @description: 根据编码类别和报表编码获取表名
     */
    public String queryTableByCodeValues(String codeType, String codeValues) {

        String inValueSql = "select regexp_substr(#{codeValues}, '[^,]+', 1, level, 'i') as id_arr from dual " +
                "connect by level <= length(#{codeValues}) - length(regexp_replace(#{codeValues}, ',', '')) + 1";
        StringBuffer sbf = new StringBuffer("SELECT DISTINCT s.TABLE_NAME FROM S_STATISTICS_CONFIG s WHERE s.PROJECT_CODE IN " +
                "(SELECT t1.CODE_VALUE FROM S_CODE_INFO t1, S_CODE_INFO t2 WHERE t1.PARENT_CODE_ID = t2.CODE_ID ");
        if (codeValues != "" && !"".equals(codeValues)) {
            sbf.append("AND t2.CODE_VALUE in ("+ inValueSql +") ");
        }
        sbf.append("AND t2.CODE_TYPE = #{codeType})");
        return sbf.toString();
    }

    /**
     * @param theMonth   统计年月
     * @param company    所属单位
     * @param codeType   编码类别(0:自身侧 1：客户侧)
     * @param codeValues 报表编码集合
     * @return 封装的sql
     * @description: 根据编码类别和报表编码获取统计配置类
     */
    public String getSelfClientSummaryByParams(String theMonth, String company, String codeValues, String codeType) {

        StringBuffer sbf = new StringBuffer("SELECT * FROM P_SELF_CLIENT_SUMMARY p WHERE p.COMPANY = '" + company +
                "' and to_char(p.THEMONTH,'yyyy-mm') = '" + theMonth + "' AND p.PROJECT_CODE IN " +
                "(SELECT t1.CODE_VALUE FROM S_CODE_INFO t1, S_CODE_INFO t2 WHERE t1.PARENT_CODE_ID = t2.CODE_ID ");
        sbf.append("AND T2.CODE_VALUE in (" + codeValues + ") AND t2.CODE_TYPE = " + codeType + ")");
        return sbf.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param projectSide 项目所属侧(0:自身侧 1：客户侧)
     * @param codeValues  报表编码集合
     * @return 封装的sql
     * @description: 根据条件获取报表信息
     */
    public String getReportInfoByParams(String theMonth, String company, String codeValues, String projectSide) {

        StringBuffer sbf = new StringBuffer("SELECT r.* FROM R_REPORT_INFO r where r.PROJECT_SIDE = " + projectSide +
                " AND r.REPORT_CODE in (" + codeValues + ") and r.COMPANY = '" + company +
                "' and to_char(r.THEMONTH,'yyyy-mm') = '" + theMonth + "' ");
        return sbf.toString();
    }

    /**
     * @param statisticsConfigList 统计类别配置表集合
     * @param theMonth             统计年月
     * @param company              所属单位
     * @param codeValues           前端勾选的报表编码
     * @param projectSide          项目所在侧（0：自身侧 1：客户侧）
     * @return 统计报表的中间结果批量插入中间过程表sql
     * @description: 报表统计结果存入中间过程表P_SELF_CLIENT_SUMMARY
     */
    public String insertBatchProcess(List<S_STATISTICS_CONFIG> statisticsConfigList, String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues, String projectSide) {

        //中间过程表字段sql
        String COLUMN = "GUID, THEMONTH, PROJECT_CODE, STATISTICS_CATEGORY, MONTH_VALUE, TOTAL_VALUE, OPERATOR, OPERATE_DATE, OPERATOR_COMPANY, COMPANY";
        //中间过程表插入值sql
        String VALUES = statisticsReport(statisticsConfigList, theMonth, company, userInfo, codeValues, projectSide);
        //封装批量插入sql
        StringBuffer sbf = new StringBuffer("INSERT ALL INTO P_SELF_CLIENT_SUMMARY (" + COLUMN + ") ");
        return sbf.append(VALUES).toString();
    }

    /**
     * @param statisticsConfigList 统计类别配置表集合
     * @param theMonth             统计年月
     * @param company              所属单位
     * @param codeValues           前端勾选的报表编码
     * @return 统计报表的中间结果批量插入中间过程表sql
     * @description: 报表统计合同/非合同结果存入中间过程表P_CONTRACT_SUMMARY
     */
    public String insertBatchContractProcess(List<S_STATISTICS_CONFIG> statisticsConfigList, String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues) {

        //中间过程表字段sql
        String COLUMN = "GUID, THEMONTH, COMPANY, PROJECT_CODE, STATISTICS_CATEGORY, CALIBER_TYPE, CATEGORY, CONTRACT_TYPE, EX_SELF_IMPL, MONTH_VALUE, TOTAL_VALUE, OPERATOR, OPERATE_DATE, OPERATOR_COMPANY";
        //中间过程表插入值sql
        String VALUES = statisticsContractReport(statisticsConfigList, theMonth, company, userInfo, codeValues);
        //封装批量插入sql
        StringBuffer sbf = new StringBuffer("INSERT ALL INTO P_CONTRACT_SUMMARY (" + COLUMN + ") ");
        return sbf.append(VALUES).toString();
    }

    /**
     * @param statisticsConfigList 统计类别配置表集合
     * @param theMonth             统计年月
     * @param company              所属单位
     * @param codeValues           前端勾选的报表编码
     * @return 统计报表的sql
     * @description: 封装统计逻辑
     */
    private String statisticsReport(List<S_STATISTICS_CONFIG> statisticsConfigList, String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues, String projectSide) {

        String statisticsCateGory_0 = "";
        //封装节能服务项目sql
        StringBuffer statisticsServiceSql = new StringBuffer();
        //封装其他项目sql
        StringBuffer statisticsSql = new StringBuffer();

        for (int i = 1; i <= statisticsConfigList.size(); i++) {
            S_STATISTICS_CONFIG statisticsConfig = statisticsConfigList.get(i - 1);
            //项目编码
            String projectCode = statisticsConfig.getProjectCode();
            //统计类别
            String statisticsCateGory = statisticsConfig.getStatisticsCateGory();
            //统计相应数据的表名
            String tableName = statisticsConfig.getTableName();
            //统计相应数据的表的字段名
            String columnName = statisticsConfig.getColumnName();
            //统计方式（0：本月 1：累计 2：本月和累计）
            String statisticsType = statisticsConfig.getStatisticsType();
            //当前用户名字
            String operator = userInfo.getUserName();
            //当前用户所属单位
            String operatorCompany = userInfo.getOrgCode();
            //统计方法：统计类别为1或8时COUNT(字段)，其他SUM(字段)
            String statisticsMethod = ClientSideProvider.STATISTICSCATEGORY_1.equals(statisticsCateGory) ||
                    ClientSideProvider.STATISTICSCATEGORY_8.equals(statisticsCateGory) || ClientSideProvider.STATISTICSCATEGORY_18.equals(statisticsCateGory)
                    ? "COUNT(" + columnName + ")" : "NVL(SUM(" + columnName + "),0)";
            //统计条件：所属公司
            String statisticsCompany = "COMPANY LIKE CONCAT('" + company + "', '%')";
            //如果是自身侧，则用于关联基表
            String selfSaveBaseSql = ClientSideProvider.SELF_SIDE.equals(projectSide) ? " t join B_SELF_SAVE_BASE b ON t.PROJECT_ID = b.PROJECT_ID " : "";
            //数据状态
//            String dataStateSql = "DATA_STATE = '0' AND ";

            //封装统计节能服务项目的sql(当本月新增或者累计无数据时，统计会出错)
            if (ClientSideProvider.STATISTICSCATEGORY_0.equals(statisticsCateGory)) {
//                //查询时间条件(如果是项目编码19~22的，时间累计至今，否则累计当年1月至今)
//                String timeSql = ClientSideProvider.SERVICE_CODE.indexOf(statisticsConfig.getProjectCode()) > -1 ? "TO_CHAR(THEMONTH, 'yyyy-mm') <= '" + theMonth + "'" :
//                        "TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' ";
//                //判断statisticsServiceSql是否有值，有则继续循环，否则往statisticsServiceSql存sql
//                if (StringUtils.isNotBlank(statisticsServiceSql.toString())) continue;
//                statisticsServiceSql.append("(SELECT dbms_random.string('X', 36) AS GUID,TO_DATE('" + theMonth + "', 'yyyy-mm') AS THEMONTH,b.PROJECT_CODE," +
//                        " '" + statisticsCateGory + "' AS STATISTICS_CATEGORY, b.MONTH_VALUE, b1.TOTAL_VALUE, '" + operator + "' AS OPERATOR, SYSDATE AS OPERATE_DATE, " +
//                        "'" + operatorCompany + "' AS OPERATOR_COMPANY, '" + company + "' AS COMPANY FROM ");
//                statisticsServiceSql.append("(SELECT PROJECT_CODE, COMPANY, " + statisticsMethod + " AS MONTH_VALUE FROM " + tableName + " " +
//                        "WHERE " + dataStateSql + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' GROUP BY PROJECT_CODE, COMPANY) b，" +
//                        "(SELECT PROJECT_CODE, COMPANY, " + statisticsMethod + " AS TOTAL_VALUE FROM " + tableName + " WHERE " + dataStateSql + statisticsCompany + " AND " +
//                        timeSql + " GROUP BY PROJECT_CODE, COMPANY) b1 WHERE DECODE(b.PROJECT_CODE,NULL,b1.PROJECT_CODE,b.PROJECT_CODE) = b1.PROJECT_CODE)");
//                if (codeValues.size() > 1) {
//                    statisticsServiceSql.append(" union all ");
//                }
                //查询时间条件(如果是项目编码19~22的，时间累计至今，否则累计当年1月至今)
                String timeSql = ClientSideProvider.SERVICE_CODE.indexOf(statisticsConfig.getProjectCode()) > -1 ? "TO_CHAR(THEMONTH, 'yyyy-mm') <= '" + theMonth + "'" :
                        "TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' ";
                statisticsServiceSql.append("(SELECT dbms_random.string('X', 36) AS GUID,TO_DATE('" + theMonth + "', 'yyyy-mm') AS THEMONTH,'" + projectCode + "' AS PROJECT_CODE," +
                        " '" + statisticsCateGory + "' AS STATISTICS_CATEGORY,b.MONTH_VALUE, b1.TOTAL_VALUE, '" + operator + "' as OPERATOR, SYSDATE AS OPERATE_DATE, '"+
                        operatorCompany + "' AS OPERATOR_COMPANY, '" + company + "' AS COMPANY FROM ");
                statisticsServiceSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE FROM " + tableName + selfSaveBaseSql +
                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' AND  PROJECT_CODE = '"+ projectCode +"') b ," +
                        "(SELECT " + statisticsMethod + " AS TOTAL_VALUE from " + tableName + selfSaveBaseSql +
                        " WHERE " + statisticsCompany + " AND " + timeSql + " AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth +
                        "' AND  PROJECT_CODE = '"+ projectCode +"') b1 ) ");
                if (i != statisticsConfigList.size()) {
                    statisticsServiceSql.append(" UNION ALL ");
                }
            } else {
                //封装统计其他项目的sql
                statisticsSql.append("(SELECT dbms_random.string('X', 36) AS GUID,TO_DATE('" + theMonth + "', 'yyyy-mm') AS THEMONTH,'" + projectCode + "' AS PROJECT_CODE," +
                        " '" + statisticsCateGory + "' AS STATISTICS_CATEGORY, ");
                //判断统计方式：0本月，1累计，2本月和累计，用于动态拼接字段
                if (ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType)) {
                    //只统计本月
                    statisticsSql.append("b.MONTH_VALUE, NULL AS TOTAL_VALUE, ");
                } else if (ClientSideProvider.STATISTICSTYPE_1.equals(statisticsType)) {
                    //只统计累计
                    statisticsSql.append("NULL AS MONTH_VALUE, b1.TOTAL_VALUE, ");
                } else {
                    //统计本月和累计
                    statisticsSql.append("b.MONTH_VALUE, b1.TOTAL_VALUE, ");
                }
                statisticsSql.append("'" + operator + "' as OPERATOR, SYSDATE AS OPERATE_DATE, " +
                        "'" + operatorCompany + "' AS OPERATOR_COMPANY, '" + company + "' AS COMPANY FROM ");
                //判断统计方式：0本月，1累计，2本月和累计，用于动态拼接统计条件
                if (ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType)) {
                    //只统计本月
                    statisticsSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE FROM " + tableName + selfSaveBaseSql +
                            " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "') b )");
                } else if (ClientSideProvider.STATISTICSTYPE_1.equals(statisticsType)) {//只统计累计
                    //只统计累计
//                    if (ClientSideProvider.TRANSFORMER_CODE.equals(statisticsConfig.getProjectCode())) {
                        //高损变压器特殊处理
//                        statisticsSql.append("(SELECT " + statisticsMethod + " AS TOTAL_VALUE FROM " + tableName + selfSaveBaseSql + " WHERE " + statisticsCompany +
//                                " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "') b1 )");
//                    } else {
                        //除高损变压器外
                        statisticsSql.append("(SELECT " + statisticsMethod + " AS TOTAL_VALUE FROM " + tableName + selfSaveBaseSql + " WHERE " + statisticsCompany +
                                " AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "') b1 )");
//                    }
                } else {
                    //统计本月和累计
                    if (ClientSideProvider.TRANSFORMER_CODE.equals(statisticsConfig.getProjectCode())) {
                        //高损变压器特殊处理
                        statisticsSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE FROM " + tableName + selfSaveBaseSql +
                                " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' AND RE_OR_EL_MONTH is not null) b ," +
                                "(SELECT " + statisticsMethod + " AS TOTAL_VALUE from " + tableName + selfSaveBaseSql +
                                " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' AND RE_OR_EL_MONTH is not null) b1 ) ");
                    } else {
                        //除高损变压器外
                        statisticsSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE FROM " + tableName + selfSaveBaseSql +
                                " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "') b ," +
                                "(SELECT " + statisticsMethod + " AS TOTAL_VALUE from " + tableName + selfSaveBaseSql +
//                            " WHERE "+ statisticsCompany +" AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR(TRUNC(SYSDATE,'yyyy'),'yyyy-mm') AND '"+ theMonth +"') b1 ) ");
                                " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "') b1 ) ");
                    }
                }
                if (i != statisticsConfigList.size()) {
                    statisticsSql.append(" UNION ALL ");
                }
            }
        }
        if (StringUtils.isNotBlank(statisticsServiceSql.toString())) {
            return statisticsServiceSql.append(statisticsSql).toString();
        } else {
            return statisticsSql.toString();
        }
    }

    /**
     * @param statisticsConfigList 统计类别配置表集合
     * @param theMonth             统计年月
     * @param company              所属单位
     * @param codeValues           前端勾选的报表编码
     * @return 统计报表的sql
     * @description: 封装合同/非合同统计逻辑
     */
    private String statisticsContractReport(List<S_STATISTICS_CONFIG> statisticsConfigList, String theMonth, String company, S_USER_INFO userInfo, List<String> codeValues) {

        //封装其他项目sql
        StringBuffer statisticsSql = new StringBuffer();

        for (int i = 1; i <= statisticsConfigList.size(); i++) {
            S_STATISTICS_CONFIG statisticsConfig = statisticsConfigList.get(i - 1);
            //项目编码
            String projectCode = statisticsConfig.getProjectCode();
            //统计类别
            String statisticsCateGory = statisticsConfig.getStatisticsCateGory();
            //统计相应数据的表名
            String tableName = statisticsConfig.getTableName();
            //统计相应数据的表的字段名
            String columnName = statisticsConfig.getColumnName();
            //统计方式（0：本月 1：累计 2：本月和累计）
            String statisticsType = statisticsConfig.getStatisticsType();
            //当前用户名字
            String operator = userInfo.getUserName();
            //当前用户所属单位
            String operatorCompany = userInfo.getOrgCode();
            //统计方法：统计类别为1或8时COUNT(字段)，其他SUM(字段)
            String statisticsMethod = ClientSideProvider.STATISTICSCATEGORY_1.equals(statisticsCateGory) ||
                    ClientSideProvider.STATISTICSCATEGORY_8.equals(statisticsCateGory) ? "COUNT(" + columnName + ")" : "NVL(SUM(" + columnName + "),0)";
            //统计条件：所属公司
            String statisticsCompany = "COMPANY LIKE CONCAT('" + company + "', '%')";
            //是否合同
            boolean isContract = ClientSideProvider.CONTRACT.equals(statisticsConfig.getProjectCode());
            //分组Sql
            String groupSql = isContract ? "GROUP BY CALIBER_TYPE,CATEGORY" :
                    "GROUP BY CALIBER_TYPE,CATEGORY,EX_SELF_IMPL";
            //是否企业自身力量实施字段sql
            String exSelfImplColumnSql = "";
//            String exSelfImplColumnSql = isContract ? "'0' AS EX_SELF_IMPL" : "b.EX_SELF_IMPL";
            if (isContract) {
                exSelfImplColumnSql = "'0' AS EX_SELF_IMPL";
            }else {
                if(!ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType)){
                    exSelfImplColumnSql = "b1.EX_SELF_IMPL";
                }else{
                    exSelfImplColumnSql = "b.EX_SELF_IMPL";
                }
            }
            //是否企业自身力量实施值sql
            String exSelfImplValueSql = isContract ? "" : ",EX_SELF_IMPL";
            //本月和累计拼接条件sql
            String whereSql = isContract ? "b.CALIBER_TYPE = b1.CALIBER_TYPE and b.CATEGORY = b1.CATEGORY" :
//                    "b.CALIBER_TYPE = b1.CALIBER_TYPE and b.CATEGORY = b1.CATEGORY and b.EX_SELF_IMPL = b1.EX_SELF_IMPL";
                    "b.CALIBER_TYPE = b1.CALIBER_TYPE and b.CATEGORY = b1.CATEGORY and " +
                            "((b.ex_self_impl IS NOT NULL AND b.EX_SELF_IMPL = b1.EX_SELF_IMPL) OR (b.EX_SELF_IMPL IS NULL AND b.EX_SELF_IMPL IS NULL))";
            //合同分类字段
            String contractColumnSql = isContract ? "'0' AS CONTRACT_TYPE" : "'1' AS CONTRACT_TYPE";
            //数据状态
//            String dataStateSql = "DATA_STATE = '0' AND ";
            //统计类别和分类
            String statisticsCategorySql = ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType) ? "b.CALIBER_TYPE,b.CATEGORY":"b1.CALIBER_TYPE,b1.CATEGORY";

            statisticsSql.append("(SELECT dbms_random.string('X', 36) AS GUID,TO_DATE('" + theMonth + "', 'yyyy-mm') AS THEMONTH,'" + company + "' AS COMPANY,'" + projectCode + "' AS PROJECT_CODE," +
                    " '" + statisticsCateGory + "' AS STATISTICS_CATEGORY,"+ statisticsCategorySql +"," + contractColumnSql + "," + exSelfImplColumnSql + ", ");
            //判断统计方式：0本月，1累计，2本月和累计，用于动态拼接字段
            if (ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType)) {
                //只统计本月
                statisticsSql.append("b.MONTH_VALUE, NULL AS TOTAL_VALUE, ");
            } else if (ClientSideProvider.STATISTICSTYPE_1.equals(statisticsType)) {
                //只统计累计
                statisticsSql.append("NULL AS MONTH_VALUE, b1.TOTAL_VALUE, ");
            } else {
                //统计本月和累计
                statisticsSql.append("b.MONTH_VALUE, b1.TOTAL_VALUE, ");
            }
            statisticsSql.append("'" + operator + "' as OPERATOR, SYSDATE AS OPERATE_DATE, " +
                    "'" + operatorCompany + "' AS OPERATOR_COMPANY FROM ");

            //判断统计方式：0本月，1累计，2本月和累计，用于动态拼接统计条件
            if (ClientSideProvider.STATISTICSTYPE_0.equals(statisticsType)) {
                //只统计本月
                statisticsSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " FROM " + tableName +
                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' " + groupSql + ") b )");
            } else if (ClientSideProvider.STATISTICSTYPE_1.equals(statisticsType)) {
                //只统计累计
                statisticsSql.append("(SELECT " + statisticsMethod + " AS TOTAL_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " FROM " + tableName +
                        " WHERE " + statisticsCompany + " " +
                        "AND TO_CHAR(THEMONTH, 'yyyy-mm') BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' " + groupSql + ")b1 )");

            } else {
                //统计本月和累计
//                statisticsSql.append("(SELECT " + statisticsMethod + " AS MONTH_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " FROM " + tableName +
//                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' " + groupSql + ") b ," +
//                        "(SELECT " + statisticsMethod + " AS TOTAL_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " from " + tableName +
//                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') " +
//                        "BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' " + groupSql + ")b1 where " + whereSql + ")");
                statisticsSql.append("(SELECT " + statisticsMethod + " AS TOTAL_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " from " + tableName +
                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') " +
                        " BETWEEN TO_CHAR((trunc(to_date('" + theMonth + "','yyyy-mm'),'yyyy')),'yyyy-mm') AND '" + theMonth + "' " + groupSql + ")b1 left join " +
                        "(SELECT " + statisticsMethod + " AS MONTH_VALUE,CALIBER_TYPE,CATEGORY" + exSelfImplValueSql + " FROM " + tableName +
                        " WHERE " + statisticsCompany + " AND TO_CHAR(THEMONTH, 'yyyy-mm') = '" + theMonth + "' " + groupSql + ") b on " + whereSql + ")");
            }
            if (i != statisticsConfigList.size()) {
                statisticsSql.append(" UNION ALL ");
            }
        }
        return statisticsSql.toString();
    }

    /**
     * @param list 报表信息集合
     * @return 封装的sql
     * @description 批量插入报表信息
     */
    public String insertBatchReportInfo(List<R_REPORT_INFO> list) {

        //报表信息表字段sql
        String COLUMN = "(REPORT_ID, REPORT_CODE, COMPANY, STATISTICS_DATE, STATISTICS_OPERATOR, SUBMIT_DATE, THEMONTH, " +
                "SUBMIT_OPERATOR, STATISTICS_STATE, SUBMIT_STATE, RETURN_OPINION, PROJECT_SIDE)";
        //封装批量插入sql
        StringBuffer sbf = new StringBuffer("INSERT All ");
        String INTO = "INTO R_REPORT_INFO";
        for (R_REPORT_INFO item : list) {
            sbf.append(INTO).append(COLUMN).append(" VALUES ");
            sbf.append("('" + item.getReportId() + "','" + item.getReportCode() + "','" + item.getCompany() + "',to_date('" + item.getStatisticsDate() + "','yyyy-mm-dd, hh24:mi:ss'),'"
                    + item.getStatisticsOperator() + "',to_date('" + item.getSubmitDate() + "','yyyy-mm-dd, hh24:mi:ss'),to_date('" + item.getTheMonth() + "','yyyy-mm'),'" + item.getSubmitOperator() + "','"
                    + item.getStatisticsState() + "','" + item.getSubmitState() + "','" + item.getReturnOpinion() + "','" + item.getProjectSide() + "')");
        }
        sbf.append(" select 1 from dual");
        return sbf.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param codeValues  前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @return 封装的sql
     * @description 根据条件删除报表信息
     */
    public String deleteReportInfo(String theMonth, String company, String codeValues, String projectSide) {
        StringBuffer sbf = new StringBuffer(" delete R_REPORT_INFO r where r.report_id in " +
                "(select report_id from R_REPORT_INFO wehre where company = '" + company +
                "' and to_char(themonth, 'yyyy-mm') = '" + theMonth + "' and PROJECT_SIDE = '" + projectSide +
                "' and REPORT_CODE in (" + codeValues + "))");
        return sbf.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param codeValues  前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @return 封装的sql
     * @description 根据条件删除中间结果信息（除合同/非合同外）
     */
    public String deleteSelfClientSummary(String theMonth, String company, String codeValues, String projectSide) {
        StringBuffer sbf = new StringBuffer(" DELETE P_SELF_CLIENT_SUMMARY p1 where  p1.GUID IN " +
                "(SELECT GUID FROM P_SELF_CLIENT_SUMMARY p WHERE p.COMPANY = '" + company +
                "' and to_char(p.THEMONTH, 'yyyy-mm') = '" + theMonth + "' AND p.PROJECT_CODE IN " +
                "(SELECT t1.CODE_VALUE FROM S_CODE_INFO t1, S_CODE_INFO t2  WHERE t1.PARENT_CODE_ID = t2.CODE_ID " +
                "AND t2.CODE_TYPE = '" + projectSide + "'");
        if (!codeValues.contains("0")) {
            sbf.append(" AND t2.CODE_VALUE in (" + codeValues +")");
        }
        sbf.append("))");
        return sbf.toString();
    }

    /**
     * @param theMonth   统计年月
     * @param company    所属单位
     * @param codeValues 前端勾选的报表编码
     * @return 封装的sql
     * @description 根据条件删除合同/非合同中间结果信息
     */
    public String deleteContractSummary(String theMonth, String company, String codeValues) {
        StringBuffer sbf = new StringBuffer(" DELETE P_CONTRACT_SUMMARY p1 where  p1.GUID IN " +
                "(SELECT GUID FROM P_CONTRACT_SUMMARY p WHERE p.COMPANY = '" + company +
                "' and to_char(p.THEMONTH, 'yyyy-mm') = '" + theMonth + "' AND p.PROJECT_CODE IN " +
                "(SELECT t1.code_value FROM S_CODE_INFO t1, S_CODE_INFO t2  WHERE t1.PARENT_CODE_ID = t2.CODE_ID " +
                "AND t2.CODE_TYPE = '1' and t2.code_value in (" + codeValues + ")))");
        return sbf.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param codeValues  前端勾选的报表编码
     * @param projectSide 项目所属侧
     * @param userInfo    当前登录用户信息
     * @return 封装的sql
     * @description 根据条件更新报表上报状态、上报人、上报时间
     */
    public String updateReportInfo(String theMonth, String company, String codeValues, String projectSide, S_USER_INFO userInfo) {
        //封装更新ql
        StringBuffer sbf = new StringBuffer(" UPDATE R_REPORT_INFO r SET r.SUBMIT_STATE = '" + ClientSideProvider.SUBMITSTATE_1 +
                "', r.SUBMIT_OPERATOR = '" + userInfo.getUserName() + "' , r.SUBMIT_DATE = SYSDATE ");
        //封装条件sql
        String whereSql = "WHERE to_char(r.THEMONTH,'yyyy-mm') = '" + theMonth + "' AND r.COMPANY = '" + company + "' AND r.PROJECT_SIDE = '" + projectSide +
                "' AND r.REPORT_CODE IN (" + codeValues + ")";
        return sbf.append(whereSql).toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param projectSide 项目所属侧
     * @param userInfo    当前登录用户信息
     * @return 更新影响行数
     * @description 更新所有下属单位的报表状态（将已统计/未上报—>已统计/已上报）
     */
    public String updateAllCompanyReportInfo(String theMonth, String company, String projectSide, S_USER_INFO userInfo) {
        //封装更新ql
        StringBuffer sbf = new StringBuffer("UPDATE R_REPORT_INFO r ");
        //封装字段sql
        String columnSql = "SET r.SUBMIT_STATE = '" + ClientSideProvider.SUBMITSTATE_1 + "', r.submit_operator = '" + userInfo.getUserName() +
                "', r.SUBMIT_DATE = SYSDATE ";
        //封装字段sql
        String whereSql = "WHERE r.REPORT_ID in " +
                "(SELECT r.REPORT_ID from R_REPORT_INFO r WHERE r.COMPANY IN " +
                "(SELECT t.ORG_CODE FROM S_ORG_CODE_INFO t WHERE t.ORG_CODE IN " +
                "(SELECT DISTINCT r.COMPANY from R_REPORT_INFO r WHERE TO_CHAR(r.THEMONTH, 'yyyy-mm') = '" + theMonth +
                "' AND r.PROJECT_SIDE = '" + projectSide + "' AND r.SUBMIT_STATE = '0') START WITH t.PARENT_ORG_CODE = '" + company +
                "' CONNECT BY PRIOR t.ORG_CODE = t.PARENT_ORG_CODE))";
        return sbf.append(columnSql).append(whereSql).toString();
    }

    /**
     * @param theMonth    统计年月
     * @param companys    所属单位
     * @param projectSide 项目所属侧
     * @param userInfo    当前登录用户信息
     * @return 更新影响行数
     * @description 更新单位的报表状态（将已统计/未上报—>已统计/已上报）
     */
    public String updateSubmitState(String theMonth, String companys, String codeValue, String projectSide, S_USER_INFO userInfo) {
        //封装更新ql
        StringBuffer sbf = new StringBuffer("UPDATE R_REPORT_INFO r ");
        //封装字段sql
        String columnSql = "SET r.SUBMIT_STATE = '" + ClientSideProvider.SUBMITSTATE_1 + "', r.SUBMIT_OPERATOR = '" + userInfo.getUserName() +
                "', r.SUBMIT_DATE = SYSDATE ";
        //封装字段sql
        String whereSql = "WHERE TO_CHAR(r.THEMONTH,'yyyy-mm') = '" + theMonth +
                "' AND r.PROJECT_SIDE = '" + projectSide + "' AND r.REPORT_CODE = '" + codeValue + "' AND r.COMPANY IN ("+ companys +")";
        return sbf.append(columnSql).append(whereSql).toString();
    }

    /**
     * @param theMonth    统计年月
     * @param codeValue   报表编码
     * @param projectSide 项目所属侧
     * @param stateType   查询状态类型（0：统计状态 1：上报状态）
     * @param stateType   状态值（0：未 1：已）
     * @return 封装的sql
     * @description 获取该月已类型报表统计/上报状态的单位集合
     */
    public String getStatisticsedCompany(String theMonth, String codeValue, String projectSide, String stateType, String stateValue) {
        StringBuffer searchSql = new StringBuffer("SELECT DISTINCT r.COMPANY FROM R_REPORT_INFO r WHERE TO_CHAR(r.THEMONTH,'yyyy-mm') = '" + theMonth +
                "' AND r.PROJECT_SIDE = '" + projectSide + "' AND r.REPORT_CODE = '" + codeValue + "' ");
        if ("0".equals(stateType)) {
            searchSql.append(" AND r.STATISTICS_STATE = '" + stateValue + "'");
        } else {
            searchSql.append(" AND r.SUBMIT_STATE = '" + stateValue + "'");
        }
        return searchSql.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param codeValue   报表编码
     * @param projectSide 项目所属侧
     * @param company     所属单位
     * @return 封装的sql
     * @description 获取该月未上报的下级所属单位
     */
    public String getNoSubmitNextCompany(String theMonth, String codeValue, String projectSide, String company) {
        String columnSql = "s.ORG_NAME,s.ORG_SHORT_NAME,r1.REPORT_ID,r1.REPORT_CODE,DECODE(r1.STATISTICS_STATE, null, 0, " +
                "r1.STATISTICS_STATE) AS STATISTICS_STATE,DECODE(r1.SUBMIT_STATE, null, 0, r1.SUBMIT_STATE) AS SUBMIT_STATE ";
        String nextCompanySql = "(SELECT * FROM S_ORG_CODE_INFO t WHERE t.PARENT_ORG_CODE = '" + company + "') s ";
        String reportInfoSql = "(SELECT * FROM R_REPORT_INFO r WHERE TO_CHAR(r.THEMONTH, 'yyyy-mm') = '" + theMonth +
                "' AND R.PROJECT_SIDE = '" + projectSide + "' AND R.REPORT_CODE = '" + codeValue + "') r1 ";
        String onSql = "ON s.ORG_CODE = r1.COMPANY ORDER BY STATISTICS_STATE,SUBMIT_STATE";
        StringBuffer searchSql = new StringBuffer("SELECT ");
        searchSql.append(columnSql).append("FROM ");
        searchSql.append(nextCompanySql).append("LEFT JOIN ");
        searchSql.append(reportInfoSql).append(onSql);
        return searchSql.toString();
    }

    /**
     * @param theMonth    统计年月
     * @param company     所属单位
     * @param codeType    报表编码类型
     * @param projectSide 项目所属侧
     * @return 封装的sql
     * @description 获取该月该单位未统计和未上报的报表编码
     */
    public String getNoStatistAndSubmit(String theMonth, String company, String codeType, String projectSide) {
        //封装查询字段sql
        String columnSql = "CODE_VALUE";
        //封装查询报表编码sql
        String reportCodeSql = "(SELECT * FROM S_CODE_INFO WHERE CODE_TYPE = " + codeType + ") s ";
        //封装按条件查询报表信息sql
        String reportInfoSql = "(SELECT * FROM R_REPORT_INFO WHERE COMPANY = '" + company + "' AND TO_CHAR(THEMONTH, 'yyyy-MM') = '" + theMonth +
                "' AND PROJECT_SIDE = '" + projectSide + "') r ";
        //封装on条件sql
        String onSql = "ON s.CODE_VALUE = r.REPORT_CODE WHERE SUBMIT_STATE IS NULL ORDER BY s.CODE_ORDER ";
        StringBuffer searchSql = new StringBuffer("SELECT " + columnSql + " FROM ");
        searchSql.append(reportCodeSql).append(" LEFT JOIN ");
        searchSql.append(reportInfoSql).append(onSql);
        return searchSql.toString();
    }

    /**
     * @param theMonth      统计年月
     * @param company       所属单位
     * @param codeValues    报表编码
     * @param projectSide   项目所属侧
     * @param returnOpinion 回退意见
     * @param userInfo      用户信息
     * @return 封装的sql
     * @description 回退报表（用于回退功能）
     */
    public String reportReturn(String theMonth, String company, String codeValues, String projectSide, String returnOpinion, S_USER_INFO userInfo) {

        String setSql = "r.STATISTICS_STATE = '0',r.SUBMIT_STATE = '2',r.STATISTICS_OPERATOR = '',r.STATISTICS_DATE = '',r.SUBMIT_OPERATOR = '',r.SUBMIT_DATE = '',r.RETURN_OPINION = '" + returnOpinion + "' ";
        String whereSql = "r.COMPANY = '" + company + "' AND to_char(r.THEMONTH,'yyyy-mm') = '" + theMonth + "' AND r.REPORT_CODE in (" + codeValues + ")";
        StringBuffer updateSql = new StringBuffer("UPDATE R_REPORT_INFO r SET ");
        updateSql.append(setSql).append("WHERE ");
        updateSql.append(whereSql);
        return updateSql.toString();
    }

    /**
     * @param theMonth     统计年月
     * @param company      所属单位
     * @param codeValue    报表编码
     * @param projectSide  项目所属侧
     * @param isStatistics 是否统计（true:已统计 false:未统计）
     * @return 封装的sql
     * @description 获取所有未上报的下属单位（用于批量上报功能）
     */
    public String getNoSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide, boolean isStatistics) {

        String statisticsStateValue = isStatistics ? "1" : "0";
//        String columnSql = "s.ORG_CODE,DECODE(r1.STATISTICS_STATE, NULL, 0, r1.STATISTICS_STATE) AS STATISTICS_STATE," +
//                "DECODE(r1.SUBMIT_STATE, NULL, 0, r1.SUBMIT_STATE) AS SUBMIT_STATE ";
        String columnSql = "ORG_CODE, STATISTICS_STATE ";
        String sSql = "(SELECT * FROM S_ORG_CODE_INFO t START WITH t.PARENT_ORG_CODE = '" + company + "' CONNECT BY PRIOR t.ORG_CODE = t.PARENT_ORG_CODE) s ";
        String r1Sql = "(SELECT * FROM R_REPORT_INFO r WHERE TO_CHAR(r.THEMONTH, 'yyyy-mm') = '" + theMonth +
                "' AND R.PROJECT_SIDE = '" + projectSide + "' AND R.REPORT_CODE = '" + codeValue + "' and R.SUBMIT_STATE != '1') r1 ";
        String onSql = "ON s.ORG_CODE = r1.COMPANY) ";
        String whereSql = "where statistics_state = '" + statisticsStateValue + "'";
//        StringBuffer searchSql = new StringBuffer("SELECT ORG_CODE AS COMPANY  FROM (");
        StringBuffer searchSql = new StringBuffer("SELECT ORG_CODE AS COMPANY  FROM (");
        searchSql.append("SELECT ").append(columnSql);
        searchSql.append("FROM ").append(sSql);
        searchSql.append("LEFT JOIN ").append(r1Sql);
        searchSql.append(onSql).append(whereSql);
        return searchSql.toString();
    }

    /**
     * @param theMonth     统计年月
     * @param company      所属单位
     * @param codeValue    报表编码
     * @param projectSide  项目所属侧
     * @return 封装的sql
     * @description 获取所有已上报的下属单位（用于批量上报功能）
     */
    public String getSubmitAllNextCompany(String theMonth, String company, String codeValue, String projectSide) {

        String columnSql = "s.ORG_CODE,r1.REPORT_ID,DECODE(r1.STATISTICS_STATE, NULL, 0, r1.STATISTICS_STATE) AS STATISTICS_STATE," +
                "DECODE(r1.SUBMIT_STATE, NULL, 0, r1.SUBMIT_STATE) AS SUBMIT_STATE ";
        String sSql = "(SELECT * FROM S_ORG_CODE_INFO t START WITH t.PARENT_ORG_CODE = '" + company + "' CONNECT BY PRIOR t.ORG_CODE = t.PARENT_ORG_CODE) s ";
        String r1Sql = "(SELECT * FROM R_REPORT_INFO r WHERE TO_CHAR(r.THEMONTH, 'yyyy-mm') = '" + theMonth +
                "' AND R.PROJECT_SIDE = '" + projectSide + "' AND R.REPORT_CODE = '" + codeValue + "' and R.SUBMIT_STATE = '1') r1 ";
        String onSql = "ON s.ORG_CODE = r1.COMPANY) ";
        StringBuffer searchSql = new StringBuffer("SELECT DECODE(REPORT_ID, NULL, '', ORG_CODE) AS COMPANY  FROM (");
        searchSql.append("SELECT ").append(columnSql);
        searchSql.append("FROM ").append(sSql);
        searchSql.append("LEFT JOIN ").append(r1Sql);
        searchSql.append(onSql);
        return searchSql.toString();
    }

    /**
     * @param theMonth     统计年月
     * @param companys     所属单位字符串
     * @param tableName   表名
     * @param isLock   是否锁定（true:锁定  false：解锁）
     * @return 封装的sql
     * @description 锁定数据（用于上报功能）
     */
    public String updateDataState(String theMonth, String companys, String tableName, String codeValues, String projectSide,boolean isLock){

        String inValueSql = "SELECT REGEXP_SUBSTR(#{companys}, '[^,]+', 1, LEVEL, 'i') AS COMPANY FROM DUAL " +
                "CONNECT BY level <= LENGTH(#{companys}) - LENGTH(REGEXP_REPLACE(#{companys}, ',', '')) + 1";
        String dateState = isLock ? "'1'" : "'0'";
        String table = ClientSideProvider.CLIENT_SIDE.equals(projectSide) ? tableName : "B_SELF_SAVE_BASE";
        StringBuffer updateSql = new StringBuffer("update "+ table +" t ");
        String columnSql = "t.data_state = "+ dateState +" ";
        String whereSql = "t.company in (" + inValueSql +") and TO_CHAR(t.THEMONTH, 'yyyy-mm') = #{theMonth}";

        updateSql.append("set ").append(columnSql);
        updateSql.append("where  ").append(whereSql);
        if(ClientSideProvider.SELF_SIDE.equals(projectSide)){
            String inCodeValueSql = "SELECT REGEXP_SUBSTR(#{codeValues}, '[^,]+', 1, LEVEL, 'i') AS COMPANY FROM DUAL " +
                    "CONNECT BY level <= LENGTH(#{codeValues}) - LENGTH(REGEXP_REPLACE(#{codeValues}, ',', '')) + 1";
            updateSql.append("AND t.PROJECT_CATEGORY in ("+ inCodeValueSql +")");
        }
        return updateSql.toString();
    }
}