package cn.csg.ucs.bi.core.permission.controller;

import cn.csg.ucs.bi.base.controller.AbstractBaseBusinessController;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Transactional
@RestController
@RequestMapping("/userMgtController")
public class UserMgtController extends AbstractBaseBusinessController<S_USER_INFO> {

    @Autowired
    @Qualifier("userMgtService")
    private UserMgtService service;

    @Override
    public <S extends AbstractBaseBusinessService> S getService() {
        return (S) service;
    }
}