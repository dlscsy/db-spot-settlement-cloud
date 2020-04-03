package cn.csg.ucs.bi.common.service.impl;

import cn.csg.ucs.bi.common.entity.S_CODE_INFO;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.mapper.CommonMapper;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.common.structure.TreeNode;
import com.alibaba.fastjson.JSONObject;
import cn.csg.ucs.bi.core.permission.service.impl.OrgMgtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private OrgMgtServiceImpl orgMgtService;

    @Override
    public List<DropDown> getDropDownCodesByCodeType(String codeType) {
        List<S_CODE_INFO> codes = commonMapper.getCodesByCodeType(codeType);
        return codes.stream().map(code -> {
            DropDown current = new DropDown();
            current.setLabel(code.getCodeName());
            current.setValue(code.getCodeValue());
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TreeNode> getTreeTradeCategories(String parentTradeCodeId) {
        return commonMapper.getTreeTradeCategories(parentTradeCodeId);
    }

    @Override
    public List<TreeNode> getTreeOrgs(String parentOrgCode) {
        return commonMapper.getTreeOrgs(parentOrgCode);
    }

    @Override
    public List<String> getAllSubOrgCodes(String parentOrgCode) {
        List<S_ORG_CODE_INFO> orgs = commonMapper.getAllSubOrgs(parentOrgCode);
        return orgs.stream().map(org -> {
            return org.getOrgCode();
        }).collect(Collectors.toList());
    }

    @Override
    public List<DropDown> getSubOrgsByParentOrgCode(String parentOrgCode) {
        List<S_ORG_CODE_INFO> orgs = commonMapper.getSubOrgsByParentOrgCode(parentOrgCode);
        return orgs.stream().map(org -> {
            DropDown current = new DropDown();
            current.setLabel(org.getOrgName());
            current.setValue(org.getOrgCode());
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DropDown> getSubDropDownCodes(String codeType, String parentCode, String parentCodeType) {
        List<S_CODE_INFO> codes = commonMapper.getSubCodes(codeType, parentCode, parentCodeType);
        return codes.stream().map(code -> {
            DropDown current = new DropDown();
            current.setLabel(code.getCodeName());
            current.setValue(code.getCodeValue());
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public String[] getCodeArrayByCodeType(String codeType) {
        List<S_CODE_INFO> list = commonMapper.getCodesByCodeType(codeType);
        return list.stream().map(item -> {
            String codeName = item.getCodeValue() + "＊" + item.getCodeName();
            return codeName;
        }).collect(Collectors.toList()).stream().toArray(String[]::new);
    }

    @Override
    public String[] getTradeCategoriesArray(String parentTradeCodeId) {
        List<TreeNode> tree = commonMapper.getTreeTradeCategories(parentTradeCodeId);
        List<TreeNode> list = tree2List(tree);
        return list.stream().map(item -> {
            String codeName = item.getValue() + "＊" + item.getLabel();
            return codeName;
        }).collect(Collectors.toList()).stream().toArray(String[]::new);
    }

    @Override
    public String[] getOrgsArray(String parentOrgCode) {
        List<TreeNode> tree = commonMapper.getTreeOrgs(parentOrgCode);
        List<TreeNode> list = tree2List(tree);
        S_ORG_CODE_INFO rootObj = (S_ORG_CODE_INFO) orgMgtService.getMapper().customQueryByPrimaryKey(parentOrgCode);
        TreeNode node = new TreeNode();
        node.setValue(rootObj.getOrgCode());
        node.setLabel(rootObj.getOrgName());
        list.add(0, node);
        return list.stream().map(item -> {
            String codeName = item.getValue() + "＊" + item.getLabel();
            return codeName;
        }).collect(Collectors.toList()).stream().toArray(String[]::new);
    }

    private List<TreeNode> tree2List(List<TreeNode> tree) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        tree.forEach(item -> {
            list.add(item);
            if (!CollectionUtils.isEmpty(item.getChildrenNodes())) {
                list.addAll(tree2List(item.getChildrenNodes()));
                item.setChildrenNodes(null);
            }
        });
        return list;
    }

    @Override
    public List<DropDown> getVoltage() {
        List<String> voltages = commonMapper.getVoltage();
        return voltages.stream().map(voltage -> {
            DropDown current = new DropDown();
            current.setLabel(voltage);
            current.setValue(voltage);
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DropDown> getModelNum(String tableName, JSONObject json) {
        List<String> modelNums = commonMapper.getModelNum(tableName, json);
        return modelNums.stream().map(modelNum -> {
            DropDown current = new DropDown();
            current.setLabel(modelNum);
            current.setValue(modelNum);
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DropDown> getCapacityByModelNum(String modelNum) {
        List<String> values = commonMapper.getCapacityByModelNum(modelNum);
        return values.stream().map(capacity -> {
            DropDown current = new DropDown();
            current.setLabel(capacity);
            current.setValue(capacity);
            return current;
        }).collect(Collectors.toList());
    }

    @Override
    public HashMap<String, Object> getTransformerParamForCalculate(String modelNum, String capacity) {
        List<HashMap<String, Object>> paramsList = commonMapper.getOtherTransformerParam(modelNum, capacity);
        if (CollectionUtils.isEmpty(paramsList)) {
            HashMap<String, Object> mapForNull = new HashMap<String, Object>();
            mapForNull.put("CAPACITY", capacity);
            mapForNull.put("NOLOAD_LOSS", 0);
            mapForNull.put("LOAD_LOSS", 0);
            return mapForNull;
        } else {
            HashMap<String, Object> paramsMap = paramsList.get(0);
            paramsMap.put("CAPACITY", capacity);
            return paramsMap;
        }
    }

    @Override
    public HashMap<String, Object> getCableParamForCalculate(String modelNum) {
        List<HashMap<String, Object>> paramsList = commonMapper.getOtherCableParam(modelNum);
        if (CollectionUtils.isEmpty(paramsList)) {
            HashMap<String, Object> mapForNull = new HashMap<String, Object>();
            mapForNull.put("RESISTANCE", 0);
            mapForNull.put("SAVE_ELE_CURRENT", 0);
            return mapForNull;
        } else {
            HashMap<String, Object> paramsMap = paramsList.get(0);
            return paramsMap;
        }
    }

    @Override
    public HashMap<String, Object> getOverheadLineParamForCalculate(String voltage, String modelNum) {
        List<HashMap<String, Object>> paramsList = commonMapper.getOtherOverheadLineParam(voltage, modelNum);
        if (CollectionUtils.isEmpty(paramsList)) {
            HashMap<String, Object> mapForNull = new HashMap<String, Object>();
            mapForNull.put("VOLTAGE", voltage);
            mapForNull.put("RESISTANCE", 0);
            mapForNull.put("SAVE_ELE_CURRENT", 0);
            return mapForNull;
        } else {
            HashMap<String, Object> paramsMap = paramsList.get(0);
            paramsMap.put("VOLTAGE", voltage);
            return paramsMap;
        }
    }

    @Override
    public String getUnificationHours() {
        return commonMapper.getUnificationHours().get(0);
    }

    @Override
    public int batchInsertCategoryInfo(String className, List<JSONObject> datas, boolean flag) {
        return commonMapper.batchInsertCategoryInfo(className, datas, flag);
    }

    @Override
    public HashMap<String, String> getEntityNames(String projectSide, String projectCategory) {
        return commonMapper.getEntityNames(projectSide, projectCategory).get(0);
    }

    @Override
    public List<String[]> getDownData(String[] methodTypes, S_USER_INFO userInfo, String projectCategory) {
        List<String[]> downData = new ArrayList<String[]>();
        for (String methodType : methodTypes) {
            String[] typeAndParam = methodType.split("&");
            if ("1".equals(typeAndParam[0])) {//获取所属单位
                downData.add(getOrgsArray(userInfo.getOrgCode()));
            } else if ("2".equals(typeAndParam[0])) {//获取行业类别
                downData.add(getTradeCategoriesArray("-1"));
            } else if ("3".equals(typeAndParam[0])) {//获取其他
                downData.add(getCodeArrayByCodeType(typeAndParam[1]));
            } else if ("4".equals(typeAndParam[0])) {//自身侧 节能单位
                List<DropDown> orgs = getSubOrgsByParentOrgCode("03");
                String[] subOrgArr = orgs.stream().map(item -> {
                    String codeName = item.getValue() + "＊" + item.getLabel();
                    return codeName;
                }).collect(Collectors.toList()).stream().toArray(String[]::new);
                downData.add(subOrgArr);
            } else if ("5".equals(typeAndParam[0])) {//自身侧项目类别
                List<DropDown> values = getDropDownCodesByCodeType("0");
                String[] arr = values.stream().filter(code -> {
                    return code.getValue().equals(projectCategory);
                }).map(item -> {
                    String codeName = item.getValue() + "＊" + item.getLabel();
                    return codeName;
                }).collect(Collectors.toList()).stream().toArray(String[]::new);
                downData.add(arr);
            }
        }
        return downData;
    }

    @Override
    public int validateReport(JSONObject json) {
        return commonMapper.validateReport(json);
    }
}
