package cn.csg.ucs.bi.business.service.require.impl;

import cn.csg.core.common.utils.CommonUtils;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.*;
import cn.csg.ucs.bi.business.entity.dto.ExcelHeaderConstantDTO;
import cn.csg.ucs.bi.business.mapper.require.CProjectMgtMapper;
import cn.csg.ucs.bi.business.service.require.CProjectMgtService;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.utils.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CProjectMgtServiceImpl implements CProjectMgtService {

    Logger logger = LoggerFactory.getLogger(CProjectMgtServiceImpl.class);

    @Autowired
    private CProjectMgtMapper cProjectMgtMapper;

    @Autowired
    private CommonService commonService;

    @Override
    public JSONObject getSaveServiceInfo(JSONObject json) {

        JSONObject result = new JSONObject();

        // 本月的
        List<B_SAVE_SERVICE> list = cProjectMgtMapper.getSaveServiceInfo(json);
        // 组装本月新增数据
        Map<String, String> month = new HashMap<String, String>();
        for (B_SAVE_SERVICE saveService : list) {
            month.put("projectCode_" + saveService.getProjectCode(), saveService.getServiceValue());
        }
        month.put("title", "本月新增");
        month.put("theMonth", json.getString("theMonth"));
        month.put("company", json.getString("company"));
        if (!list.isEmpty()) {
            month.put("projectId", list.get(0).getProjectId());
        }

        // 今年的
        String theMonth = json.getString("theMonth");
        theMonth = theMonth.substring(0, 4);
        json.put("year", theMonth);
        List<B_SAVE_SERVICE> list_year = cProjectMgtMapper.getSaveServiceInfoForYear(json);
        // 组装本年新增数据
        Map<String, String> year = new HashMap<String, String>();
        for (B_SAVE_SERVICE saveService : list_year) {
            year.put("projectCode_" + saveService.getProjectCode(), saveService.getServiceValue());
        }
        year.put("title", "本年累计");

        List<Object> rowsList = new ArrayList<Object>();
        rowsList.add(month);
        rowsList.add(year);
        result.put("rows", rowsList);
        return result;
    }

    @Override
    public JSONObject getDiagnosisInfo(JSONObject json) {

        JSONObject result = new JSONObject();
        List<B_DIAGNOSIS> list = cProjectMgtMapper.getDiagnosisInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_DIAGNOSIS>(list).getTotal());
        return result;
    }

    @Override
    public JSONObject getHLTransformer(JSONObject json) {
        JSONObject result = new JSONObject();
        List<B_HL_TRANSFORMER> list = cProjectMgtMapper.getHLTransformer(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_HL_TRANSFORMER>(list).getTotal());
        return result;
    }

    @Override
    public JSONObject getPromoteLEDInfo(JSONObject json) {

        JSONObject result = new JSONObject();
        List<B_LED> list = cProjectMgtMapper.getPromoteLEDInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_LED>(list).getTotal());

        return result;
    }

    @Override
    public JSONObject getIncentiveInfo(JSONObject json) {
        JSONObject result = new JSONObject();
        List<B_INCENTIVE> list = cProjectMgtMapper.getIncentiveInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_INCENTIVE>(list).getTotal());
        return result;
    }

    @Override
    public JSONObject getClientSideInfo(JSONObject json) {
        JSONObject result = new JSONObject();
        List<B_CLIENT_SIDE> list = cProjectMgtMapper.getClientSideInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_CLIENT_SIDE>(list).getTotal());
        return result;
    }

    @Override
    public JSONObject getContractInfo(JSONObject json) {
        JSONObject result = new JSONObject();
        List<B_CONTRACT> list = cProjectMgtMapper.getContractInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_CONTRACT>(list).getTotal());
        return result;
    }

    @Override
    public JSONObject getUnContractInfo(JSONObject json) {
        JSONObject result = new JSONObject();
        List<B_UNCONTRACT> list = cProjectMgtMapper.getUnContractInfo(json);
        result.put("rows", list);
        result.put("total", new PageInfo<B_UNCONTRACT>(list).getTotal());
        return result;
    }

    @Override
    @Transactional
    public int addObject(JSONObject json, S_USER_INFO userInfo) {
        setUserInfo(json, userInfo);
        String projectCategory = json.getString("projectCategory");
        switch (projectCategory) {
            case "1":
                return addSaveService(json, userInfo);
            case "2":
                return cProjectMgtMapper.addCommon(json, B_DIAGNOSIS.class);
            case "3":
                return cProjectMgtMapper.addCommon(json, B_HL_TRANSFORMER.class);
            case "4":
                return cProjectMgtMapper.addCommon(json, B_LED.class);
            case "5":
                return cProjectMgtMapper.addCommon(json, B_INCENTIVE.class);
            case "6":
                return cProjectMgtMapper.addCommon(json, B_CLIENT_SIDE.class);
            case "7":
                return addContract(json, userInfo);
            case "8":
                return addUnContract(json, userInfo);
            default:
                return 0;
        }
    }

    private void setUserInfo(JSONObject json, S_USER_INFO userInfo) {
        String projectCategory = json.getString("projectCategory");
        if (!projectCategory.equals("7") && !projectCategory.equals("8")) {
            json.put("operator", userInfo.getUserName());
            json.put("operateDate", CommonUtils.createCurrentTimeStr());
            json.put("operatorCompany", userInfo.getOrgCode());
            json.put("dataState", "0");
        }
    }

    private int addSaveService(JSONObject json, S_USER_INFO userInfo) {

        Set<String> keySet = json.keySet();
        Iterator<String> iterator = keySet.iterator();
        List<B_SAVE_SERVICE> list = new ArrayList<B_SAVE_SERVICE>();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        String projectId = CommonUtils.createUUID();
        while (iterator.hasNext()) {
            while (iterator.hasNext()) {
                String key = iterator.next();
                String number = key.substring(key.length() - 2, key.length());
                if (pattern.matcher(number).matches()) {
                    B_SAVE_SERVICE saveService = new B_SAVE_SERVICE();
                    saveService.setGuid(CommonUtils.createUUID());
                    saveService.setDataState("0");
                    saveService.setDatasource("0");
                    saveService.setCompany(json.getString("company"));
                    saveService.setOperator(userInfo.getUserName());
                    saveService.setOperatorCompany(userInfo.getOrgCode());
                    saveService.setProjectCode(number);
                    saveService.setServiceValue(json.getString(key));
                    saveService.setProjectId(projectId);
                    list.add(saveService);
                }
            }
            ;

            if (list.isEmpty()) {
                return 0;
            }
        }
        ;
        int add = cProjectMgtMapper.addSaveService(list);
        return add;
    }

    @Override
    public List<Map<String, String>> queryExistsForValidate(JSONObject json) {
        String projectCategory = json.getString("projectCategory");
        switch (projectCategory) {
            case "1":
                return cProjectMgtMapper.queryExistsForValidate(json, B_SAVE_SERVICE.class);
            case "2":
                return cProjectMgtMapper.queryExistsForValidate(json, B_DIAGNOSIS.class);
            case "3":
                return cProjectMgtMapper.queryExistsForValidate(json, B_HL_TRANSFORMER.class);
            case "4":
                return cProjectMgtMapper.queryExistsForValidate(json, B_LED.class);
            case "5":
                return cProjectMgtMapper.queryExistsForValidate(json, B_INCENTIVE.class);
            case "6":
                return cProjectMgtMapper.queryExistsForValidate(json, B_CLIENT_SIDE.class);
            case "7":
                JSONArray jsonArray = json.getJSONArray("objArr");
                return cProjectMgtMapper.queryExistsForValidate(jsonArray.getJSONObject(0), B_CONTRACT.class);
            case "8":
                JSONArray jsonArrayUn = json.getJSONArray("objArr");
                return cProjectMgtMapper.queryExistsForValidate(jsonArrayUn.getJSONObject(0), B_UNCONTRACT.class);
        }
        return null;
    }

    @Override
    public int delByPrimaryKeys(JSONObject json) {
        String projectCategory = json.getString("projectCategory");
        String primaryKeys = json.getJSONArray("keys").toString().replaceAll("\\[", "")
                .replaceAll("]", "").replaceAll("\"", "'");
        switch (projectCategory) {
            case "1":
                return cProjectMgtMapper.delSaveService(json);
            case "2":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_DIAGNOSIS.class);
            case "3":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_HL_TRANSFORMER.class);
            case "4":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_LED.class);
            case "5":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_INCENTIVE.class);
            case "6":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_CLIENT_SIDE.class);
            case "7":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_CONTRACT.class);
            case "8":
                return cProjectMgtMapper.delByPrimaryKeys(primaryKeys, B_UNCONTRACT.class);
        }
        return 0;
    }

    @Override
    public int updateObject(JSONObject json, S_USER_INFO userInfo) {
        setUserInfo(json, userInfo);
        String projectCategory = json.getString("projectCategory");
        switch (projectCategory) {
            case "1":
                return updateSaveService(json, userInfo);
            case "2":
                return cProjectMgtMapper.updateCommon(json, B_DIAGNOSIS.class);
            case "3":
                return cProjectMgtMapper.updateCommon(json, B_HL_TRANSFORMER.class);
            case "4":
                return cProjectMgtMapper.updateCommon(json, B_LED.class);
            case "5":
                return cProjectMgtMapper.updateCommon(json, B_INCENTIVE.class);
            case "6":
                return cProjectMgtMapper.updateCommon(json, B_CLIENT_SIDE.class);
            case "7":
                return updateContract(json, userInfo);
            case "8":
                return updateUnContract(json, userInfo);
        }
        return 0;
    }

    private int updateSaveService(JSONObject json, S_USER_INFO userInfo) {
        int update = 0;
        JSONObject updateJson = new JSONObject();
        updateJson.put("operator", userInfo.getUserName());
        updateJson.put("company", json.get("company"));
        updateJson.put("operateDate", CommonUtils.createCurrentTimeStr());
        updateJson.put("operatorCompany", userInfo.getOrgCode());
        updateJson.put("dataState", "0");
        updateJson.put("theMonth", json.get("theMonth"));
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Set<String> keySet = json.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String number = key.substring(key.length() - 2, key.length());
            if (pattern.matcher(number).matches()) {
                updateJson.put("serviceValue", json.get(key));
                updateJson.put("projectCode", number);
                update = update + cProjectMgtMapper.updateSaveService(updateJson);
            }
        }
        return update;
    }

    private int updateContract(JSONObject json, S_USER_INFO userInfo) {
        JSONArray jsonArray = json.getJSONArray("objArr");
        int update = 0;
        for (Object o : jsonArray) {
            JSONObject js = (JSONObject) o;
            js.put("operator", userInfo.getUserName());
            js.put("operateDate", CommonUtils.createCurrentTimeStr());
            js.put("operatorCompany", userInfo.getOrgCode());
            js.put("dataState", "0");
            update += cProjectMgtMapper.updateCommon(js, B_CONTRACT.class);
        }
        return update;
    }

    private int updateUnContract(JSONObject json, S_USER_INFO userInfo) {
        JSONArray jsonArray = json.getJSONArray("objArr");
        int update = 0;
        for (Object o : jsonArray) {
            JSONObject js = (JSONObject) o;
            js.put("operator", userInfo.getUserName());
            js.put("operateDate", CommonUtils.createCurrentTimeStr());
            js.put("operatorCompany", userInfo.getOrgCode());
            js.put("dataState", "0");
            update += cProjectMgtMapper.updateCommon(js, B_UNCONTRACT.class);
        }
        return update;
    }

    private int addContract(JSONObject json, S_USER_INFO userInfo) {
        JSONArray jsonArray = json.getJSONArray("objArr");
        int add = 0;
        String projectId = CommonUtils.createUUID();
        for (Object o : jsonArray) {
            JSONObject js = (JSONObject) o;
            js.put("projectId", projectId);
            js.put("operator", userInfo.getUserName());
            js.put("operateDate", CommonUtils.createCurrentTimeStr());
            js.put("operatorCompany", userInfo.getOrgCode());
            js.put("dataState", "0");
            add += cProjectMgtMapper.addCommon(js, B_CONTRACT.class);
        }
        return add;
    }

    private int addUnContract(JSONObject json, S_USER_INFO userInfo) {
        JSONArray jsonArray = json.getJSONArray("objArr");
        int add = 0;
        String projectId = CommonUtils.createUUID();
        for (Object o : jsonArray) {
            JSONObject js = (JSONObject) o;
            js.put("projectId", projectId);
            js.put("operator", userInfo.getUserName());
            js.put("operateDate", CommonUtils.createCurrentTimeStr());
            js.put("operatorCompany", userInfo.getOrgCode());
            js.put("dataState", "0");
            add += cProjectMgtMapper.addCommon(js, B_UNCONTRACT.class);
        }
        return add;
    }

    @Override
    public File generateTemplate(String fileName, S_USER_INFO userInfo, String projectCategory) {
        String filePath = this.getClass().getResource("/").getPath() + "/resources/";
        switch (projectCategory) {
            case "2":
                filePath += "节能诊断明细项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.DIAGNOSIS_HEADER, ExcelHeaderConstantDTO.DIAGNOSIS_DOWNROWS, ExcelHeaderConstantDTO.DIAGNOSIS_METHOTYPE, ExcelHeaderConstantDTO.DIAGNOSIS_TYPE);
            case "3":
                filePath += "高损变压器项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.HL_TRANSFORMER_HEADER, ExcelHeaderConstantDTO.HL_TRANSFORMER_DOWNROWS, ExcelHeaderConstantDTO.HL_TRANSFORMER_METHOTYPE, ExcelHeaderConstantDTO.HL_TRANSFORMER_TYPE);
            case "4":
                filePath += "推广LED项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.LED_HEADER, ExcelHeaderConstantDTO.LED_DOWNROWS, ExcelHeaderConstantDTO.LED_METHOTYPE, ExcelHeaderConstantDTO.LED_TYPE);
            case "5":
                filePath += "激励措施或新设备新技术降低负荷项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.INCENTIVE_HEADER, ExcelHeaderConstantDTO.INCENTIVE_DOWNROWS, ExcelHeaderConstantDTO.INCENTIVE_METHOTYPE, ExcelHeaderConstantDTO.INCENTIVE_TYPE);
            case "6":
                filePath += "客户侧节能改造发电项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.CLIENT_SIDE_HEADER, ExcelHeaderConstantDTO.CLIENT_SIDE_DOWNROWS, ExcelHeaderConstantDTO.CLIENT_SIDE_METHOTYPE, ExcelHeaderConstantDTO.CLIENT_SIDE_TYPE);
            case "7":
                filePath += "合同能源管理项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.CONTRACT_HEADER, ExcelHeaderConstantDTO.CONTRACT_DOWNROWS, ExcelHeaderConstantDTO.CONTRACT_METHOTYPE, ExcelHeaderConstantDTO.CONTRACT_TYPE);
            case "8":
                filePath += "非合同能源管理项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.UNCONTRACT_HEADER, ExcelHeaderConstantDTO.UNCONTRACT_DOWNROWS, ExcelHeaderConstantDTO.UNCONTRACT_METHOTYPE, ExcelHeaderConstantDTO.UNCONTRACT_TYPE);
        }
        return null;
    }

    @Override
    public JSONResponseBody batchAdd(JSONObject json, S_USER_INFO userInfo) throws ClassNotFoundException {
        JSONResponseBody result = new JSONResponseBody();
        String message = "";
        int add = 0;
        String projectCategory = json.getString("projectCategory");
        HashMap<String, String> entityMap = commonService.getEntityNames("1", projectCategory);
        String clazzName = entityMap.get("CLASS_NAME");
        List<JSONObject> list = json.getObject("data", List.class);

        // 判断是否已上报
        for (int i = 0;i<list.size();i++){
            JSONObject item = list.get(i);
            item.put("projectSide","1");
            item.put("projectCategory",json.get("projectCategory"));
            int count = commonService.validateReport(item);
            if(count > 0){
                return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "第"+(i+1)+"条记录本月已上报");
            }
            item.put("operator", userInfo.getUserName());
            item.put("operateDate", CommonUtils.createCurrentTimeStr());
            item.put("guid", CommonUtils.createUUID());
            item.put("operatorCompany", userInfo.getOrgCode());
            item.put("dataState", "0");
        }

        list.forEach(item -> {
            item.put("operator", userInfo.getUserName());
            item.put("operateDate", CommonUtils.createCurrentTimeStr());
            item.put("guid", CommonUtils.createUUID());
            item.put("operatorCompany", userInfo.getOrgCode());
            item.put("dataState", "0");
        });
        Boolean validateFlag = true;
        if (!projectCategory.equals("1")) {//校验数据库本月是否有同户号项目
            List<Map<String, String>> validateExists = cProjectMgtMapper.batchValidateExists(list, Class.forName(clazzName));
            if (!CollectionUtils.isEmpty(validateExists)) {
                validateFlag = false;
                message += "户号" + StringUtils.join(validateExists.stream().map(x -> x.get("USER_NUM")).collect(Collectors.toList()), "、") + "数据库已存在数据，";
            }
        }
        if (projectCategory.equals("7") || projectCategory.equals("8")) {//合同与非合同导入校验
            //校验是否导入相同户号、项目名称超过12个月项目
            List<Map<String, String>> validateTimes = cProjectMgtMapper.importValidate(list, Class.forName(clazzName));
            if (!CollectionUtils.isEmpty(validateTimes)) {
                validateFlag = false;
                message += "户号" + StringUtils.join(validateTimes.stream().map(x -> x.get("USER_NUM")).collect(Collectors.toList()), "、") + "导入数据超过12个月，";
            }
            //校验导入数据是否存在相同户号数据
            List<JSONObject> temp = list.stream().map(item -> {
                JSONObject js = new JSONObject();
                js.put("userNum", item.getString("userNum"));
                js.put("projectId", item.getString("projectId"));
                return js;
            }).distinct().collect(Collectors.toList());
            List<Map<String, String>> validateRepeates = cProjectMgtMapper.repeatedVerificate(temp, Class.forName(clazzName));
            if (!CollectionUtils.isEmpty(validateRepeates)) {
                validateFlag = false;
                message += "户号" + StringUtils.join(validateRepeates.stream().map(x -> x.get("USER_NUM")).collect(Collectors.toList()), "、") + "在excel中存在重复数据，";
            }
        } else {//校验导入数据是否存在相同户号数据
            List<Map<String, String>> validateRepeates = cProjectMgtMapper.repeatedVerificate(list, Class.forName(clazzName));
            if (!CollectionUtils.isEmpty(validateRepeates)) {
                validateFlag = false;
                message += "户号" + StringUtils.join(validateRepeates.stream().map(x -> x.get("USER_NUM")).collect(Collectors.toList()), "、") + "在excel中存在重复数据，";
            }
        }
        if (validateFlag) {
            add = commonService.batchInsertCategoryInfo(clazzName, list, true);
        } else {
            message += "无法导入";
            return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, message);
        }

        if (add > 0) {
            result = JSONResponseBody.createSuccessResponseBody("导入成功", null);
        } else {
            result = JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "导入失败");
        }
        return result;
    }

    private File generateTemplate(String filePath, S_USER_INFO userInfo, String[] header, String[] downRows, String[] methoType, JSONArray arr) {
        List<String[]> downData = commonService.getDownData(methoType, userInfo, null);
        return ExcelUtil.createExcelTemplate(filePath, header, downData, downRows, arr);
    }
}

