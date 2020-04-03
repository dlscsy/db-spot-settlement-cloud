package cn.csg.core.common.annotation;

import cn.csg.core.common.enums.ECompareType;
import cn.csg.core.common.enums.ELowLimitOperator;
import cn.csg.core.common.enums.EUpLimitOperator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用Mapper使用的注解
 * 用于标识该字段为数字类型
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberField {

    /**
     * 小数点保留位数
     *
     * @return
     */
    public int SCALE() default 2;

    /**
     * 查询时条件是否是范围数组
     *
     * @return
     */
    public boolean NEEDDATERANGE() default false;

    /**
     * 比较方式
     *
     * @return
     */
    public ECompareType COMPARETYPE() default ECompareType.EQUAL;

    /**
     * 上限的运算符：<=或者<
     *
     * @return
     */
    public EUpLimitOperator UP_LIMIT_OPERATOR() default EUpLimitOperator.LESS_THAN_OR_EQUAL;

    /**
     * 下限的运算符：>=或者>
     *
     * @return
     */
    public ELowLimitOperator LOW_LIMIT_OPERATOR() default ELowLimitOperator.GREATER_THAN_OR_EQUAL;
}
