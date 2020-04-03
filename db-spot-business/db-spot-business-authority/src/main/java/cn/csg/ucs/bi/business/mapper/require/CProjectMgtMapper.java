package cn.csg.ucs.bi.business.mapper.require;

import cn.csg.core.common.mapper.helper.CustomMapTypeHandler;
import cn.csg.ucs.bi.business.entity.B_DIAGNOSIS;
import cn.csg.ucs.bi.business.entity.B_HL_TRANSFORMER;
import cn.csg.ucs.bi.business.entity.B_LED;
import cn.csg.ucs.bi.business.entity.B_SAVE_SERVICE;
import cn.csg.core.common.mapper.helper.CustomMapTypeHandler;
import cn.csg.ucs.bi.business.entity.*;
import cn.csg.ucs.bi.business.mapper.require.provider.CProjectMgtMapperProvider;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CProjectMgtMapper {

    /**
     * @Author chengzhifeng
     * @Description 获取节能服务(本月新增)
     * @Date 10:26 2019/11/27
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_SAVE_SERVICE>
     **/
    @Select("SELECT * FROM B_SAVE_SERVICE t1 WHERE \"TO_CHAR\" (T1.THEMONTH, 'yyyy-mm') = #{theMonth} AND t1.COMPANY = #{company} ")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "PROJECT_CODE",property = "projectCode"),
            @Result(column = "SERVICE_VALUE",property = "serviceValue"),
            @Result(column = "PROJECT_ID",property = "projectId"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState")
    })
    public List<B_SAVE_SERVICE> getSaveServiceInfo(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 获取节能服务(本年累计)
     * @Date 10:26 2019/11/27
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_SAVE_SERVICE>
     **/
    @Select("SELECT \"SUM\" (T1.SERVICE_VALUE) AS SERVICE_VALUE, T1.PROJECT_CODE FROM B_SAVE_SERVICE t1 WHERE \"TO_CHAR\" (T1.THEMONTH, 'yyyy') = #{year} " +
            "AND t1.COMPANY = #{company} AND T1.PROJECT_CODE != '20' AND T1.PROJECT_CODE != '21' AND T1.PROJECT_CODE != '22' GROUP BY T1.PROJECT_CODE " +
            "UNION ALL SELECT \"SUM\" (T2.SERVICE_VALUE), T2.PROJECT_CODE AS SERVICE_VALUE FROM B_SAVE_SERVICE t2 WHERE T2.THEMONTH < ADD_MONTHS(SYSDATE, 1) AND T2.PROJECT_CODE = '22' AND T2.COMPANY = #{company} GROUP BY T2.PROJECT_CODE " +
            "UNION ALL SELECT \"SUM\" (T2.SERVICE_VALUE), T2.PROJECT_CODE AS SERVICE_VALUE FROM B_SAVE_SERVICE t2 WHERE T2.THEMONTH < ADD_MONTHS(SYSDATE, 1) AND T2.PROJECT_CODE = '20' AND T2.COMPANY = #{company} GROUP BY T2.PROJECT_CODE " +
            "UNION ALL SELECT \"SUM\" (T2.SERVICE_VALUE), T2.PROJECT_CODE AS SERVICE_VALUE FROM B_SAVE_SERVICE t2 WHERE T2.THEMONTH < ADD_MONTHS(SYSDATE, 1) AND T2.PROJECT_CODE = '21' AND T2.COMPANY = #{company} GROUP BY T2.PROJECT_CODE ")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "PROJECT_CODE",property = "projectCode"),
            @Result(column = "SERVICE_VALUE",property = "serviceValue"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState")
    })
    List<B_SAVE_SERVICE> getSaveServiceInfoForYear(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 获取节能诊断明细
     * @Date 10:51 2019/11/27
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_DIAGNOSIS>
     **/
    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getDiagnosisInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "IMPL_COMPANY",property = "implCompany"),
            @Result(column = "DIAGNOSIS_CATEGORY",property = "diagnosisCategory"),
            @Result(column = "DIAGNOSIS_DATE",property = "diagnosisDate"),
            @Result(column = "DIAGNOSIS_COST",property = "diagnosisCost"),
            @Result(column = "SAVE_QUANTITY",property = "saveQuantity"),
            @Result(column = "EX_REFORM",property = "exReform"),
            @Result(column = "EX_CONTRACT",property = "exContract"),
            @Result(column = "EX_COMPLETE",property = "exComplete"),
            @Result(column = "REFORM_COMPANY",property = "reformCompany"),
            @Result(column = "REFORM_TYPE",property = "reformType"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_REFORM_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_COMPLETE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_CONTRACT_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "USER_TRADE_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DIAGNOSIS_CATEGORY_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "REFORM_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_DIAGNOSIS> getDiagnosisInfo(JSONObject json);

    /**
     * 查询客户侧节能改造发电项目数据
     */
    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getIncentiveInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "PROJECT_NAME",property = "projectName"),
            @Result(column = "REDUCE_METHOD",property = "reduceMethod"),
            @Result(column = "INCENTIVE_CONTENT",property = "incentiveContent"),
            @Result(column = "IMPL_DATE",property = "implDate"),
            @Result(column = "TRANSFER_QUANTITY",property = "transferQuantity"),
            @Result(column = "PEAK_DURATION",property = "peakDuration"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "TRANSFER_QUANTITY_YEAR",property = "transferQuantityYear"),
            @Result(column = "PEAK_DURATION_YEAR",property = "peakDurationYear"),
            @Result(column = "TRANSFERLOAD_YEAR",property = "transferLoadYear"),
            @Result(column = "USER_TRADE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "REDUCE_METHOD_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_INCENTIVE> getIncentiveInfo(JSONObject json);

    /**
     * 查询激励措施或新设备新技术降低负荷数据
     */
    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getClientSideInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "PROJECT_NAME",property = "projectName"),
            @Result(column = "UNIT_CODE",property = "unitCode"),
            @Result(column = "UNIT_CAPACITY",property = "unitCapacity"),
            @Result(column = "WORKING_DATE",property = "workingDate"),
            @Result(column = "CATALOG_PRICE",property = "catalogPrice"),
            @Result(column = "GRID_PRICE",property = "gridPrice"),
            @Result(column = "UNIT_QUANTITY",property = "unitQuantity"),
            @Result(column = "RE_QUANTITY",property = "reQuantity"),
            @Result(column = "RE_CONVERT_QUANTITY",property = "reConvertQuantity"),
            @Result(column = "SELF_QUANTITY",property = "selfQuantity"),
            @Result(column = "UNIT_QUANTITY_YEAR",property = "unitQuantityYear"),
            @Result(column = "RE_QUANTITY_YEAR",property = "reQuantityYear"),
            @Result(column = "RE_CONVERT_QUANTITY_YEAR",property = "reConvertQuantityYear"),
            @Result(column = "SELF_QUANTITY_YEAR",property = "selfQuantityYear"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "TECHNOLOGY_CATEGORY",property = "technologyCategory"),
            @Result(column = "PROJECT_TYPE",property = "projectType"),
            @Result(column = "USER_TRADE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "TECHNOLOGY_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_CLIENT_SIDE> getClientSideInfo(JSONObject json);

    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getContractInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "PROJECT_NAME",property = "projectName"),
            @Result(column = "PROJECT_TYPE",property = "projectType"),
            @Result(column = "PROJECT_COMPLETE_DATE",property = "projectCompleteDate"),
            @Result(column = "IMPL_COMPANY",property = "implCompany"),
            @Result(column = "DIAGNOSIS_DATE",property = "diagnosisDate"),
            @Result(column = "TECHNOLOGY_CATEGORY",property = "technologyCategory"),
            @Result(column = "CONTRACT_START_DATE",property = "contractStartDate"),
            @Result(column = "CONTRACT_END_DATE",property = "contractEndDate"),
            @Result(column = "EX_CHECK",property = "exCheck"),
            @Result(column = "CALIBER_TYPE",property = "caliberType"),
            @Result(column = "INVEST_AMOUNT",property = "investAmount"),
            @Result(column = "ELE_SCALE",property = "eleScale"),
            @Result(column = "SAVE_QUANTITY",property = "saveQuantity"),
            @Result(column = "SAVE_POWER",property = "savePower"),
            @Result(column = "SAVE_CONVERT_QUANTITY",property = "saveConvertQuantity"),
            @Result(column = "SAVE_CONVERT_POWER",property = "saveConvertPower"),
            @Result(column = "SAVE_COST",property = "saveCost"),
            @Result(column = "SAVE_PROMOTE_TYPE",property = "savePromoteType"),
            @Result(column = "INVEST_AMOUNT1",property = "investAmount1"),
            @Result(column = "ELE_SCALE1",property = "eleScale1"),
            @Result(column = "SAVE_QUANTITY1",property = "saveQuantity1"),
            @Result(column = "SAVE_POWER1",property = "savePower1"),
            @Result(column = "SAVE_CONVERT_QUANTITY1",property = "saveConvertQuantity1"),
            @Result(column = "SAVE_CONVERT_POWER1",property = "saveConvertPower1"),
            @Result(column = "SAVE_COST1",property = "saveCost1"),
            @Result(column = "GUID1",property = "guid1"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "PROJECT_ID",property = "projectId"),
            @Result(column = "SAVE_PROMOTE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "CALIBER_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_CHECK_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "USER_TRADE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "TECHNOLOGY_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_CONTRACT> getContractInfo(JSONObject json);


    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getUnContractInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "PROJECT_NAME",property = "projectName"),
            @Result(column = "PROJECT_TYPE",property = "projectType"),
            @Result(column = "PROJECT_COMPLETE_DATE",property = "projectCompleteDate"),
            @Result(column = "IMPL_COMPANY",property = "implCompany"),
            @Result(column = "DIAGNOSIS_DATE",property = "diagnosisDate"),
            @Result(column = "TECHNOLOGY_CATEGORY",property = "technologyCategory"),
            @Result(column = "CONTRACT_START_DATE",property = "contractStartDate"),
            @Result(column = "CONTRACT_END_DATE",property = "contractEndDate"),
            @Result(column = "EX_CHECK",property = "exCheck"),
            @Result(column = "EX_SELF_IMPL",property = "exSelfImpl"),
            @Result(column = "CALIBER_TYPE",property = "caliberType"),
            @Result(column = "INVEST_AMOUNT",property = "investAmount"),
            @Result(column = "ELE_SCALE",property = "eleScale"),
            @Result(column = "SAVE_QUANTITY",property = "saveQuantity"),
            @Result(column = "SAVE_POWER",property = "savePower"),
            @Result(column = "SAVE_CONVERT_QUANTITY",property = "saveConvertQuantity"),
            @Result(column = "SAVE_CONVERT_POWER",property = "saveConvertPower"),
            @Result(column = "SAVE_COST",property = "saveCost"),
            @Result(column = "SAVE_PROMOTE_TYPE",property = "savePromoteType"),
            @Result(column = "INVEST_AMOUNT1",property = "investAmount1"),
            @Result(column = "ELE_SCALE1",property = "eleScale1"),
            @Result(column = "SAVE_QUANTITY1",property = "saveQuantity1"),
            @Result(column = "SAVE_POWER1",property = "savePower1"),
            @Result(column = "SAVE_CONVERT_QUANTITY1",property = "saveConvertQuantity1"),
            @Result(column = "SAVE_CONVERT_POWER1",property = "saveConvertPower1"),
            @Result(column = "SAVE_COST1",property = "saveCost1"),
            @Result(column = "GUID1",property = "guid1"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "PROJECT_ID",property = "projectId"),
            @Result(column = "EX_SELF_IMPL_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "SAVE_PROMOTE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "CALIBER_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_CHECK_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "USER_TRADE_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_TYPE_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "TECHNOLOGY_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_UNCONTRACT> getUnContractInfo(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 获取高损变压器项目信息
     * @Date 16:19 2019/11/27
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_HL_TRANSFORMER>
     **/
    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getHLTransformer")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "TRANSFORMER_CODE",property = "transformerCode"),
            @Result(column = "TRANSFORMER_MODEL_NUM",property = "transformerModelNum"),
            @Result(column = "CAPACITY",property = "capacity"),
            @Result(column = "RE_OR_EL_MONTH",property = "reOrElMonth"),
            @Result(column = "REFORM_MODEL_NUM",property = "reformModelNum"),
            @Result(column = "REFORM_COMPANY",property = "reformCompany"),
            @Result(column = "REFORM_TYPE",property = "reformType"),
            @Result(column = "SAVE_QUANTITY",property = "saveQuantity"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "USER_TRADE_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "REFORM_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_HL_TRANSFORMER> getHLTransformer(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 获取推广LED信息
     * @Date 19:54 2019/11/27
     * @Param [json]
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_LED>
     **/
    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "getPromoteLEDInfo")
    @Results({
            @Result(column = "THEMONTH",property = "theMonth"),
            @Result(column = "COMPANY",property = "company"),
            @Result(column = "USER_NUM",property = "userNum"),
            @Result(column = "USER_NAME",property = "userName"),
            @Result(column = "USER_TRADE_TYPE",property = "userTradeType"),
            @Result(column = "PROJECT_NAME",property = "projectName"),
            @Result(column = "EX_REFORM",property = "exReform"),
            @Result(column = "IMPL_COMPANY",property = "implCompany"),
            @Result(column = "IMPL_TYPE",property = "implType"),
            @Result(column = "LED_NUM",property = "ledNum"),
            @Result(column = "SAVE_ABILITY",property = "saveAbility"),
            @Result(column = "REDUCE_POWER_RATE",property = "reducePowerRate"),
            @Result(column = "INVEST_AMOUNT",property = "investAmount"),
            @Result(column = "CONTRACT_START_DATE",property = "contractStartDate"),
            @Result(column = "CONTRACT_END_DATE",property = "contractEndDate"),
            @Result(column = "DATASOURCE",property = "datasource"),
            @Result(column = "DATA_STATE",property = "dataState"),
            @Result(column = "COMPANY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "PROJECT_CATEGORY_RV", property = "dictionaries", typeHandler = CustomMapTypeHandler.class),
            @Result(column = "USER_TRADE_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "EX_REFORM_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "IMPL_TYPE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATASOURCE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class),
            @Result(column = "DATA_STATE_RV",property = "dictionaries",typeHandler = CustomMapTypeHandler.class)
    })
    List<B_LED> getPromoteLEDInfo(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 通用的新增mapper
     * @Date 14:39 2019/11/29
     * @Param [json, b_diagnosisClass]
     * @return void
     **/
    @InsertProvider(type = CProjectMgtMapperProvider.class,method="addCommon")
    int addCommon(JSONObject json, Class clazz);

    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "selectExistSQL")
    List<Map<String,String>> queryExistsForValidate(JSONObject json, Class clazz);

    /**
     * @Author chengzhifeng
     * @Description 新增节能服务mapper
     * @Date 14:24 2019/11/30
     * @Param [list]
     * @return int
     **/
    @Insert("<script>" +
            " insert all" +
            " <foreach collection='list' item='item' index='index'>" +
            "into B_SAVE_SERVICE (GUID, THEMONTH, COMPANY,PROJECT_CODE,SERVICE_VALUE,DATASOURCE,DATA_STATE,OPERATOR,OPERATE_DATE,OPERATOR_COMPANY,PROJECT_ID) values " +
            " (#{item.guid}, SYSDATE, #{item.company}, #{item.projectCode}, #{item.serviceValue}, #{item.datasource}, #{item.dataState},#{item.operator},SYSDATE,#{item.operatorCompany},#{item.projectId}) " +
            " </foreach> " +
            "SELECT 1 FROM DUAL " +
            " </script>" )
    int addSaveService(List<B_SAVE_SERVICE> list);

    @DeleteProvider(type = CProjectMgtMapperProvider.class, method = "delByPrimaryKeys")
    int delByPrimaryKeys(String primaryKeys, Class clazz);

    @UpdateProvider(type = CProjectMgtMapperProvider.class, method = "updateCommon")
    int updateCommon(JSONObject json, Class clazz);

    /**
     * @Author chengzhifeng
     * @Description 删除节能服务mapper
     * @Date 19:37 2019/12/2
     * @Param [json]
     * @return int
     **/
    @Delete("DELETE FROM B_SAVE_SERVICE t1 WHERE t1.COMPANY = #{company} AND \"TO_CHAR\"(t1.THEMONTH,'yyyy-MM') = #{theMonth}")
    int delSaveService(JSONObject json);

    /**
     * @Author chengzhifeng
     * @Description 更新节能服务mapper
     * @Date 20:41 2019/12/3
     * @Param [json, userInfo]
     * @return int
     **/
    //@UpdateProvider(type = CProjectMgtMapperProvider.class,method = "updateSaveService")
    @Update("UPDATE B_SAVE_SERVICE t1 SET " +
            "T1.SERVICE_VALUE = #{serviceValue}," +
            "T1.\"OPERATOR\" = #{operator}," +
            "T1.OPERATE_DATE = SYSDATE," +
            "T1.OPERATOR_COMPANY = #{operatorCompany}" +
            "WHERE T1.PROJECT_CODE = #{projectCode} AND t1.COMPANY = #{company} AND \"TO_CHAR\"(T1.THEMONTH,'yyyy-MM') = #{theMonth}")
    int updateSaveService(JSONObject json);

    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "importValidate")
    List<Map<String,String>> importValidate(List<JSONObject> list,Class clazz);

    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "batchValidateExists")
    List<Map<String,String>> batchValidateExists(List<JSONObject> list,Class clazz);

    @SelectProvider(type = CProjectMgtMapperProvider.class, method = "repeatedVerificate")
    List<Map<String,String>> repeatedVerificate(List<JSONObject> list,Class clazz);
}
