package com.fmx.xiaomeng.modules.systemsetting.annotation;

import java.lang.annotation.*;

/**
 * 系统操作日志注解
 *
 * @author honghu
 * @date 2025-12-20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
