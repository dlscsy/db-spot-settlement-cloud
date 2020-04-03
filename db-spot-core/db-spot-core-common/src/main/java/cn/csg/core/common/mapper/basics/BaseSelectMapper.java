package cn.csg.core.common.mapper.basics;

import cn.csg.core.common.mapper.defined.CustomSelectMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.select.*;
import tk.mybatis.mapper.common.condition.SelectByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

public interface BaseSelectMapper<T> extends Marker,
        SelectMapper<T>,
        SelectByConditionMapper<T>,
        SelectOneMapper<T>,
        SelectAllMapper<T>,
        SelectByPrimaryKeyMapper<T>,
        SelectByExampleMapper<T>,
        SelectCountByConditionMapper<T>,
        SelectCountMapper<T>,
        SelectByIdsMapper<T>,
        ExistsWithPrimaryKeyMapper<T>,
        CustomSelectMapper<T> {
}
