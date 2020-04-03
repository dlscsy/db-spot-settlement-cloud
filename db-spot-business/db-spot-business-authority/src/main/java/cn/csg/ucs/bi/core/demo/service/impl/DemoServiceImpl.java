package cn.csg.ucs.bi.core.demo.service.impl;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.core.demo.entity.Demo;
import cn.csg.ucs.bi.core.demo.mapper.DemoMapper;
import cn.csg.ucs.bi.core.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl extends AbstractBaseBusinessService<Demo> implements DemoService {

    @Autowired
    private DemoMapper mapper;

    @Override
    public <M extends BaseBusinessMapper> M getMapper() {
        return (M) mapper;
    }
}
