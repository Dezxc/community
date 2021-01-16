package com.zhu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注解  在需要登录的controller方法加该注解
 * 拦截器中拦截如果方法有该注解，则判断是否登录，没有登录则拦截
 */

@Target(ElementType.METHOD)   //在方法上使用
@Retention(RetentionPolicy.RUNTIME)         //作用范围：运行时
public @interface LoginRequired {
}
