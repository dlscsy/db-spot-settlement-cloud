package cn.csg.core.common.mapper.provider;

import cn.csg.core.common.mapper.BaseBusinessMapper;
import cn.csg.core.common.mapper.helper.CustomColumnsSQLHelper;
import cn.csg.core.common.mapper.helper.CustomConditionsSQLHelper;
import cn.csg.core.common.structure.TableMeta;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;

public abstract class BaseProvider {

    /**
     * 获取泛型对应的实体类
     *
     * @param context
     * @return
     */
    public Class<?> getEntityClass(ProviderContext context) {
        Class<?> mapperType = context.getMapperType();
        for (Type parent : mapperType.getGenericInterfaces()) {
            ResolvableType parentType = ResolvableType.forType(parent);
            if (parentType.getRawClass() == BaseBusinessMapper.class) {
                return parentType.getGeneric(0).getRawClass();
            }
        }
        return null;
    }

    /**
     * 构建查询操作的SQL语句
     *
     * @param entityClass
     * @param tableMeta
     * @param conditions
     * @return
     */
    public String selectSQLWrapper(Class<?> entityClass, TableMeta tableMeta, String conditions) {
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(CustomColumnsSQLHelper.getBaseColumnsSQL(tableMeta));
        sql.append(CustomColumnsSQLHelper.getDictionarieColumnsSQL(tableMeta));
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(conditions);
        sql.append(CustomConditionsSQLHelper.getOrderSQL(entityClass, tableMeta));
        return sql.toString();
    }

    /**
     * 构建查询操作的SQL语句（用于获取已存在的数据）
     *
     * @param tableMeta
     * @param jo
     * @return
     */
    public String selectExistSQLWrapper(TableMeta tableMeta, JSONObject jo) {
        StringBuffer sql = new StringBuffer("SELECT *");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(CustomConditionsSQLHelper.getConditionsForExistSQL(jo, tableMeta));
        return sql.toString();
    }

    /**
     * 构建删除操作的SQL语句
     *
     * @param tableMeta
     * @param conditions
     * @return
     */
    public String delSQLWrapper(TableMeta tableMeta, String conditions) {
        StringBuffer sql = new StringBuffer("DELETE");
        sql.append(" FROM " + tableMeta.getTableName() + " " + tableMeta.getTableAliasName());
        sql.append(conditions);
        return sql.toString();
    }
}
