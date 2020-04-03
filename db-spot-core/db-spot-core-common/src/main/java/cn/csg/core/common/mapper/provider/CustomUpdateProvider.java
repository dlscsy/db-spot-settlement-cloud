package cn.csg.core.common.mapper.provider;

import cn.csg.core.common.mapper.helper.CustomUpdateSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.builder.annotation.ProviderContext;

public class CustomUpdateProvider extends BaseProvider{

    public String customUpdate(ProviderContext context, JSONObject jo) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return CustomUpdateSQLHelper.getUpdateSQL(jo, tableMeta);
    }
}
