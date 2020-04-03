package cn.csg.ucs.bi.common.service.impl;

import cn.csg.ucs.bi.common.entity.S_FILE_INFO;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.common.mapper.FileMgtMapper;
import cn.csg.ucs.bi.common.service.FileMgtService;
import cn.csg.ucs.bi.common.structure.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fileMgtService")
public class FileMgtServiceImpl implements FileMgtService {
    @Autowired
    private FileMgtMapper mapper;

    @Override
    public List<TreeNode> getTreeZzjgs(String parentZzbm) {
        return mapper.getTreeZzjgs(parentZzbm);
    }

    @Override
    public List<S_FILE_INFO> getFileInfo(String orgCode, String projectSide,String projectCategory,String projectId, String fileName) {
        return mapper.getFileInfo(orgCode, projectSide,projectCategory,projectId, fileName);
    }

    @Override
    public int addFileInfo(S_FILE_INFO obj) {
        return mapper.addFileInfo(obj);
    }

    @Override
    public int removeFileInfo(String keys) {
        return mapper.removeFileInfo(keys);
    }

    @Override
    public List<S_FILE_INFO> getAllFileInfoByOrgCode(String projectId) {
        return mapper.getAllFileInfoByOrgCode(projectId);
    }

    @Override
    public S_ORG_CODE_INFO getOrgByOrgCode(String orgCode) {
        return mapper.getOrgByOrgCode(orgCode);
    }

}
