package cn.csg.ucs.bi.business.mapper.require.provider;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomSQLHelperUtils;
import cn.csg.core.common.mapper.helper.CustomUpdateSQLHelper;
import cn.csg.core.common.mapper.provider.BaseProvider;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SProjectMgtMapperProvider extends BaseProvider {

    public String getSProjectInfo(JSONObject jo) {
        TableMeta tableMeta = TableMeta.forClass(SelfSaveBase.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(", (SELECT " + tableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME);
        sql.append(".TABLE_NAME FROM S_PROJECT_CATEGORY_CLASS_REL " + tableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME);
        sql.append(" WHERE " + tableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME + ".PROJECT_SIDE = '0' ");
        sql.append("AND " + tableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME + ".PROJECT_CATEGORY = ");
        sql.append(tableMeta.getTableAliasName() + ".PROJECT_CATEGORY) AS TABLE_NAME");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
//        sql.append(" AND " + tableMeta.getTableAliasName() + ".PROJECT_CATEGORY IN ('1', '2', '3', '4', '5', '10', '13')");
        sql.append(CustomConditionsSQLHelper.getOrderSQL(SelfSaveBase.class, tableMeta));
        return sql.toString();
    }

    public String getSubInfos(@Param("projectId") String projectId, @Param("tableName") String tableName) throws ClassNotFoundException {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE PROJECT_ID = '" + projectId + "'");
        return sql.toString();
    }
    public String updateSubInfos(ProviderContext context, JSONObject jo, String className) throws ClassNotFoundException {
        Class<?> entityClass = Class.forName(className);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return CustomUpdateSQLHelper.getUpdateSQLByColumn(jo, tableMeta);
    }

    public String delSProjectInfo(String tableName, String keys) {
        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);
        sql.append(" t WHERE t.PROJECT_ID IN (" + keys + ")");
        return sql.toString();
    }

    public String delSubInfos(String tableName, String keys) {
        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);
        sql.append(" t WHERE t.GUID IN (" + keys + ")");
        return sql.toString();
    }

    public String repeatedVerificate(List<JSONObject> list,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        StringBuffer sql = new StringBuffer();
        Map<String,String> map = tableMeta.getValidateExistFieldsColumnMap();
        Collection<String> keys = map.keySet();
        String dual = selectListFromDual(list,tableMeta);
        Iterator<String> iterator = keys.iterator();
        StringBuffer table = new StringBuffer("select ");
        StringBuffer columns = new StringBuffer();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (tableMeta.getAllFieldsMap().get(key).isAnnotationPresent(DateField.class)) {
                String dateFormat = tableMeta.getAllFieldsMap().get(key).getAnnotation(DateField.class).FORMAT();
                table.append("to_char(").append(tableMeta.getTableAliasName());
                table.append(".").append(value).append(", '").append(dateFormat);
                table.append("') AS ").append(value);
            } else {
                table.append(tableMeta.getTableAliasName()).append(".").append(value);
            }
            columns.append(value);
            if (iterator.hasNext()) {
                table.append(", ");
                columns.append(", ");
            }
        }
        table.append(" from ").append(tableMeta.getTableName()).append(" where to_char(THEMONTH,'").
                append(tableMeta.getAllFieldsMap().get("theMonth").getAnnotation(DateField.class).FORMAT()).append("') = '").
                append(list.get(0).getString("theMonth")).append("' and PROJECT_CATEGORY='").append(list.get(0).getString("projectCategory")).append("'");
        sql.append("select t1.* from (select ").append(columns).append(",count(1) countNum from (").append(table).append(" union all ").
                append(dual).append(") group by ").append(columns).append(") t1 where t1.countNum>1 ");
        return sql.toString();
    }

    private String selectListFromDual(List<JSONObject> list,TableMeta tableMeta){
        StringBuffer dual = new StringBuffer("");
        Map<String,String> map = tableMeta.getValidateExistFieldsColumnMap();
        Collection<String> keys = map.keySet();
        if (!CollectionUtils.isEmpty(keys)) {
            for (int i=0;i<list.size();i++) {
                StringBuffer sb = new StringBuffer("select ");
                Iterator<String> iterator = keys.iterator();
                JSONObject item = list.get(i);
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = map.get(key);
                    sb.append("'").append(item.getString(key)).append("' as ").append(value);
                    if (iterator.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append(" from dual");
                if(i != list.size()-1){
                    sb.append(" union all ");
                }
                dual.append(sb);
            }
        }
        return dual.toString();
    }
}
