package cn.csg.core.common.mapper.provider;

import cn.csg.core.common.structure.TableMeta;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.util.StringUtils;

public class CustomBaseSelectProvider extends BaseProvider {

    /**
     * 根据主键获取数据
     *
     * @param context
     * @param primaryKey
     * @return
     */
    public String customQueryByPrimaryKey(ProviderContext context, String primaryKey) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        if (StringUtils.isEmpty(tableMeta.getPkColumn())) {
            throw new NullPointerException(entityClass.getName() + "实体类没有用@Id注解指明主键");
        }
        StringBuffer conditions = new StringBuffer(" WHERE " + tableMeta.getTableAliasName() + "." + tableMeta.getPkColumn() + " = '" + primaryKey + "'");
        return this.selectSQLWrapper(entityClass, tableMeta, conditions.toString());
    }

    /**
     * 根据主键删除数据
     *
     * @param context
     * @param primaryKey
     * @return
     */
    public String customDelByPrimaryKey(ProviderContext context, String primaryKey) {
        Class<?> entityClass = this.getEntityClass(context);
        TableMeta tableMeta = TableMeta.forClass(entityClass);
        if (StringUtils.isEmpty(tableMeta.getPkColumn())) {
            throw new NullPointerException(entityClass.getName() + "实体类没有用@Id注解指明主键");
        }
        StringBuffer conditions = new StringBuffer(" WHERE " + tableMeta.getTableAliasName() + "." + tableMeta.getPkColumn() + " = '" + primaryKey + "'");
        return this.delSQLWrapper(tableMeta, conditions.toString());
    }
}
