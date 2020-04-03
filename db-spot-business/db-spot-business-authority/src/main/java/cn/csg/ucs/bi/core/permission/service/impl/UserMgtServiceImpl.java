package cn.csg.ucs.bi.core.permission.service.impl;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.ucs.bi.base.service.AbstractBaseBusinessService;
import cn.csg.ucs.bi.common.entity.S_USER_INFO;
import cn.csg.ucs.bi.core.permission.mapper.UserMgtMapper;
import cn.csg.ucs.bi.core.permission.service.UserMgtService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userMgtService")
public class UserMgtServiceImpl extends AbstractBaseBusinessService<S_USER_INFO> implements UserMgtService {

    @Autowired
    private UserMgtMapper mapper;

    @Override
    public <M extends BaseBusinessMapper> M getMapper() {
        return (M) mapper;
    }

    @Override
    public S_USER_INFO getUserByAccount(String account) {
        JSONObject jo = new JSONObject();
        jo.put("account", account);
        return mapper.customQueryByConditions(jo).get(0);
//        return mapper.getUserByAccount(jo).get(0);
    }

    @Override
    public int updateUserInfoAfterLoginSuccess(String account, String token, String tokenExpDate, String operator, String operateDate, String operatorCompany) {
        return mapper.updateUserInfoAfterLoginSuccess(account, token, tokenExpDate, operator, operateDate, operatorCompany);
    }

    @Override
    public int updateUserInfoAfterLogOutSuccess(String account, String operator, String operateDate, String operatorCompany) {
        return mapper.updateUserInfoAfterLogOutSuccess(account, operator, operateDate, operatorCompany);
    }

    @Override
    public int updateTokenExpDate(String tokenExpDate, String account, String operator, String operateDate, String operatorCompany) {
        return mapper.updateTokenExpDate(tokenExpDate, account, operator, operateDate, operatorCompany);
    }

    @Override
    public int changePassword(String account,String newPassword) {
        return mapper.changePassword(account,newPassword);
    }

    @Override
    public int updateWrongNum(String account, String wrongNum, String updater, String updateDate) {
        return mapper.updateWrongNum(account, wrongNum, updater, updateDate);
    }

    @Override
    public String getWrongNumByAccount(String account) {
        return mapper.getWrongNumByAccount(account);
    }

    @Override
    public int lockedUser(String account, String updater, String updateDate) {
        return mapper.lockedUser(account, updater, updateDate);
    }

    @Override
    public int unlockedUser(String account, String updater, String updateDate) {
        return mapper.unlockedUser(account, updater, updateDate);
    }
}