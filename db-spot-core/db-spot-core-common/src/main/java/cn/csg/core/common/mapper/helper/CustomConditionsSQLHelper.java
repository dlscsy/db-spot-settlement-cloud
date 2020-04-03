package cn.csg.core.common.mapper.helper;

import cn.csg.core.common.annotation.*;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CustomConditionsSQLHelper {

    /**
     * 获取排序的SQL语句段
     *
     * @param entityClass
     * @param tableMeta
     * @return
     */
    public static String getOrderSQL(Class<?> entityClass, TableMeta tableMeta) {
        StringBuffer order = new StringBuffer("");
        OrderConfig oc = entityClass.getAnnotation(OrderConfig.class);
        if (oc != null && !"".equals(oc.FIELDS())) {
            String[] fields = oc.FIELDS().split(",");
            order.append(" ORDER BY ");
            for (int i = 0; i < fields.length; i++) {
                order.append(tableMeta.getTableAliasName() + "." + fields[i]);
                if (i < fields.length - 1) {
                    order.append(", ");
                }
            }
            order.append(" " + oc.ORDERTYPE());
        }
        return order.toString();
    }

    /**
     * 获取条件的SQL语句段
     *
     * @param jo
     * @param tableMeta
     * @return
     */
    public static String getConditionsSQL(JSONObject jo, TableMeta tableMeta) {
        String tableAliasName = tableMeta.getTableAliasName();
        StringBuffer conditions = new StringBuffer(" WHERE 1 = 1");
        if (!jo.isEmpty()) {
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String property = entry.getKey(); // 获取条件属性字段名称，这个字段与实体类中的属性字段名称是一致的
                Object value = entry.getValue(); // 获取条件属性值
                if (tableMeta.getAllFieldsMap().containsKey(property)) {
                    Field currentField = tableMeta.getAllFieldsMap().get(property);
                    String columnName = tableMeta.getAllFieldsColumnMap().get(property);
                    if (property.equals(tableMeta.getPkProperty()) &&
                            !currentField.isAnnotationPresent(NeedIn.class) &&
                            !currentField.isAnnotationPresent(NeedLike.class) &&
                            !currentField.isAnnotationPresent(DateField.class) &&
                            !currentField.isAnnotationPresent(NumberField.class)
                    ) {
                        // 如果是主键作为了查询条件，则直接使用该条件进行查询，忽略其余条件
                        conditions.append(" AND " + tableAliasName + "." + columnName + " = '" + value + "'");
                        break;
                    }

                    if (currentField.isAnnotationPresent(NeedIn.class)) {
                        // 如果是IN条件查询
                        String inValues = CustomSQLHelperUtils.getInValues(value);
                        conditions.append(" AND " + tableAliasName + "." + columnName + " IN (" + inValues + ")");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(NeedLike.class)) {
                        // 如果是模糊查询
                        NeedLike nl = currentField.getAnnotation(NeedLike.class);
                        conditions.append(" AND " + tableAliasName + "." + columnName + " LIKE '");
                        conditions.append(nl.PREFIX() ? "%" : "").append(value);
                        conditions.append(nl.SUFFIX() ? "%" : "").append("'");
                        continue;
                    }

                    if (currentField.isAnnotationPresent(DateField.class)) {
                        // 如果是日期条件
                        DateField df = currentField.getAnnotation(DateField.class);
                        String dateFormat = df.FORMAT();
                        if (df.NEEDDATERANGE()) {
                            // 如果日期条件是区间条件查询
                            String upLimitOperator = df.UP_LIMIT_OPERATOR().getOperator();
                            String lowLimitOperator = df.LOW_LIMIT_OPERATOR().getOperator();
                            List<String> dateRange = CustomSQLHelperUtils.getRangeValues(value);
                            conditions.append(" AND " + tableAliasName + "." + columnName);
                            conditions.append(" " + lowLimitOperator + " to_date('" + dateRange.get(0));
                            conditions.append("', '" + dateFormat + "')");
                            conditions.append(" AND " + tableAliasName + "." + columnName);
                            conditions.append(" " + upLimitOperator + " to_date('" + dateRange.get(1));
                            conditions.append("', '" + dateFormat + "')");
                        } else {
                            String operator = "=";
                            if (df.COMPARETYPE().getCode() > 0) {
                                operator = df.LOW_LIMIT_OPERATOR().getOperator();
                            }
                            if (df.COMPARETYPE().getCode() < 0) {
                                operator = df.UP_LIMIT_OPERATOR().getOperator();
                            }
                            conditions.append(" AND to_char(" + tableAliasName + "." + columnName);
                            conditions.append(", '" + dateFormat + "') " + operator + " '" + value + "'");
                        }
                        continue;
                    }

                    if (currentField.isAnnotationPresent(NumberField.class)) {
                        // 如果是数字条件
                        NumberField nf = currentField.getAnnotation(NumberField.class);
                        if (nf.NEEDDATERANGE()) {
                            // 如果数字条件是区间条件查询
                            String upLimitOperator = nf.UP_LIMIT_OPERATOR().getOperator();
                            String lowLimitOperator = nf.LOW_LIMIT_OPERATOR().getOperator();
                            List<String> numberRange = CustomSQLHelperUtils.getRangeValues(value);
                            conditions.append(" AND " + tableAliasName + "." + columnName + " " + lowLimitOperator + " '" + numberRange.get(0) + "'");
                            conditions.append(" AND " + tableAliasName + "." + columnName + " " + upLimitOperator + " '" + numberRange.get(1) + "'");
                        } else {
                            String operator = "=";
                            if (nf.COMPARETYPE().getCode() > 0) {
                                operator = nf.LOW_LIMIT_OPERATOR().getOperator();
                            }
                            if (nf.COMPARETYPE().getCode() < 0) {
                                operator = nf.UP_LIMIT_OPERATOR().getOperator();
                            }
                            conditions.append(" AND " + tableAliasName + "." + columnName + " " + operator + " '" + value + "'");
                        }
                        continue;
                    }

                    conditions.append(" AND " + tableAliasName + "." + columnName + " = '" + value + "'");
                }
            }
        }
        return conditions.toString();
    }

    /**
     * 获取条件的SQL语句段（用于获取已存在的数据）
     *
     * @param jo
     * @param tableMeta
     * @return
     */
    public static String getConditionsForExistSQL(JSONObject jo, TableMeta tableMeta) {
        String tableAliasName = tableMeta.getTableAliasName();
        StringBuffer conditions = new StringBuffer("");
        Map<String, String> validateExistFieldsColumnMap = tableMeta.getValidateExistFieldsColumnMap();
        if (validateExistFieldsColumnMap.size() == 0 || jo.isEmpty()) {
            conditions.append(" WHERE 1 = 2");
        } else {
            StringBuffer currConditions = new StringBuffer(" WHERE 1 = 1");
            for (Map.Entry<String, String> entry : validateExistFieldsColumnMap.entrySet()) {
                String property = entry.getKey();
                String columnName = entry.getValue();
                Field currentField = tableMeta.getAllFieldsMap().get(property);
                if (jo.containsKey(property)) {
                    Object value = jo.get(property);
                    if (property.equals(tableMeta.getPkProperty())) {
                        // 如果是主键作为了查询条件，则直接使用该条件进行查询，忽略其余条件
                        currConditions.append(" AND " + tableAliasName + "." + columnName + " = '" + value + "'");
                        break;
                    }

                    if (currentField.isAnnotationPresent(DateField.class)) {
                        // 如果是日期条件
                        String dateFormat = currentField.getAnnotation(DateField.class).FORMAT();
                        currConditions.append(" AND to_char(" + tableAliasName + "." + columnName);
                        currConditions.append(", '" + dateFormat + "') = '" + value + "'");
                        continue;
                    }

                    currConditions.append(" AND " + tableAliasName + "." + columnName + " = '" + value + "'");
                }
            }

            conditions.append(" WHERE 1 = 1".equals(currConditions.toString()) ? " WHERE 1 = 2" : currConditions);
        }

        return conditions.toString();
    }
}
