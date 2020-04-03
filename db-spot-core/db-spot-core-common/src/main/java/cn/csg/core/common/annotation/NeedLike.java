package cn.csg.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询操作使用的注解
 * 是否该字段需要模糊查询
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NeedLike {

    /**
     * 开头是否模糊匹配
     *
     * @return
     */
    public boolean PREFIX() default true;

    /**
     * 结尾是否模糊匹配
     *
     * @return
     */
    public boolean SUFFIX() default true;
}
