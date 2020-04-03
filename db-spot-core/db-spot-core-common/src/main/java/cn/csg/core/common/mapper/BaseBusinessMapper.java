package cn.csg.core.common.mapper;

import cn.csg.core.common.mapper.basics.BaseDeleteMapper;
import cn.csg.core.common.mapper.basics.BaseInsertMapper;
import cn.csg.core.common.mapper.basics.BaseSelectMapper;
import cn.csg.core.common.mapper.basics.BaseUpdateMapper;

public interface BaseBusinessMapper<T> extends
        BaseDeleteMapper<T>,
        BaseInsertMapper<T>,
        BaseSelectMapper<T>,
        BaseUpdateMapper<T> {
}
