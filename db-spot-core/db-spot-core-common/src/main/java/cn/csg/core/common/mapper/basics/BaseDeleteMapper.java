package cn.csg.core.common.mapper.basics;

import cn.csg.core.common.mapper.defined.CustomDeleteMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.delete.DeleteMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

public interface BaseDeleteMapper<T> extends Marker,
        DeleteMapper<T>,
        DeleteByPrimaryKeyMapper<T>,
        DeleteByConditionMapper<T>,
        DeleteByIdsMapper<T>,
        CustomDeleteMapper<T> {
}
