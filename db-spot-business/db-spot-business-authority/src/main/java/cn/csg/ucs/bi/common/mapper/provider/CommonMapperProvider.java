package cn.csg.ucs.bi.common.mapper.provider;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.mapper.helper.CustomInsertSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CommonMapperProvider {

    public String getModelNum(String tableName, JSONObject json) {
        StringBuffer sql = new StringBuffer("SELECT DISTINCT t.MODEL_NUM FROM " + tableName + " t WHERE 1 = 1");
        if (!json.isEmpty()) {
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                String property = entry.getKey();
                Object value = entry.getValue();
                sql.append(" AND t." + property + " = '" + value + "'");
            }
        }
        return sql.toString();
    }

    public String batchInsertCategoryInfo(String className, List<JSONObject> datas, boolean flag) throws ClassNotFoundException {

        TableMeta tableMeta = TableMeta.forClass(Class.forName(className));
        StringBuffer insterSql = new StringBuffer("INSERT ALL ");
        if (!datas.isEmpty()) {
            //封装批量插入sql
            for (JSONObject data : datas) {
                //拼接类名和值 例："(字段1，字段2) VALUES (值1，值2)"
                String columsAndValues = getColumnsValuesSQL(data, tableMeta, flag);
                insterSql.append("INTO ");
                insterSql.append(tableMeta.getTableName()).append(" ");
                insterSql.append(columsAndValues);
            }
        }
        insterSql.append("SELECT 1 FROM DUAL ");
        return insterSql.toString();
    }

    private static String getColumnsValuesSQL(JSONObject data, TableMeta tableMeta, boolean flag) {

        //定义最后字段名和字段值的拼接结果
        String columnsValues = "";
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        if (flag) {
            // 获取属性名和列名的映射关系
            Map<String, Object> propertyTOColumn = CustomInsertSQLHelper.getColumnValueMap(data,tableMeta);
            // 获取列名和属性名的映射关系
            Map<String, String> columnToProperty = tableMeta.getAllColumnsFieldMap();
            for (Map.Entry<String, Object> entry : propertyTOColumn.entrySet()) {
                // 获取列名
                String column = entry.getKey();
                // 如果该列名不存在则继续循环
                if(StringUtils.isBlank(column)) continue;
                // 获取字段名
                String property = columnToProperty.get(column);
                // 获取字段值
                Object value = entry.getValue();
                // 暂存值
                StringBuffer subValueStr = new StringBuffer("");
                // 拼接列名
                columns.append(column).append(",");
                // 获取属性对象
                Field currentField = tableMeta.getAllFieldsMap().get(property);
                if (currentField.isAnnotationPresent(DateField.class)) {
                    // 是日期类型的字段
                    String dateFormat = currentField.getAnnotation(DateField.class).FORMAT();
                    subValueStr.append("to_date('" + value + "', '" + dateFormat + "')");
                } else {
                    // 其他类型的字段
                    subValueStr.append("'" + value + "'");
                }
                // 拼接字段值
                values.append(subValueStr).append(",");
            }
            // 删除拼接列名的最后一个逗号
            columns.deleteCharAt(columns.length() - 1);
            // 删除拼接字段值的最后一个逗号
            values.deleteCharAt(values.length() - 1);
            columns.append(") ");
            values.append(") ");
            columnsValues = columns.append(" VALUES ").append(values).toString();

        } else {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                // 获取列名
                String column = entry.getKey();
                // 获取字段值
                Object value = entry.getValue();
                StringBuffer subValueStr = new StringBuffer("'" + value + "'");
                // 拼接列名
                columns.append(column).append(",");
                // 拼接字段值
                values.append(subValueStr).append(",");
            }
            // 删除拼接列名的最后一个逗号
            columns.deleteCharAt(columns.length() - 1);
            // 删除拼接字段值的最后一个逗号
            values.deleteCharAt(values.length() - 1);
            columns.append(") ");
            values.append(") ");
            columnsValues = columns.append(" VALUES ").append(values).toString();
        }
        return columnsValues;
    }
}
