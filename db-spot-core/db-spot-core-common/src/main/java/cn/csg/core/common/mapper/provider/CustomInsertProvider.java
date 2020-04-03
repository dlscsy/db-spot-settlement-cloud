package cn.csg.core.common.mapper.provider;

import cn.csg.core.common.mapper.helper.CustomInsertSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.builder.annotation.ProviderContext;

public class CustomInsertProvider extends BaseProvider {

    public String customInsert(ProviderContext context, JSONObject jo) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return CustomInsertSQLHelper.getInsertSQL(jo, tableMeta);
    }
}
