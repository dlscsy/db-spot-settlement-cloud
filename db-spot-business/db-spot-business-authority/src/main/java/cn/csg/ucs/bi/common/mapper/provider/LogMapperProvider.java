package cn.csg.ucs.bi.common.mapper.provider;

import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomInsertSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.business.entity.B_LED;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import cn.csg.ucs.bi.common.entity.S_LOGIN_LOG;
import com.alibaba.fastjson.JSONObject;

public class LogMapperProvider {

    public String addLog(S_LOGIN_LOG obj) {

        TableMeta tableMeta = TableMeta.forClass(S_LOGIN_LOG.class);
        StringBuffer sql = new StringBuffer();
        sql.append(CustomInsertSQLHelper.getInsertSQL(JSONObject.parseObject(JSONObject.toJSONString(obj)),tableMeta));

        return sql.toString();
    }

    public String getLoggerByPage(JSONObject json) {
        TableMeta tableMeta = TableMeta.forClass(S_LOGIN_LOG.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(json, tableMeta));
        sql.append(CustomConditionsSQLHelper.getOrderSQL(S_LOGIN_LOG.class,tableMeta));
        return sql.toString();

    }

}
