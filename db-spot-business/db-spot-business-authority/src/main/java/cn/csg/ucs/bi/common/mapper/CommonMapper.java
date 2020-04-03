package cn.csg.ucs.bi.common.mapper;

import cn.csg.ucs.bi.common.entity.S_CODE_INFO;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.mapper.provider.CommonMapperProvider;
import cn.csg.ucs.bi.common.structure.TreeNode;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CommonMapper {

    @Select("SELECT t.CODE_VALUE, t.CODE_NAME FROM S_CODE_INFO t WHERE t.CODE_TYPE = #{codeType} ORDER BY t.CODE_ORDER, t.CODE_VALUE")
    @Results(id = "codeInfoSimpleMap", value = {
            @Result(column = "CODE_VALUE", property = "codeValue"),
            @Result(column = "CODE_NAME", property = "codeName"),
    })
    List<S_CODE_INFO> getCodesByCodeType(@Param("codeType") String codeType);

    @Select("SELECT t.CODE_ID, t.CODE_VALUE, t.CODE_NAME FROM S_TRADE_CODE_INFO t WHERE t.PARENT_CODE_ID = #{parentTradeCodeId} ORDER BY t.CODE_ORDER")
    @Results({
            @Result(column = "CODE_VALUE", property = "value"),
            @Result(column = "CODE_NAME", property = "label"),
            @Result(column = "CODE_ID", property = "childrenNodes", many = @Many(select = "cn.csg.ucs.bi.common.mapper.CommonMapper.getTreeTradeCategories", fetchType = FetchType.EAGER))
    })
    List<TreeNode> getTreeTradeCategories(@Param("parentTradeCodeId") String parentTradeCodeId);

    @Select("SELECT t.ORG_CODE, t.ORG_NAME FROM S_ORG_CODE_INFO t WHERE t.PARENT_ORG_CODE = #{parentOrgCode} ORDER BY t.ORG_CODE")
    @Results({
            @Result(column = "ORG_CODE", property = "value"),
            @Result(column = "ORG_NAME", property = "label"),
            @Result(column = "ORG_CODE", property = "childrenNodes", many = @Many(select = "cn.csg.ucs.bi.common.mapper.CommonMapper.getTreeOrgs", fetchType = FetchType.EAGER))
    })
    List<TreeNode> getTreeOrgs(@Param("parentOrgCode") String parentOrgCode);

    @Select("SELECT t.* FROM S_ORG_CODE_INFO t START WITH t.PARENT_ORG_CODE = #{parentOrgCode} CONNECT BY PRIOR t.ORG_CODE = t.PARENT_ORG_CODE")
    @Results(id = "orgCodeInfoMap", value = {
            @Result(column = "ORG_CODE", property = "orgCode"),
            @Result(column = "ORG_NAME", property = "orgName"),
            @Result(column = "ORG_SHORT_NAME", property = "orgShortName"),
            @Result(column = "PARENT_ORG_CODE", property = "parentOrgCode"),
            @Result(column = "ORG_CLASS", property = "orgClass")
    })
    List<S_ORG_CODE_INFO> getAllSubOrgs(@Param("parentOrgCode") String parentOrgCode);

    @Select("SELECT t.* FROM S_ORG_CODE_INFO t WHERE t.PARENT_ORG_CODE = #{parentOrgCode} ORDER BY t.ORG_CODE")
    @ResultMap(value = "orgCodeInfoMap")
    List<S_ORG_CODE_INFO> getSubOrgsByParentOrgCode(@Param("parentOrgCode") String parentOrgCode);

    @Select("SELECT t.CODE_VALUE, t.CODE_NAME FROM S_CODE_INFO t WHERE t.CODE_TYPE = #{codeType} and t.PARENT_CODE_ID = (select CODE_ID from S_CODE_INFO where CODE_TYPE = #{parentCodeType} and CODE_VALUE = #{parentCode}) ORDER BY t.CODE_ORDER, t.CODE_VALUE")
    @ResultMap(value = "codeInfoSimpleMap")
    List<S_CODE_INFO> getSubCodes(@Param("codeType") String codeType, @Param("parentCode") String parentCode, @Param("parentCodeType") String parentCodeType);

    @Select("SELECT DISTINCT t.VOLTAGE FROM S_OVERHEAD_LINE_PARAM t")
    List<String> getVoltage();

    @SelectProvider(type = CommonMapperProvider.class, method = "getModelNum")
    List<String> getModelNum(String tableName, JSONObject json);

    @Select("SELECT DISTINCT t.CAPACITY FROM S_TRANSFORMER_PARAM t WHERE t.MODEL_NUM = #{modelNum}")
    List<String> getCapacityByModelNum(@Param("modelNum") String modelNum);

    @Select("SELECT t.NOLOAD_LOSS, t.LOAD_LOSS FROM S_TRANSFORMER_PARAM t WHERE t.MODEL_NUM = #{modelNum} AND t.CAPACITY = #{capacity}")
    List<HashMap<String, Object>> getOtherTransformerParam(@Param("modelNum") String modelNum, @Param("capacity") String capacity);

    @Select("SELECT t.RESISTANCE, t.SAVE_ELE_CURRENT FROM S_CABLE_PARAM t WHERE t.MODEL_NUM = #{modelNum}")
    List<HashMap<String, Object>> getOtherCableParam(@Param("modelNum") String modelNum);

    @Select("SELECT t.RESISTANCE, t.SAVE_ELE_CURRENT FROM S_OVERHEAD_LINE_PARAM t WHERE t.VOLTAGE = #{voltage} AND t.MODEL_NUM = #{modelNum}")
    List<HashMap<String, Object>> getOtherOverheadLineParam(@Param("voltage") String voltage, @Param("modelNum") String modelNum);

    @Select("SELECT t.NEW_VALUE FROM B_UNIFICATION_HOURS t")
    List<String> getUnificationHours();

    @InsertProvider(type = CommonMapperProvider.class, method = "batchInsertCategoryInfo")
    int batchInsertCategoryInfo(String className, List<JSONObject> datas, boolean flag);

    @Select("SELECT t.TABLE_NAME, t.CLASS_NAME FROM S_PROJECT_CATEGORY_CLASS_REL t WHERE t.PROJECT_SIDE = #{projectSide} AND t.PROJECT_CATEGORY = #{projectCategory}")
    List<HashMap<String, String>> getEntityNames(@Param("projectSide") String projectSide, @Param("projectCategory") String projectCategory);

    /**
     * @Author chengzhifeng
     * @Description  验证是否已上报
     * @Date 17:59 2019/12/24
     * @Param [json]
     * @return int
     **/
    @Select("SELECT COUNT(1) FROM R_REPORT_INFO t1 WHERE t1.COMPANY = #{company} AND TO_CHAR(t1.THEMONTH,'yyyy-mm') = #{theMonth} AND t1.REPORT_CODE = #{projectCategory} AND T1.PROJECT_SIDE = #{projectSide} AND t1.SUBMIT_STATE = '1'")
    int validateReport(JSONObject json);
}
