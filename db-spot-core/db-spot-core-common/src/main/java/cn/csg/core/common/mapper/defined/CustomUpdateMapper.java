package cn.csg.core.common.mapper.defined;

import cn.csg.core.common.mapper.provider.CustomUpdateProvider;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CustomUpdateMapper<T> {

    @UpdateProvider(type = CustomUpdateProvider.class, method = "customUpdate")
    int customUpdate(JSONObject jo);
}
