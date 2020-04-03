package cn.csg.ucs.bi.base.service;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public abstract class AbstractBaseBusinessService<T> {

    public List<T> queryByConditions(JSONObject jo) {
        return getMapper().customQueryByConditions(jo);
    }

    public int add(JSONObject jo) {
        return getMapper().customInsert(jo);
    }

    public int update(JSONObject jo) {
        return getMapper().customUpdate(jo);
    }

    public int del(String primaryKeys) {
        return getMapper().customDelByPrimaryKeys(primaryKeys);
    }

    public List<T> queryExistsForValidate(JSONObject jo) {
        return getMapper().customQueryExistsForValidate(jo);
    }

    public abstract <M extends BaseBusinessMapper> M getMapper();
}
