package cn.csg.core.common.mapper.defined;

import cn.csg.core.common.mapper.provider.CustomConditionProvider;
import cn.csg.core.common.mapper.provider.CustomBaseSelectProvider;
import cn.csg.core.common.mapper.provider.CustomIdsProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface CustomSelectMapper<T> {

    @SelectProvider(type = CustomConditionProvider.class, method = "customQueryByConditions")
    List<T> customQueryByConditions(JSONObject jo);

    @SelectProvider(type = CustomBaseSelectProvider.class, method = "customQueryByPrimaryKey")
    T customQueryByPrimaryKey(String primaryKey);

    @SelectProvider(type = CustomIdsProvider.class, method = "customQueryByPrimaryKeys")
    List<T> customQueryByPrimaryKeys(String primaryKeys);

    @SelectProvider(type = CustomConditionProvider.class, method = "customQueryExistsForValidate")
    List<T> customQueryExistsForValidate(JSONObject jo);
}
