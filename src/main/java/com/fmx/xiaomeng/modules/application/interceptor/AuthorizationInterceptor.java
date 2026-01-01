package com.fmx.xiaomeng.modules.application.interceptor;


import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    public static final String USER_KEY = "userId";
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //带login注解接口的是需要获取用户信息才能访问的，同时只有带这个注解才在request保存userId，供loginUser注解使用，
        // 如果没有登录（没有token）的情况下访问带了Login注解的接口，则报错，没有@Login注解的接口随便访问

        // TODO: 2023/2/26

        NeedLogin annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(NeedLogin.class);
        } else {
            return true;
        }

//        log.info("annotation!!!", annotation);

//        log.info("参数HttpServletRequest!!!", request.getServerName());  //这里取不到

        if (annotation == null) {
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());

        //凭证为空
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN, "请登录");
        }

        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN, "请登录");
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));
        return true;
    }
}
