package cn.csg.ucs.bi.business.mapper.require;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.core.common.mapper.helper.CustomMapTypeHandler;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import cn.csg.ucs.bi.business.mapper.require.provider.CProjectMgtMapperProvider;
import cn.csg.ucs.bi.business.mapper.require.provider.SProjectMgtMapperProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface SProjectMgtMapper extends BaseBusinessMapper<SelfSaveBase> {

    @SelectProvider(type = SProjectMgtMapperProvider.class, method = "getSProjectInfo")
    @Results({
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "THEMONTH", property = "theMonth"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "SAVE_PROPERTY", property = "saveProperty"),
            @Result(column = "COMPANY", property = "company"),
            @Result(column = "PROJECT_CATEGORY", property = "projectCategory"),
            @Result(column = "SAVE_COMPANY", property = "saveCompany"),
            @Result(column = "TRADE_CATEGORY", property = "tradeCategory"),
            @Result(column = "AREA", property = "area"),
            @Result(column = "EX_CONTRACT", property = "exContract"),
            @Result(column = "PROJECT_TOTAL_INVEST", property = "projectTotalInvest"),
            @Result(column = "EX_SAVE_CHECK", property = "exSaveCheck"),
            @Result(column = "SAVE_CHECK_COMPANY", property = "saveCheckCompany"),
            @Result(column = "WORKING_DATE", property = "workingDate"),
            @Result(column = "PROJECT_NUM", property = "projectNum"),
            @Result(column = "IMPL_COMPANY", property = "implCompany"),
            @Result(column = "EX_SAVE_DIAGNOSE", property = "exSaveDiagnose"),
            @Result(column = "DIAGNOSE_DATE", property = "diagnoseDate"),
            @Result(column = "PROJECT_IMPL_PROPERTY", property = "projectImplProperty"),
            @Result(column = "SAVE_PROMOTE_TYPE", property = "savePromoteType"),
            @Result(column = "CONTRACT_START_DATE", property = "contractStartDate"),
            @Result(column = "CONTRACT_END_DATE", property = "contractEndDate"),
            @Result(column = "DATASOURCE", property = "dataSource"),
            @Result(column = "DATA_STATE", property = "dataState"),
            @Result(column = "OPERATOR", property = "operator"),
            @Result(column = "OPERATE_DATE", property = "operateDate"),
            @Result(column = "OPERATOR_COMPANY", property = "operatorCompany"),
            @Result(column = "TABLE_NAME", property = "tableName"),
            @Result(column = "SAVE_PROPERTY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "SAVE_COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "TRADE_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_CONTRACT_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_SAVE_CHECK_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_SAVE_DIAGNOSE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_IMPL_PROPERTY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "SAVE_PROMOTE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "OPERATOR_COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "{projectId=PROJECT_ID,tableName=TABLE_NAME}", property = "subInfos",  many = @Many(select = "cn.csg.ucs.bi.business.mapper.require.SProjectMgtMapper.getSubInfos", fetchType = FetchType.EAGER))
    })
    List<SelfSaveBase> getSProjectInfo(JSONObject jo);

    @SelectProvider(type = SProjectMgtMapperProvider.class, method = "getSubInfos")
    List<HashMap<String, Object>> getSubInfos(String projectId, String tableName, String className);

    @UpdateProvider(type = SProjectMgtMapperProvider.class, method = "updateSubInfos")
    int updateSubInfos(JSONObject jo, String className);

    @DeleteProvider(type = SProjectMgtMapperProvider.class, method = "delSProjectInfo")
    int delSProjectInfo(String tableName, String keys);

    @DeleteProvider(type = SProjectMgtMapperProvider.class, method = "delSubInfos")
    int delSubInfos(String tableName, String keys);

    @SelectProvider(type = SProjectMgtMapperProvider.class, method = "repeatedVerificate")
    List<Map<String,String>> repeatedVerificate(List<JSONObject> list,Class clazz);
}
