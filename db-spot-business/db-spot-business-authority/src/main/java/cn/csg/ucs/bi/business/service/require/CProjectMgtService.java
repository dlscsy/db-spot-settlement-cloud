package cn.csg.ucs.bi.business.service.require;

import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CProjectMgtService {

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author chengzhifeng
     * @Description 获取节能服务
     * @Date 10:25 2019/11/27
     * @Param [json]
     **/
    public JSONObject getSaveServiceInfo(JSONObject json);

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author chengzhifeng
     * @Description 获取节能服务
     * @Date 14:54 2019/11/27
     * @Param [json]
     **/
    JSONObject getDiagnosisInfo(JSONObject json);

    /**
     * @return java.util.List<cn.csg.ucs.bi.business.entity.B_HL_TRANSFORMER>
     * @Author chengzhifeng
     * @Description 获取高损变压器项目信息
     * @Date 16:15 2019/11/27
     * @Param [json]
     **/
    JSONObject getHLTransformer(JSONObject json);

    /**
     * @return java.util.List
     * @Author chengzhifeng
     * @Description 获取推广LED信息
     * @Date 19:52 2019/11/27
     * @Param [json]
     **/
    JSONObject getPromoteLEDInfo(JSONObject json);

    JSONObject getIncentiveInfo(JSONObject json);

    JSONObject getClientSideInfo(JSONObject json);

    JSONObject getContractInfo(JSONObject json);

    JSONObject getUnContractInfo(JSONObject json);

    int addObject(JSONObject json, S_USER_INFO userInfo);

    List<Map<String, String>> queryExistsForValidate(JSONObject json);

    int delByPrimaryKeys(JSONObject json);

    int updateObject(JSONObject json, S_USER_INFO userInfo);

    File generateTemplate(String fileName, S_USER_INFO userInfo, String projectCategory);

    JSONResponseBody batchAdd(JSONObject json, S_USER_INFO userInfo) throws ClassNotFoundException;

}
