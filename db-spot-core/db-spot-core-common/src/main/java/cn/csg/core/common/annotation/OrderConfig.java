package cn.csg.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询操作使用的注解
 * 用于设置排序
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderConfig {

    /**
     * 排序字段
     *
     * @return
     */
    public String FIELDS() default "";

    /**
     * 排序方式：DESC,ASC
     *
     * @return
     */
    public String ORDERTYPE() default "DESC";
}
