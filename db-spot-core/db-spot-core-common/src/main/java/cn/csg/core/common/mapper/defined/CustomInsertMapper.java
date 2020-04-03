package cn.csg.core.common.mapper.defined;

import cn.csg.core.common.mapper.provider.CustomInsertProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.InsertProvider;

public interface CustomInsertMapper<T> {

    @InsertProvider(type = CustomInsertProvider.class, method = "customInsert")
    int customInsert(JSONObject jo);
}
