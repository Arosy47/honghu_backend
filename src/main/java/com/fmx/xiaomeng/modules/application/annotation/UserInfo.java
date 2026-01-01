package com.fmx.xiaomeng.modules.application.annotation;

import java.lang.annotation.*;

/**
 * 参数级注解，用于在控制器方法中注入当前登录用户模型。
 * 原理说明：通过参数解析器在运行时读取请求上下文的用户标识，
 * 再由服务层查询并填充具体的用户模型对象。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInfo {

}
