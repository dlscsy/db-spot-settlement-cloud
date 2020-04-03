package cn.csg.core.common.annotation;

import cn.csg.core.common.structure.TableMeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用Mapper使用的注解，但多用于保存
 * 用于标识该字段取值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSetting {

    /**
     * 默认值
     *
     * @return
     */
    public String DEFAULT_VALUE() default "null";

    /**
     * 是否该字段所持久化的数据为复选数据
     *
     * @return
     */
    public boolean USECHECKBOX() default false;

    /**
     * 复选数据保存在数据库中使用的分隔符
     *
     * @return
     */
    public String SPLIT() default TableMeta.DEFAULT_CHECK_BOX_VALUES_SPLIT;
}
