package cn.csg.core.common.mapper.helper;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.DictionarieField;
import cn.csg.core.common.structure.TableMeta;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CustomColumnsSQLHelper {

    /**
     * 获取基础列的SQL语句段，这些列是不需要特殊处理的（例如数据字典映射）
     *
     * @param tableMeta
     * @return
     */
    public static String getBaseColumnsSQL(TableMeta tableMeta) {
        StringBuffer baseColumns = new StringBuffer("");
        Collection<String> keys = tableMeta.getAllFieldsColumnMap().keySet();
        if (!CollectionUtils.isEmpty(keys)) {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = tableMeta.getAllFieldsColumnMap().get(key);
                if (tableMeta.getAllFieldsMap().get(key).isAnnotationPresent(DateField.class)) {
                    String dateFormat = tableMeta.getAllFieldsMap().get(key).getAnnotation(DateField.class).FORMAT();
                    baseColumns.append("to_char(").append(tableMeta.getTableAliasName());
                    baseColumns.append(".").append(value).append(", '").append(dateFormat);
                    baseColumns.append("') AS ").append(value);
                } else {
                    baseColumns.append(tableMeta.getTableAliasName()).append(".").append(value);
                }
                if (iterator.hasNext()) {
                    baseColumns.append(", ");
                }
            }
        }

        return baseColumns.toString();
    }

    /**
     * 获取需要做数据字典映射的列的SQL语句段
     *
     * @param tableMeta
     * @return
     */
    public static String getDictionarieColumnsSQL(TableMeta tableMeta) {
        StringBuffer dColumns = new StringBuffer("");
        Map<String, DictionarieField> dcmap = tableMeta.getDictionarieFieldsColumnMap();
        Map<String, String> columns = tableMeta.getAllFieldsColumnMap();
        if (!dcmap.isEmpty()) {
            for (Map.Entry<String, DictionarieField> entry : dcmap.entrySet()) {
                String property = entry.getKey();
                DictionarieField df = entry.getValue();
                String columnName = columns.get(property);
                if (!df.SQL().equals("")) {
                    dColumns.append(", " + df.SQL());
                    continue;
                }

                if (!df.TABLE_NAME().equals("") && !df.RELATIONFIELD().equals("") && !df.MAPPINGFIELD().equals("")) {
                    String relationTableAliasName = df.TABLE_ALIAS_NAME();
                    dColumns.append(", " + "(SELECT " + relationTableAliasName + "." + df.MAPPINGFIELD());
                    dColumns.append(" FROM " + df.TABLE_NAME() + " " + relationTableAliasName);
                    dColumns.append(" WHERE " + relationTableAliasName + "." + df.RELATIONFIELD() + " = ");
                    dColumns.append(tableMeta.getTableAliasName() + "." + columnName);
                    dColumns.append(!"".equals(df.ADDITIONAL()) ? " AND " + df.ADDITIONAL() : "");
                    dColumns.append(") AS " + columnName + df.RELATION_COLUMN_ALIAS_NAME_SUFFIX());
                }
            }
        }

        return dColumns.toString();
    }
}
