package cn.csg.ucs.bi.business.service.require.impl;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.core.common.utils.CommonUtils;
import cn.csg.ucs.bi.base.constant.BaseCode;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.dto.ExcelHeaderConstantDTO;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import cn.csg.ucs.bi.business.mapper.require.SProjectMgtMapper;
import cn.csg.ucs.bi.business.service.require.SProjectMgtService;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.common.service.CommonService;
import cn.csg.ucs.bi.common.structure.DropDown;
import cn.csg.ucs.bi.utils.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("sProjectMgtService")
public class SProjectMgtServiceImpl extends AbstractBaseBusinessService<SelfSaveBase> implements SProjectMgtService {

    @Autowired
    private SProjectMgtMapper mapper;

    @Autowired
    private CommonService commonService;

    @Override
    public List<SelfSaveBase> getSProjectInfo(JSONObject jo) {
        return mapper.getSProjectInfo(jo);
    }

    @Override
    public int updateSubInfos(JSONObject jo, String className) {
        return mapper.updateSubInfos(jo, className);
    }

    @Override
    public int delSProjectInfo(String tableName, String keys) {
        return mapper.delSProjectInfo(tableName, keys);
    }

    @Override
    public int delSubInfos(String tableName, String keys) {
        return mapper.delSubInfos(tableName, keys);
    }

    @Override
    public File generateSelfTemplate(String fileName, S_USER_INFO userInfo, String projectCategory) {
        String filePath = this.getClass().getResource("/").getPath() + "/resources/";
        switch (projectCategory) {
            case "1":
                filePath += "变电站无功补偿项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.COMPENSATE_HEADER, ExcelHeaderConstantDTO.COMPENSATE_DOWNROWS, ExcelHeaderConstantDTO.COMPENSATE_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.COMPENSATE_TYPE);
            case "2":
                filePath += "高效变压器应用项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.HE_TRANSFORMER_HEADER, ExcelHeaderConstantDTO.HE_TRANSFORMER_DOWNROWS, ExcelHeaderConstantDTO.HE_TRANSFORMER_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.HE_TRANSFORMER_TYPE);
            case "3":
                filePath += "线路改造项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.LINE_HEADER, ExcelHeaderConstantDTO.LINE_DOWNROWS, ExcelHeaderConstantDTO.LINE_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.LINE_TYPE);
            case "4":
                filePath += "升压改造项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.BOOST_HEADER, ExcelHeaderConstantDTO.BOOST_DOWNROWS, ExcelHeaderConstantDTO.BOOST_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.BOOST_TYPE);
            case "5":
                filePath += "电机系统节能项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.ELE_MACHINERY_HEADER, ExcelHeaderConstantDTO.ELE_MACHINERY_DOWNROWS, ExcelHeaderConstantDTO.ELE_MACHINERY_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.ELE_MACHINERY_TYPE);
            case "6":
                filePath += "中央空调余热回收项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.WASTE_RECOVERY_HEADER, ExcelHeaderConstantDTO.WASTE_RECOVERY_DOWNROWS, ExcelHeaderConstantDTO.WASTE_RECOVERY_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.WASTE_RECOVERY_TYPE);
            case "7":
                filePath += "中央空调系统控制节能及中央空调过渡季冷却水制冷项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.VRV_HEADER, ExcelHeaderConstantDTO.VRV_DOWNROWS, ExcelHeaderConstantDTO.VRV_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.VRV_TYPE);
            case "8":
                filePath += "绿色照明项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.LIGHTING_HEADER, ExcelHeaderConstantDTO.LIGHTING_DOWNROWS, ExcelHeaderConstantDTO.LIGHTING_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.LIGHTING_TYPE);
            case "9":
                filePath += "水（地）源热泵项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.HEAT_PUMP_HEADER, ExcelHeaderConstantDTO.HEAT_PUMP_DOWNROWS, ExcelHeaderConstantDTO.HEAT_PUMP_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.HEAT_PUMP_TYPE);
            case "10":
                filePath += "电蓄冷（热）项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.ELE_STORAGE_HEADER, ExcelHeaderConstantDTO.ELE_STORAGE_DOWNROWS, ExcelHeaderConstantDTO.ELE_STORAGE_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.ELE_STORAGE_TYPE);
            case "11":
                filePath += "燃煤工业锅炉分层燃烧项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.COAL_HEADER, ExcelHeaderConstantDTO.COAL_DOWNROWS, ExcelHeaderConstantDTO.COAL_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.COAL_TYPE);
            case "12":
                filePath += "燃气锅炉冷凝式余热回收项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.GAS_HEADER, ExcelHeaderConstantDTO.GAS_DOWNROWS, ExcelHeaderConstantDTO.GAS_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.GAS_TYPE);
            case "13":
                filePath += "资源利用发电项目" + fileName;
                return generateTemplate(filePath, userInfo, ExcelHeaderConstantDTO.RESOURCE_HEADER, ExcelHeaderConstantDTO.RESOURCE_DOWNROWS, ExcelHeaderConstantDTO.RESOURCE_METHOTYPE, projectCategory, ExcelHeaderConstantDTO.RESOURCE_TYPE);

        }
        return null;
    }

    @Override
    public JSONResponseBody batchSelfAdd(JSONObject json, S_USER_INFO userInfo) throws ClassNotFoundException {
        JSONResponseBody result = new JSONResponseBody();
        String message = "";
        int add = 0;
        String projectCategory = json.getString("projectCategory");
        HashMap<String, String> entityMap = commonService.getEntityNames("0", projectCategory);
        String clazzName = entityMap.get("CLASS_NAME");
        JSONObject data = json.getJSONObject("data");
        List<JSONObject> list = data.getObject("resultArr", List.class);
        List<JSONObject> baseList = data.getObject("baseArr", List.class);

        if (!CollectionUtils.isEmpty(baseList)) {//校验数据库、excel数据重复
            List<Map<String, String>> repeats = mapper.repeatedVerificate(baseList, SelfSaveBase.class);
            if (!CollectionUtils.isEmpty(repeats)) {
                message += "项目名称为：" + StringUtils.join(repeats.stream().map(x -> x.get("PROJECT_NAME")).collect(Collectors.toList()), "、") + " 的数据已存在或excel中存在重复数据，";
                message += "无法导入";
                return JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, message);
            }
        }
        // 判断是否已上报
        for (int i = 0;i<baseList.size();i++){
            JSONObject item = baseList.get(i);
            item.put("projectSide","0");
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
            item.put("guid", CommonUtils.createUUID());
        });
//        baseList.forEach(item -> {
//
//            item.put("operator", userInfo.getUserName());
//            item.put("operateDate", CommonUtils.createCurrentTimeStr());
//            item.put("guid", CommonUtils.createUUID());
//            item.put("operatorCompany", userInfo.getOrgCode());
//            item.put("dataState", "0");
//        });

        add += commonService.batchInsertCategoryInfo(SelfSaveBase.class.getName(), baseList, true);
        add += commonService.batchInsertCategoryInfo(clazzName, list, true);
        if (add > 0) {
            result = JSONResponseBody.createSuccessResponseBody("导入成功", null);
        } else {
            result = JSONResponseBody.createFailResponseBody(BaseCode.BUSINESS_ERROR, "导入失败");
        }
        return result;
    }

    private File generateTemplate(String filePath, S_USER_INFO userInfo, String[] header, String[] downRows, String[] methoType, String projectCategory, JSONArray arr) {
        List<String[]> downData = commonService.getDownData(methoType, userInfo, projectCategory);
        return ExcelUtil.createExcelTemplate(filePath, header, downData, downRows, arr);
    }


    @Override
    public <M extends BaseBusinessMapper> M getMapper() {
        return (M) mapper;
    }
}
