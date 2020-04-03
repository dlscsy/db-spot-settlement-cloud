package cn.csg.ucs.bi.common.service;

import cn.csg.ucs.bi.common.entity.S_FILE_INFO;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.structure.TreeNode;

import java.util.List;

public interface FileMgtService {

    /**
     * 获取组织机构树
     *
     * @param parentZzbm 父节点ID
     * @return 下拉树统一结构封装体
     */
    List<TreeNode> getTreeZzjgs(String parentZzbm);

    /**
     * 获取附件数据信息
     *
     * @param orgCode
     * @param projectSide
     * @param projectCategory
     * @param fileName
     * @return
     */
    List<S_FILE_INFO> getFileInfo(String orgCode, String projectSide,String projectCategory,String projectId, String fileName);

    /**
     * 新增附件数据信息
     *
     * @param obj
     * @return
     */
    int addFileInfo(S_FILE_INFO obj);

    /**
     * 删除附件数据信息
     *
     * @param keys
     * @return
     */
    int removeFileInfo(String keys);

    /**
     * 根据项目id获取所选数据的附件数据信息
     *
     * @param projectId
     * @return
     */
    List<S_FILE_INFO> getAllFileInfoByOrgCode(String projectId);

    // 获取组织机构
    S_ORG_CODE_INFO getOrgByOrgCode(String orgCode);

}
