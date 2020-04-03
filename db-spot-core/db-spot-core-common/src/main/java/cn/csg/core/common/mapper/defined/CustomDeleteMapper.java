package cn.csg.core.common.mapper.defined;

import cn.csg.core.common.mapper.provider.CustomBaseSelectProvider;
import cn.csg.core.common.mapper.provider.CustomConditionProvider;
import cn.csg.core.common.mapper.provider.CustomIdsProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.DeleteProvider;

public interface CustomDeleteMapper<T> {

    @DeleteProvider(type = CustomConditionProvider.class, method = "customDelByConditions")
    int customDelByConditions(JSONObject jo);

    @DeleteProvider(type = CustomBaseSelectProvider.class, method = "customDelByPrimaryKey")
    int customDelByPrimaryKey(String primaryKey);

    @DeleteProvider(type = CustomIdsProvider.class, method = "customDelByPrimaryKeys")
    int customDelByPrimaryKeys(String primaryKeys);
}
