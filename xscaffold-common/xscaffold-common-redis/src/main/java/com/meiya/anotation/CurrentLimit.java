package com.meiya.anotation;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaopf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CurrentLimit {

    String key() default "current:limit";
    /**
     * 单位时间的限流次数
     * @return 默认100次
     */
    long limitPerUnitTime() default 100;

    /**
     * 限流计算的单位时间
     * @return 默认1s
     */
    long unitTime() default 1;

    /**
     * 限流后返回信息
     * @return 默认信息
     */
    String message() default "系统繁忙,请稍后再试";



}
