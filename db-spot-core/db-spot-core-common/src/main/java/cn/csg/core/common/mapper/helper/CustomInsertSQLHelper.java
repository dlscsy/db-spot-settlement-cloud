package cn.csg.core.common.mapper.helper;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.NumberField;
import cn.csg.core.common.annotation.ValueSetting;
import cn.csg.core.common.constant.BaseBusinessMapperExceptionCode;
import cn.csg.core.common.exception.BaseBusinessMapperException;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.core.common.utils.CommonUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.math.NumberUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class CustomInsertSQLHelper {

    /**
     * 获取字段和值的映射MAP集合
     *
     * @param jo
     * @param tableMeta
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getColumnValueMap(JSONObject jo, TableMeta tableMeta) {
        Map<String, Object> result = new HashMap<>();
        if (!jo.isEmpty()) {
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String property = entry.getKey(); // 获取条件属性字段名称，这个字段与实体类中的属性字段名称是一致的
                Object value = entry.getValue(); // 获取条件属性值
                if (tableMeta.getAllFieldsMap().containsKey(property)) {
                    Field currentField = tableMeta.getAllFieldsMap().get(property);
                    String columnName = tableMeta.getAllFieldsColumnMap().get(property);

                    if (currentField.isAnnotationPresent(ValueSetting.class) &&
                            currentField.getAnnotation(ValueSetting.class).USECHECKBOX()) {
                        // 复选类型
                        String split = currentField.getAnnotation(ValueSetting.class).SPLIT();
                        String checkBoxValues = CustomSQLHelperUtils.getCheckBoxValues(split, value);
                        result.put(columnName, checkBoxValues);
                        continue;
                    }

                    if (currentField.isAnnotationPresent(NumberField.class)
                            && !NumberUtils.isDigits(String.valueOf(value))
                            && NumberUtils.isNumber(String.valueOf(value))) {
                        // 浮点数类型
                        int scale = currentField.getAnnotation(NumberField.class).SCALE();
                        BigDecimal bd = new BigDecimal(String.valueOf(value));
                        double numberValue = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
                        result.put(columnName, numberValue);
                        continue;
                    }

                    result.put(columnName, value);
                }
            }

            if (result.size() == 0) {
                throw new BaseBusinessMapperException(
                        BaseBusinessMapperExceptionCode.OBJECT_FOR_INSERT_PROPERTY_MATCH_EXCEPTION,
                        "要新增的数据的所有属性与实体类属性不一致！"
                );
            }
        } else {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.OBJECT_FOR_INSERT_EMPTY_EXCEPTION,
                    "要新增的数据为空！"
            );
        }

        return result;
    }

    /**
     * 获取单条插入的SQL语句段
     *
     * @param jo
     * @param tableMeta
     * @return
     */
    public static String getInsertSQL(JSONObject jo, TableMeta tableMeta) {
        StringBuffer columnsSQL = new StringBuffer("");
        StringBuffer valuesSQL = new StringBuffer("");

        Map<String, Object> result = getColumnValueMap(jo, tableMeta);
        Collection<String> properties = tableMeta.getAllFieldsColumnMap().keySet();
        Iterator<String> iterator = properties.iterator();
        while (iterator.hasNext()) {
            String property = iterator.next();
            String columnName = tableMeta.getAllFieldsColumnMap().get(property);
            Field currentField = tableMeta.getAllFieldsMap().get(property);
            columnsSQL.append(columnName);

            if (result.containsKey(columnName)) {
                // 如果有具体值
                String value = String.valueOf(result.get(columnName));
                if (currentField.isAnnotationPresent(DateField.class)) {
                    // 是日期类型的字段
                    String dateFormat = currentField.getAnnotation(DateField.class).FORMAT();
                    valuesSQL.append("to_date('" + value + "', '" + dateFormat + "')");
                } else {
                    // 其他类型的字段
                    valuesSQL.append("'" + value + "'");
                }
            } else {
                // 如果是空值
                if (property.equals(tableMeta.getPkProperty())) {
                    // 如果是主键
                    valuesSQL.append("'" + CommonUtils.createUUID() + "'");
                } else {
                    String defaultValue = currentField.isAnnotationPresent(ValueSetting.class)
                            ? currentField.getAnnotation(ValueSetting.class).DEFAULT_VALUE() : "null";
                    valuesSQL.append(defaultValue);
                }
            }

            if (iterator.hasNext()) {
                columnsSQL.append(", ");
                valuesSQL.append(", ");
            }
        }

        StringBuffer insertSQL = new StringBuffer("INSERT INTO " + tableMeta.getTableName() + " ");
        insertSQL.append("(").append(columnsSQL).append(")").append(" VALUES ").append("(").append(valuesSQL).append(")");
        return insertSQL.toString();
    }
}
