package cn.csg.core.common.annotation;

import cn.csg.core.common.structure.TableMeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用Mapper使用的注解
 * 用于标识该字段是否需要映射数据字典
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DictionarieField {

    /**
     * 附加条件
     *
     * @return
     */
    public String ADDITIONAL() default "";

    /**
     * 自定义sql进行关联
     *
     * @return
     */
    public String SQL() default "";

    /**
     * 映射关联的表名
     *
     * @return
     */
    public String TABLE_NAME() default "";

    /**
     * 映射关联的表的别名
     *
     * @return
     */
    public String TABLE_ALIAS_NAME() default TableMeta.DEFAULT_RELATION_TABLE_ALIAS_NAME;

    /**
     * 关联字段
     *
     * @return
     */
    public String RELATIONFIELD() default "";

    /**
     * 映射字段
     *
     * @return
     */
    public String MAPPINGFIELD() default "";

    /**
     * 映射值对应的实体类属性
     *
     * @return
     */
    public String PROPERTY();

    /**
     * 默认的映射关联的列的别名的后缀
     */
    public String RELATION_COLUMN_ALIAS_NAME_SUFFIX() default TableMeta.DEFAULT_RELATION_COLUMN_ALIAS_NAME_SUFFIX;
}
