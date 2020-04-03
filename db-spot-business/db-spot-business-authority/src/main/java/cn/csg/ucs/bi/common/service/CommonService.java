package cn.csg.ucs.bi.common.service;

import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.common.structure.TreeNode;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;

public interface CommonService {

    /**
     * 根据编码类型获取编码下拉数据集合
     *
     * @param codeType 编码类型
     * @return 下拉列表统一结构封装体
     */
    List<DropDown> getDropDownCodesByCodeType(String codeType);

    /**
     * 获取行业类别编码下拉树数据集合
     *
     * @param parentTradeCodeId
     * @return
     */
    List<TreeNode> getTreeTradeCategories(String parentTradeCodeId);

    /**
     * 获取组织机构编码下拉树数据集合
     *
     * @param parentOrgCode 父组织机构节点编码
     * @return 下拉树统一结构封装体
     */
    List<TreeNode> getTreeOrgs(String parentOrgCode);

    /**
     * 根据上级组织机构编码获取所有下属组织机构的编码
     *
     * @param parentOrgCode
     * @return
     */
    List<String> getAllSubOrgCodes(String parentOrgCode);

    /**
     * 根据上级组织机构编码获取下属组织机构的编码（不包含下级的下级）
     *
     * @param parentOrgCode
     * @return
     */
    List<DropDown> getSubOrgsByParentOrgCode(String parentOrgCode);

    /**
     * 根据父级编码类型、父级编码、子级编码类型获取子级编码下拉数据集合
     *
     * @param codeType
     * @param parentCode
     * @param parentCodeType
     * @return
     */
    List<DropDown> getSubDropDownCodes(String codeType, String parentCode, String parentCodeType);

    /**
     * 根据编码类型获取excel编码下拉数据数组
     *
     * @param codeType
     * @return
     */
    String[] getCodeArrayByCodeType(String codeType);

    /**
     * 获取行业类别编码excel下拉数据数组
     *
     * @param parentTradeCodeId
     * @return
     */
    String[] getTradeCategoriesArray(String parentTradeCodeId);

    /**
     * 获取组织机构编码excel下拉数据
     *
     * @param parentOrgCode
     * @return
     */
    String[] getOrgsArray(String parentOrgCode);

    /**
     * 获取额定电压
     *
     * @return
     */
    List<DropDown> getVoltage();

    /**
     * 获取变压器配变型号、导线类型
     *
     * @param tableName
     * @param json
     * @return
     */
    List<DropDown> getModelNum(String tableName, JSONObject json);

    /**
     * 根据变压器配配变型号获取变压器容量
     *
     * @param modelNum
     * @return
     */
    List<DropDown> getCapacityByModelNum(String modelNum);

    /**
     * 根据变压器配配变型号和变压器容量获取变压器用于计算的参数
     *
     * @param modelNum
     * @param capacity
     * @return
     */
    HashMap<String, Object> getTransformerParamForCalculate(String modelNum, String capacity);

    /**
     * 根据导线类型获取电缆用于计算的参数
     *
     * @param modelNum
     * @return
     */
    HashMap<String, Object> getCableParamForCalculate(String modelNum);

    /**
     * 根据额定电压和导线型号获取架空线用于计算的参数
     *
     * @param voltage
     * @param modelNum
     * @return
     */
    HashMap<String, Object> getOverheadLineParamForCalculate(String voltage, String modelNum);

    /**
     * 获取全年统调小时数
     *
     * @return
     */
    String getUnificationHours();

    /**
     * 批量新增自身侧和客户侧项目分类信息
     *
     * @param className
     * @param datas
     * @param flag
     * @return
     */
    int batchInsertCategoryInfo(String className, List<JSONObject> datas, boolean flag);

    /**
     * 根据项目所属侧和项目类别获取实体类名
     *
     * @param projectSide
     * @param projectCategory
     * @return
     */
    HashMap<String, String> getEntityNames(String projectSide, String projectCategory);

    /**
     * 获取导入模板下拉数据
     * @param methodTypes
     * @param userInfo
     * @param projectCategory
     * @return
     */
    List<String[]> getDownData(String[] methodTypes, S_USER_INFO userInfo, String projectCategory);

    /**
     * @Author chengzhifeng
     * @Description 验证是否已上报
     * @Date 17:58 2019/12/24
     * @Param [json]
     * @return int
     **/
    int validateReport(JSONObject json);
}
