package cn.csg.ucs.bi.business.service.require;

import cn.csg.ucs.bi.base.structure.JSONResponseBody;
import cn.csg.ucs.bi.business.entity.helper.SelfSaveBase;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;

public interface SProjectMgtService {

    /**
     * 获取电网自身节电量节电力项目信息
     *
     * @param jo
     * @return
     */
    List<SelfSaveBase> getSProjectInfo(JSONObject jo);

    /**
     * 更新电网自身节电量节电力项目分类信息
     *
     * @param jo
     * @param className
     * @return
     */
    int updateSubInfos(JSONObject jo, String className);

    /**
     * 删除电网自身节电量节电力项目信息
     *
     * @param tableName
     * @param keys
     * @return
     */
    int delSProjectInfo(String tableName, String keys);

    /**
     * 删除电网自身节电量节电力项目分类信息
     *
     * @param tableName
     * @param keys
     * @return
     */
    int delSubInfos(String tableName, String keys);

    File generateSelfTemplate(String fileName, S_USER_INFO userInfo, String projectCategory);

    JSONResponseBody batchSelfAdd(JSONObject json, S_USER_INFO userInfo) throws ClassNotFoundException;
}
