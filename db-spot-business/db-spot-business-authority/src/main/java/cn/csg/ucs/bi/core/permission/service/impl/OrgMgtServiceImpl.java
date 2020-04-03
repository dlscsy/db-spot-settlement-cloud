package cn.csg.ucs.bi.core.permission.service.impl;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.common.entity.S_ORG_CODE_INFO;
import cn.csg.ucs.bi.core.permission.mapper.OrgMgtMapper;
import cn.csg.ucs.bi.core.permission.service.OrgMgtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orgMgtService")
public class OrgMgtServiceImpl extends AbstractBaseBusinessService<S_ORG_CODE_INFO> implements OrgMgtService {

    @Autowired
    private OrgMgtMapper mapper;

    @Override
    public <M extends BaseBusinessMapper> M getMapper() {
        return (M) mapper;
    }
}
