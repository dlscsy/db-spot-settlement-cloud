package cn.csg.ucs.bi.business.mapper.require.provider;

import cn.csg.core.common.annotation.DateField;
import cn.csg.core.common.annotation.NumberField;
import cn.csg.core.common.annotation.ValueSetting;
import cn.csg.core.common.constant.BaseBusinessMapperExceptionCode;
import cn.csg.core.common.exception.BaseBusinessMapperException;
import cn.csg.core.common.mapper.helper.*;
import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.provider.BaseProvider;
import cn.csg.core.common.structure.TableMeta;
import cn.csg.ucs.bi.business.entity.*;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CProjectMgtMapperProvider extends BaseProvider {

    private Logger logger = LoggerFactory.getLogger(CProjectMgtMapperProvider.class);

    public String getDiagnosisInfo(JSONObject jo) {
        TableMeta tableMeta = TableMeta.forClass(B_DIAGNOSIS.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(jo.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
        return sql.toString();
    }

    public String getHLTransformer(JSONObject jo) {
        TableMeta tableMeta = TableMeta.forClass(B_HL_TRANSFORMER.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(jo.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
        return sql.toString();

    }

    public String getPromoteLEDInfo(JSONObject jo) {
        TableMeta tableMeta = TableMeta.forClass(B_LED.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(jo.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
        return sql.toString();

    }

    public String addCommon(JSONObject json,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        StringBuffer sql = new StringBuffer();
        sql.append(CustomInsertSQLHelper.getInsertSQL(json,tableMeta));
        return sql.toString();
    }

    public String getIncentiveInfo(JSONObject json){
        TableMeta tableMeta = TableMeta.forClass(B_INCENTIVE.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(json.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(",t2.T2.TRANSFER_QUANTITY_YEAR,T2.PEAK_DURATION_YEAR,round(T2.TRANSFER_QUANTITY_YEAR/T2.PEAK_DURATION_YEAR,2) TRANSFERLOAD_YEAR");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(" left join (");
        sql.append("select USER_NUM,sum(TRANSFER_QUANTITY) TRANSFER_QUANTITY_YEAR,sum(PEAK_DURATION) PEAK_DURATION_YEAR from B_INCENTIVE where TO_CHAR(THEMONTH,'yyyy-mm') <= '")
                .append(json.getString("theMonth")).append("' and TO_CHAR(THEMONTH,'yyyy') = SUBSTR('").append(json.getString("theMonth")).append("',1,4) GROUP BY USER_NUM ");
        sql.append(") T2 on ").append(tableMeta.getTableAliasName()).append(".USER_NUM = T2.USER_NUM");
        sql.append(" where ");
        sql.append(tableMeta.getTableAliasName()).append(".COMPANY LIKE '").append(json.getString("company")).append("%'");
        sql.append(" and TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')='").append(json.getString("theMonth")).append("'");
        if(!StringUtils.isEmpty(json.getString("userNum"))){
            sql.append(" and ").append(tableMeta.getTableAliasName()).append(".USER_NUM = '").append(json.getString("userNum")).append("'");
        }
        sql.append(" ORDER BY COMPANY");
        return sql.toString();
    }

    public String getClientSideInfo(JSONObject json){
        TableMeta tableMeta = TableMeta.forClass(B_CLIENT_SIDE.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(json.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(",t2.T2.UNIT_QUANTITY_YEAR,T2.RE_QUANTITY_YEAR,T2.RE_CONVERT_QUANTITY_YEAR,T2.SELF_QUANTITY_YEAR");
        sql.append(" FROM ").append(tableMeta.getTableName()).append(" ").append(tableMeta.getTableAliasName());
        sql.append(" left join (");
        sql.append("select USER_NUM,sum(UNIT_QUANTITY) UNIT_QUANTITY_YEAR,sum(RE_QUANTITY) RE_QUANTITY_YEAR,sum(RE_CONVERT_QUANTITY) RE_CONVERT_QUANTITY_YEAR,sum(SELF_QUANTITY) SELF_QUANTITY_YEAR from B_CLIENT_SIDE where TO_CHAR(THEMONTH,'yyyy-mm') <='")
                .append(json.getString("theMonth")).append("' and TO_CHAR(THEMONTH,'yyyy') = SUBSTR('").append(json.getString("theMonth")).append("',1,4) GROUP BY USER_NUM ");
        sql.append(") T2 on ").append(tableMeta.getTableAliasName()).append( ".USER_NUM = T2.USER_NUM");
        sql.append(" where ");
        sql.append(tableMeta.getTableAliasName()).append(".COMPANY LIKE '").append(json.getString("company")).append("%'");
        sql.append(" and TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')='").append(json.getString("theMonth")).append("'");
        if(!StringUtils.isEmpty(json.getString("userNum"))){
            sql.append("and ").append(tableMeta.getTableAliasName()).append(".USER_NUM = '").append(json.getString("userNum")).append("'");
        }
        sql.append(" ORDER BY COMPANY");
        return sql.toString();
    }

    public String getContractInfo(JSONObject json){
        TableMeta tableMeta = TableMeta.forClass(B_CONTRACT.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(json.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(",T2.INVEST_AMOUNT INVEST_AMOUNT1,T2.ELE_SCALE ELE_SCALE1,T2.SAVE_QUANTITY SAVE_QUANTITY1,T2.SAVE_POWER SAVE_POWER1,T2.SAVE_CONVERT_QUANTITY SAVE_CONVERT_QUANTITY1,T2.SAVE_CONVERT_POWER SAVE_CONVERT_POWER1,T2.SAVE_COST SAVE_COST1,T2.GUID GUID1");
        sql.append(" FROM ").append(tableMeta.getTableName()).append(" ").append(tableMeta.getTableAliasName());
        sql.append(" left join ").append(tableMeta.getTableName()).append(" ").append("T2");
        sql.append(" on T2.PROJECT_ID=").append(tableMeta.getTableAliasName()).append(".PROJECT_ID");
//        sql.append(" on T2.USER_NUM=").append(tableMeta.getTableAliasName()).append(".USER_NUM");
//        sql.append(" and ").append("TO_CHAR(T2.THEMONTH,'yyyy-mm') = ").append("TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')");
        sql.append(" and T2.CALIBER_TYPE  = '1'");
        sql.append(" where ");
        sql.append(tableMeta.getTableAliasName()).append(".COMPANY LIKE '").append(json.getString("company")).append("%'");
        sql.append(" and TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')='").append(json.getString("theMonth")).append("'");
        sql.append(" and ").append(tableMeta.getTableAliasName()).append(".CALIBER_TYPE='0'");
        if(!StringUtils.isEmpty(json.getString("userNum"))){
            sql.append(" and ").append(tableMeta.getTableAliasName()).append(".USER_NUM = '").append(json.getString("userNum")).append("'");
        }
        if(!StringUtils.isEmpty(json.getString("category"))){
            sql.append(" and ").append(tableMeta.getTableAliasName()).append(".CATEGORY='").append(json.getString("category")).append("'");
        }
        sql.append(" ORDER BY COMPANY");
        return sql.toString();
    }

    public String getUnContractInfo(JSONObject json){
        TableMeta tableMeta = TableMeta.forClass(B_UNCONTRACT.class);
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(",(select code_name from S_CODE_INFO where CODE_VALUE = '").append(json.getString("projectCategory")).append("' and CODE_TYPE = '1') PROJECT_CATEGORY_RV");
        sql.append(",T2.INVEST_AMOUNT INVEST_AMOUNT1,T2.ELE_SCALE ELE_SCALE1,T2.SAVE_QUANTITY SAVE_QUANTITY1,T2.SAVE_POWER SAVE_POWER1,T2.SAVE_CONVERT_QUANTITY SAVE_CONVERT_QUANTITY1,T2.SAVE_CONVERT_POWER SAVE_CONVERT_POWER1,T2.SAVE_COST SAVE_COST1,T2.GUID GUID1");
        sql.append(" FROM ").append(tableMeta.getTableName()).append(" ").append(tableMeta.getTableAliasName());
        sql.append(" left join ").append(tableMeta.getTableName()).append(" ").append("T2");
        sql.append(" on T2.PROJECT_ID=").append(tableMeta.getTableAliasName()).append(".PROJECT_ID");
//        sql.append(" on T2.USER_NUM=").append(tableMeta.getTableAliasName()).append(".USER_NUM");
//        sql.append(" and ").append("TO_CHAR(T2.THEMONTH,'yyyy-mm') = ").append("TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')");
        sql.append(" and T2.CALIBER_TYPE  = '1'");
        sql.append(" where ");
        sql.append(tableMeta.getTableAliasName()).append(".COMPANY LIKE '").append(json.getString("company")).append("%'");
        sql.append(" and TO_CHAR(").append(tableMeta.getTableAliasName()).append(".THEMONTH,'yyyy-mm')='").append(json.getString("theMonth")).append("'");
        sql.append(" and ").append(tableMeta.getTableAliasName()).append(".CALIBER_TYPE='0'");
        if(!StringUtils.isEmpty(json.getString("userNum"))){
            sql.append(" and ").append(tableMeta.getTableAliasName()).append(".USER_NUM = '").append(json.getString("userNum")).append("'");
        }
        if(!StringUtils.isEmpty(json.getString("category"))){
            sql.append(" and ").append(tableMeta.getTableAliasName()).append(".CATEGORY='").append(json.getString("category")).append("'");
        }
        sql.append(" ORDER BY COMPANY");
        return sql.toString();
    }

    public String selectExistSQL(JSONObject json,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        return this.selectExistSQLWrapper(tableMeta, json);
    }

    public String delByPrimaryKeys(String primaryKeys,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        if (StringUtils.isEmpty(tableMeta.getPkColumn())) {
            throw new NullPointerException(clazz.getName() + "实体类没有用@Id注解指明主键");
        }
        StringBuffer conditions = new StringBuffer(" WHERE " + tableMeta.getTableAliasName() + "." + tableMeta.getPkColumn() + " IN (" + primaryKeys + ")");
        return this.delSQLWrapper(tableMeta, conditions.toString());
    }

    public String updateCommon(JSONObject json,Class clazz){
        StringBuffer sql = new StringBuffer();
        TableMeta tableMeta = TableMeta.forClass(clazz);
        sql = sql.append(getUpdateSQL(json,tableMeta));
        return sql.toString();
    }

    public static String getUpdateSQL(JSONObject jo, TableMeta tableMeta) {
        String tableAliasName = tableMeta.getTableAliasName();
        StringBuffer updateSQL = new StringBuffer("");
        String primaryKeyValue = jo.getString("primaryKeyValue");
        jo.remove("primaryKeyValue");
        if (!jo.isEmpty()) {
            updateSQL.append("UPDATE " + tableMeta.getTableName() + " " + tableAliasName + " SET");
            StringBuffer setSQL = new StringBuffer("");
//            System.out.println(tableMeta.getAllFieldsMap());
//            System.out.println(tableMeta.getAllFieldsColumnMap());
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String property = entry.getKey(); // 获取条件属性字段名称，这个字段与实体类中的属性字段名称是一致的
                Object value = entry.getValue(); // 获取条件属性值
                if (tableMeta.getAllFieldsColumnMap().containsKey(property) && !StringUtils.isEmpty(value)) {
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
                }else if(tableMeta.getAllFieldsColumnMap().containsKey(property) && StringUtils.isEmpty(value)) {
                    Field currentField = tableMeta.getAllFieldsMap().get(property);
                    String columnName = tableMeta.getAllFieldsColumnMap().get(property);
                    // 如果是空值
                    value = currentField.isAnnotationPresent(ValueSetting.class)
                            ? currentField.getAnnotation(ValueSetting.class).DEFAULT_VALUE() : "null";
                    setSQL.append(" " + tableAliasName + "." + columnName + " = " + value + ",");
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

    public String importValidate(List<JSONObject> list, Class clazz){
        StringBuffer sql = new StringBuffer("select DISTINCT t1.* from ");
        TableMeta tableMeta = TableMeta.forClass(clazz);
        sql.append("(").append("select min(THEMONTH) THEMONTH,USER_NUM,PROJECT_NAME from ").append(tableMeta.getTableName()).append(" GROUP BY USER_NUM,PROJECT_NAME) t1");
        String dual = selectListFromDual(list);
        sql.append(",(").append(dual).append(") t2");
        sql.append(" where t1.USER_NUM = t2.USER_NUM and t1.PROJECT_NAME = t2.PROJECT_NAME and MONTHS_BETWEEN(TO_DATE(t2.THEMONTH, 'yyyy-MM') ,t1.THEMONTH) > 11");
        return sql.toString();
    }

    public String batchValidateExists(List<JSONObject> list,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        StringBuffer sql = new StringBuffer("select DISTINCT t1.USER_NUM from ");
        sql.append(tableMeta.getTableName()).append(" t1");
        String dual = selectListFromDual(list);
        sql.append(",(").append(dual).append(") t2");
        sql.append(" where t1.USER_NUM = t2.USER_NUM and TO_CHAR(t1.THEMONTH,'yyyy-mm') = t2.THEMONTH");
        return sql.toString();
    }

    public String repeatedVerificate(List<JSONObject> list,Class clazz){
        TableMeta tableMeta = TableMeta.forClass(clazz);
        StringBuffer sql = new StringBuffer("select t2.* from (select t1.USER_NUM,count(t1.USER_NUM) countNum from ");
        String dual = selectListFromDual(list);
        sql.append("(").append(dual).append(") t1").append(" group by USER_NUM) t2 where t2.countNum > 1") ;
        return sql.toString();
    }

    private String selectListFromDual(List<JSONObject> list){
        StringBuffer dual = new StringBuffer("");
        for (int i=0;i<list.size();i++){
            JSONObject json = list.get(i);
            dual.append("select '").append(json.getString("theMonth")).append("' as THEMONTH,'").
                    append(json.getString("userNum")).append("' as USER_NUM,'").append(json.getString("projectName")).
                    append("' as PROJECT_NAME").append(" from dual");

            if(i != list.size()-1){
                dual.append(" union all ");
            }
        }
        return dual.toString();
    }
}
