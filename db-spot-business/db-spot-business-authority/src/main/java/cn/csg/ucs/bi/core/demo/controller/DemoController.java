package cn.csg.ucs.bi.core.demo.controller;

import cn.csg.ucs.bi.base.controller.AbstractBaseBusinessController;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.core.demo.entity.Demo;
import cn.csg.ucs.bi.core.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/demoController")
public class DemoController extends AbstractBaseBusinessController<Demo> {

    @Autowired
    @Qualifier("demoService")
    private DemoService service;

    @Override
    public <S extends AbstractBaseBusinessService> S getService() {
        return (S) service;
    }
}
