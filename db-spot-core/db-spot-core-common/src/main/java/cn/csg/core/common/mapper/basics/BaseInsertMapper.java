package cn.csg.core.common.mapper.basics;

import cn.csg.core.common.mapper.defined.CustomInsertMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;

public interface BaseInsertMapper<T> extends Marker,
        InsertMapper<T>,
        InsertSelectiveMapper<T>,
        MySqlMapper<T>,
        CustomInsertMapper<T> {
}
