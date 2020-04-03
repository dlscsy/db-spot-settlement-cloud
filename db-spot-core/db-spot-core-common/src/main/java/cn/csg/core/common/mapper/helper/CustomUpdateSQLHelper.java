package cn.csg.core.common.mapper.helper;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.NumberField;
import cn.csg.core.common.annotation.ValueSetting;
import cn.csg.core.common.constant.BaseBusinessMapperExceptionCode;
import cn.csg.core.common.exception.BaseBusinessMapperException;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class CustomUpdateSQLHelper {

    public static String getUpdateSQL(JSONObject jo, TableMeta tableMeta) {
        String tableAliasName = tableMeta.getTableAliasName();
        StringBuffer updateSQL = new StringBuffer("");
        String primaryKeyValue = jo.getString("primaryKeyValue");
        jo.remove("primaryKeyValue");
        if (!jo.isEmpty()) {
            updateSQL.append("UPDATE " + tableMeta.getTableName() + " " + tableAliasName + " SET");
            StringBuffer setSQL = new StringBuffer("");
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
                        setSQL.append(" " + tableAliasName + "." + columnName + " = '" + checkBoxValues + "',");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(NumberField.class)
                            && !NumberUtils.isDigits(String.valueOf(value))
                            && NumberUtils.isNumber(String.valueOf(value))) {
                        int scale = currentField.getAnnotation(NumberField.class).SCALE();
                        BigDecimal bd = new BigDecimal(String.valueOf(value));
                        double numberValue = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
                        setSQL.append(" " + tableAliasName + "." + columnName + " = '" + numberValue + "',");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(DateField.class)) {
                        // 如果是日期条件
                        String dateFormat = currentField.getAnnotation(DateField.class).FORMAT();
                        setSQL.append(" " + tableAliasName + "." + columnName);
                        setSQL.append(" = to_date('" + value + "', '" + dateFormat + "'), ");
                        continue;
                    }

                    setSQL.append(" " + tableAliasName + "." + columnName + " = '" + value + "',");
                }
            }

            if (StringUtils.isEmpty(primaryKeyValue)) {
                throw new BaseBusinessMapperException(
                        BaseBusinessMapperExceptionCode.PK_VALUE_FOR_UPDATE_EMPTY_EXCEPTION,
                        "更新数据时主键的值不能为空！"
                );
            }

            if (StringUtils.isEmpty(setSQL.toString())) {
                throw new BaseBusinessMapperException(
                        BaseBusinessMapperExceptionCode.OBJECT_FOR_UPDATE_PROPERTY_MATCH_EXCEPTION,
                        "要更新的数据的所有属性与实体类属性不一致！"
                );
            }

            updateSQL.append(setSQL.deleteCharAt(setSQL.lastIndexOf(",")));
            updateSQL.append(" WHERE " + tableAliasName + "." + tableMeta.getPkColumn() + " = '" + primaryKeyValue + "'");
        } else {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.OBJECT_FOR_UPDATE_EMPTY_EXCEPTION,
                    "要更新的数据为空！"
            );
        }

        return updateSQL.toString();
    }

    public static String getUpdateSQLByColumn(JSONObject jo, TableMeta tableMeta) {
        String tableAliasName = tableMeta.getTableAliasName();
        StringBuffer updateSQL = new StringBuffer("");
        String primaryKeyValue = jo.getString("primaryKeyValue");
        jo.remove("primaryKeyValue");
        if (!jo.isEmpty()) {
            updateSQL.append("UPDATE " + tableMeta.getTableName() + " " + tableAliasName + " SET");
            StringBuffer setSQL = new StringBuffer("");
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String columnName = entry.getKey(); // 获取条件属性字段名称，这个字段与实体类中的属性字段名称是一致的
                String property = tableMeta.getAllColumnsFieldMap().get(columnName);
                Object value = entry.getValue(); // 获取条件属性值
                if (tableMeta.getAllFieldsMap().containsKey(property)) {
                    Field currentField = tableMeta.getAllFieldsMap().get(property);

                    if (currentField.isAnnotationPresent(ValueSetting.class) &&
                            currentField.getAnnotation(ValueSetting.class).USECHECKBOX()) {
                        // 复选类型
                        String split = currentField.getAnnotation(ValueSetting.class).SPLIT();
                        String checkBoxValues = CustomSQLHelperUtils.getCheckBoxValues(split, value);
                        setSQL.append(" " + tableAliasName + "." + columnName + " = '" + checkBoxValues + "',");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(NumberField.class)
                            && !NumberUtils.isDigits(String.valueOf(value))
                            && NumberUtils.isNumber(String.valueOf(value))) {
                        int scale = currentField.getAnnotation(NumberField.class).SCALE();
                        BigDecimal bd = new BigDecimal(String.valueOf(value));
                        double numberValue = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
                        setSQL.append(" " + tableAliasName + "." + columnName + " = '" + numberValue + "',");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(DateField.class)) {
                        // 如果是日期条件
                        String dateFormat = currentField.getAnnotation(DateField.class).FORMAT();
                        setSQL.append(" " + tableAliasName + "." + columnName);
                        setSQL.append(" = to_date('" + value + "', '" + dateFormat + "'), ");
                        continue;
                    }

                    setSQL.append(" " + tableAliasName + "." + columnName + " = '" + value + "',");
                }
            }

            if (StringUtils.isEmpty(primaryKeyValue)) {
                throw new BaseBusinessMapperException(
                        BaseBusinessMapperExceptionCode.PK_VALUE_FOR_UPDATE_EMPTY_EXCEPTION,
                        "更新数据时主键的值不能为空！"
                );
            }

            if (StringUtils.isEmpty(setSQL.toString())) {
                throw new BaseBusinessMapperException(
                        BaseBusinessMapperExceptionCode.OBJECT_FOR_UPDATE_PROPERTY_MATCH_EXCEPTION,
                        "要更新的数据的所有属性与实体类属性不一致！"
                );
            }

            updateSQL.append(setSQL.deleteCharAt(setSQL.lastIndexOf(",")));
            updateSQL.append(" WHERE " + tableAliasName + "." + tableMeta.getPkColumn() + " = '" + primaryKeyValue + "'");
        } else {
            throw new BaseBusinessMapperException(
                    BaseBusinessMapperExceptionCode.OBJECT_FOR_UPDATE_EMPTY_EXCEPTION,
                    "要更新的数据为空！"
            );
        }

        return updateSQL.toString();
    }
}
