package com.fmx.xiaomeng.modules.application.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要登录鉴权的方法。
 * 原理说明：由拦截器在请求到达控制器方法前检查该注解，
 * 若标记存在则进行令牌解析与用户身份校验。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin {
}
