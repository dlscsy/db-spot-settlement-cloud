package cn.csg.ucs.bi.business.mapper.require.provider;

import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.business.entity.S_PROJECT_OPTION_CONFIG;
import cn.csg.ucs.bi.common.entity.S_STATISTICS_CONFIG;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 客户侧报表汇总provider
 * @author: G.A.N
 * @create: 2019-12-19
 */
public class CProjectInfoSummaryProvider {

    public String getReportInfoSummary(String company, String theMonth, S_PROJECT_OPTION_CONFIG projectOptionConfig){


        String projectCode = StringUtils.isNotBlank(projectOptionConfig.getProjectCode())?"AND p.PROJECT_CODE = '"+ projectOptionConfig.getProjectCode():"";
        StringBuffer searchSql = new StringBuffer("SELECT s1.*, s2.* FROM ");
        StringBuffer s1Sql = new StringBuffer("(SELECT s.ORG_CODE, s.ORG_NAME, s.ORG_ORDER FROM S_ORG_CODE_INFO s " +
                "WHERE s.PARENT_ORG_CODE = '"+ company +"' OR s.ORG_CODE = (CASE WHEN EXISTS " +
                "(SELECT * FROM S_ORG_CODE_INFO s WHERE s.PARENT_ORG_CODE = '"+ company +"') then '' ELSE '"+ company +"' END)) s1");
        StringBuffer s2Sql = new StringBuffer("(SELECT p.MONTH_VALUE, p.TOTAL_VALUE, p.COMPANY FROM P_SELF_CLIENT_SUMMARY p " +
                " WHERE TO_CHAR(p.THEMONTH, 'yyyy-mm') = '" + theMonth +"'"+ projectCode +"' and p.STATISTICS_CATEGORY = '"+ projectOptionConfig.getStatisticsCategory() +"') s2 ");
        StringBuffer s3Sql = new StringBuffer("");
        if("7,8,9".indexOf(projectOptionConfig.getProjectType()) >= 0){
            s3Sql.append(" (SELECT sum(p.MONTH_VALUE) AS MONTH_VALUE, SUM(p.TOTAL_VALUE) as TOTAL_VALUE, p.COMPANY FROM P_CONTRACT_SUMMARY p " +
                    "WHERE  TO_CHAR(p.THEMONTH, 'yyyy-mm') = '" + theMonth + "' AND p.STATISTICS_CATEGORY = '"+ projectOptionConfig.getStatisticsCategory() +"'");
            if(StringUtils.isNotBlank(projectOptionConfig.getProjectCode())){
                s3Sql.append(" and p.PROJECT_CODE = '"+ projectOptionConfig.getProjectCode() +"'");
            }
            if(StringUtils.isNotBlank(projectOptionConfig.getCategory())){
                String[] categoryArr = projectOptionConfig.getCategory().split(",");
                s3Sql.append(" AND p.CATEGORY IN ("+ String.join(",", Arrays.asList(categoryArr)) +")");
            }
            if(StringUtils.isNotBlank(projectOptionConfig.getCaliberType())){
                s3Sql.append(" AND p.CALIBER_TYPE = '"+ projectOptionConfig.getCaliberType() +"'");
            }
            s3Sql.append(" GROUP BY COMPANY");
            s3Sql.append(") s2 ");
        }
        StringBuffer onSql = new StringBuffer(" ON s1.ORG_CODE = s2.COMPANY ORDER BY s1.ORG_ORDER");
        searchSql.append(s1Sql).append(" LEFT JOIN ");
        if("7,8,9".indexOf(projectOptionConfig.getProjectType()) >= 0){
            searchSql.append(s3Sql).append(onSql);
        }else{
            searchSql.append(s2Sql).append(onSql);
        }
        return searchSql.toString();
    }

    public String getCategoryDropDown(String projectCategory){
        StringBuffer sql = new StringBuffer("SELECT ");
        TableMeta tableMeta = TableMeta.forClass(S_PROJECT_OPTION_CONFIG.class);
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        JSONObject jo = new JSONObject();
        jo.put("projectType",projectCategory);
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
        return sql.toString();
    }
}