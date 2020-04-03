package cn.csg.core.common.mapper.provider;

import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.builder.annotation.ProviderContext;

public class CustomConditionProvider extends BaseProvider {

    /**
     * 根据条件获取数据
     * 支持模糊查询，区间范围查询（>=，<=，<，>，=），IN查询
     *
     * @param context
     * @param jo
     * @return
     */
    public String customQueryByConditions(ProviderContext context, JSONObject jo) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return this.selectSQLWrapper(entityClass, tableMeta, CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
    }

    /**
     * 根据条件删除数据
     * 支持模糊匹配，区间范围匹配（>=，<=，<，>，=），IN匹配
     *
     * @param context
     * @param jo
     * @return
     */
    public String customDelByConditions(ProviderContext context, JSONObject jo) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return this.delSQLWrapper(tableMeta, CustomConditionsSQLHelper.getConditionsSQL(jo, tableMeta));
    }

    /**
     * 根据条件获取已存在的数据
     *
     * @param context
     * @param jo
     * @return
     */
    public String customQueryExistsForValidate(ProviderContext context, JSONObject jo) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        return this.selectExistSQLWrapper(tableMeta, jo);
    }
}
