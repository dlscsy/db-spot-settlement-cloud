package cn.csg.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用Mapper使用的注解
 * 用于设置主表的别名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SetAlias {

    /**
     * 别名
     *
     * @return
     */
    public String value();
}
