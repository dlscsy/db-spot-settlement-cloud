package cn.csg.ucs.bi.common.controller;

import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.common.structure.TreeNode;
import cn.csg.ucs.bi.core.permission.service.impl.OrgMgtServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/commonController")
public class CommonController {

    @Autowired
    @Qualifier("commonService")
    private CommonService commonService;

    @Autowired
    private OrgMgtServiceImpl orgMgtService;

    @ResponseBody
    @GetMapping("/getDropDownCodesByCodeType/{codeType}")
    public JSONResponseBody getDropDownCodesByCodeType(@PathVariable("codeType") String codeType) {
        List<DropDown> values = commonService.getDropDownCodesByCodeType(codeType);
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getTreeTradeCategories/{parentTradeCodeId}")
    public JSONResponseBody getTreeTradeCategories(@PathVariable("parentTradeCodeId") String parentTradeCodeId) {
        List<TreeNode> treeNodes = commonService.getTreeTradeCategories(parentTradeCodeId);
        JSONObject jo = new JSONObject();
        jo.put("treeNodes", treeNodes);
        return JSONResponseBody.createSuccessResponseBody("", jo);
    }

    @ResponseBody
    @GetMapping("/getTreeOrgs/{parentOrgCode}")
    public JSONResponseBody getTreeOrgs(@PathVariable("parentOrgCode") String parentOrgCode) {
        List<TreeNode> treeNodes = commonService.getTreeOrgs(parentOrgCode);
        JSONObject jo = new JSONObject();
        if ("00".equals(parentOrgCode)) {
            jo.put("treeNodes", treeNodes);
        } else {
            S_ORG_CODE_INFO rootObj = (S_ORG_CODE_INFO) orgMgtService.getMapper().customQueryByPrimaryKey(parentOrgCode);
            TreeNode root = new TreeNode();
            root.setValue(rootObj.getOrgCode());
            root.setLabel(rootObj.getOrgName());
            root.setChildrenNodes(treeNodes);
            List<TreeNode> result = new ArrayList<>();
            result.add(root);
            jo.put("treeNodes", result);
        }
        return JSONResponseBody.createSuccessResponseBody("", jo);
    }

    @ResponseBody
    @GetMapping("/getAllSubOrgs/{parentOrgCode}")
    public JSONResponseBody getAllSubOrgs(@PathVariable("parentOrgCode") String parentOrgCode) {
        List<String> subCodes = commonService.getAllSubOrgCodes(parentOrgCode);
        return JSONResponseBody.createSuccessResponseBody("", subCodes);
    }

    @ResponseBody
    @GetMapping("/getSubOrgsByParentOrgCode/{parentOrgCode}")
    public JSONResponseBody getSubOrgsByParentOrgCode(@PathVariable("parentOrgCode") String parentOrgCode) {
        List<DropDown> values = commonService.getSubOrgsByParentOrgCode(parentOrgCode);
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getSubDropDownCodes")
    public JSONResponseBody getSubDropDownCodes(String params) {
        JSONObject json = JSONObject.parseObject(params);
        String codeType = json.getString("codeType");
        String parentCode = json.getString("parentCode");
        String parentCodeType = json.getString("parentCodeType");
        List<DropDown> values = commonService.getSubDropDownCodes(codeType, parentCode, parentCodeType);
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getVoltage")
    public JSONResponseBody getVoltage() {
        List<DropDown> values = commonService.getVoltage();
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getModelNum/{tableName}")
    public JSONResponseBody getModelNum(@PathVariable("tableName") String tableName, String params) {
        JSONObject json = JSONObject.parseObject(params);
        List<DropDown> values = commonService.getModelNum(tableName, json);
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getCapacityByModelNum")
    public JSONResponseBody getCapacityByModelNum(String params) {
        JSONObject json = JSONObject.parseObject(params);
        String modelNum = json.getString("MODEL_NUM");
        List<DropDown> values = commonService.getCapacityByModelNum(modelNum);
        return JSONResponseBody.createSuccessResponseBody("", values);
    }

    @ResponseBody
    @GetMapping("/getTransformerParamForCalculate")
    public JSONResponseBody getTransformerParamForCalculate(String params) {
        JSONObject json = JSONObject.parseObject(params);
        String modelNum = json.getString("MODEL_NUM");
        String capacity = json.getString("CAPACITY");
        HashMap<String, Object> paramsMap = commonService.getTransformerParamForCalculate(modelNum, capacity);
        return JSONResponseBody.createSuccessResponseBody("", paramsMap);
    }

    @ResponseBody
    @GetMapping("/getCableParamForCalculate")
    public JSONResponseBody getCableParamForCalculate(String params) {
        JSONObject json = JSONObject.parseObject(params);
        String modelNum = json.getString("MODEL_NUM");
        HashMap<String, Object> paramsMap = commonService.getCableParamForCalculate(modelNum);
        return JSONResponseBody.createSuccessResponseBody("", paramsMap);
    }

    @ResponseBody
    @GetMapping("/getOverheadLineParamForCalculate")
    public JSONResponseBody getOverheadLineParamForCalculate(String params) {
        JSONObject json = JSONObject.parseObject(params);
        String voltage = json.getString("VOLTAGE");
        String modelNum = json.getString("MODEL_NUM");
        HashMap<String, Object> paramsMap = commonService.getOverheadLineParamForCalculate(voltage, modelNum);
        return JSONResponseBody.createSuccessResponseBody("", paramsMap);
    }

    @ResponseBody
    @GetMapping("/getUnificationHours")
    public JSONResponseBody getUnificationHours() {
        String hours = commonService.getUnificationHours();
        return JSONResponseBody.createSuccessResponseBody("", hours);
    }

    /**
     * @Author chengzhifeng
     * @Description 验证是否已上报
     * @Date 17:56 2019/12/24
     * @Param [params]
     * @return cn.csg.ucs.bi.base.structure.JSONResponseBody
     **/
    @ResponseBody
    @PostMapping("/validateReport")
    public JSONResponseBody validateReport(String params) {

        Boolean result = false;
        JSONObject json = JSONObject.parseObject(params);
        int count = commonService.validateReport(json);
        if (count > 0) {
            result = true;
        }
        return JSONResponseBody.createSuccessResponseBody("", result);
    }
}
