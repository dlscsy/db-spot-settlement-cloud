package cn.csg.ucs.bi.business.mapper.calculate.provider;

import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.business.entity.S_SMARTBI_REPORT_REL;
import cn.csg.ucs.bi.business.entity.R_REPORT_INFO;
import cn.csg.ucs.bi.common.entity.S_CODE_INFO;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: 计量侧报表汇总provider
 * @author: G.A.N
 * @create: 2019-12-02
 */
public class CalculateSideProvider {

    /**
     * @description: 根据编码类别和报表编码获取统计配置类
     * @param jo    查询参数封装json对象
     * @return 封装的sql
     */
    public String getReportSummary(JSONObject jo){
        TableMeta reportInfoTableMeta = TableMeta.forClass(R_REPORT_INFO.class);
        TableMeta codeInfoTableMeta = TableMeta.forClass(S_CODE_INFO.class);
        TableMeta smartbiReportRel = TableMeta.forClass(S_SMARTBI_REPORT_REL.class);
        String columnSql = "DECODE(r.REPORT_ID, null, sys_guid(), r.REPORT_ID) AS REPORT_ID, s.CODE_NAME, s.CODE_VALUE, '" +
                jo.getString("theMonth") + "' AS THEMONTH, r.STATISTICS_DATE, r.SUBMIT_OPERATOR, r.SUBMIT_DATE, r.RETURN_OPINION, " +
                "DECODE(r.STATISTICS_STATE,null,0,r.STATISTICS_STATE) STATISTICS_STATE,rel.SMART_ID";
        StringBuffer sql = new StringBuffer("SELECT "+ columnSql +" FROM ");
        sql.append(" (SELECT * from " + codeInfoTableMeta.getTableName()+ " " + codeInfoTableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME);
        sql.append(" WHERE " + codeInfoTableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME + ".CODE_TYPE = '22') s ");
        sql.append("LEFT JOIN ");
        sql.append("(SELECT * FROM " + reportInfoTableMeta.getTableName());
        sql.append(" WHERE TO_CHAR(THEMONTH,'yyyymm') = '"+jo.getString("theMonth")+"' AND COMPANY = '"+jo.getString("company")+"' AND PROJECT_SIDE NOT IN (1,2)) r ");
        sql.append("ON s.CODE_VALUE = r.REPORT_CODE ");
        sql.append("LEFT JOIN ");
        sql.append("(SELECT * FROM "+ smartbiReportRel.getTableName()+") rel ");
        sql.append("ON s.CODE_TYPE = rel.REPORT_TYPE AND s.CODE_VALUE = rel.REPORT_CODE");
        sql.append(CustomConditionsSQLHelper.getOrderSQL(S_CODE_INFO.class, codeInfoTableMeta));
        return sql.toString();
    }
}