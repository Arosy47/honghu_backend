package com.fmx.xiaomeng.modules.systemsetting.aspect;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.log.Logger;
import com.fmx.xiaomeng.common.log.LoggerFactory;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import org.apache.shiro.authz.AuthorizationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Aspect
@Component
public class ControllerInvocationAspect {

    private final static Logger log = LoggerFactory.getLogger(ControllerInvocationAspect.class);

    @Around("execution(* *..controller..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            String method = joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName();

            Object[] args = joinPoint.getArgs();
            String params = "";
            if (!(args == null || args.length == 0)) {
                List<Object> realArgs = Arrays.stream(args).filter(arg -> !(arg instanceof UserModel || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse))
                        .collect(Collectors.toList());
                params = JSON.toJSONString(realArgs);
            }

            log.exception(ex, "{}, params:{}", method, params);

            Map<String, Object> responseData = new HashMap<>();
            if (ex instanceof BusinessException) {
                BusinessException businessException = (BusinessException) ex;
                if (ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED.equals(businessException.getErrorCode())) {
                    responseData.put("errCode", businessException.getErrorCode().code());
                    responseData.put("errMsg", "请重试");
                } else {
                    responseData.put("errCode", businessException.getErrorCode().code());
                    responseData.put("errMsg", businessException.getErrorCode().message());
                }
            } else if (ex instanceof ServletRequestBindingException) {
                responseData.put("errCode", ErrorCodeEnum.UNKNOWN_ERROR.code());
                responseData.put("errMsg", "url绑定路由问题");
            } else if (ex instanceof NoHandlerFoundException) {
                responseData.put("errCode", ErrorCodeEnum.UNKNOWN_ERROR.message());
                responseData.put("errMsg", "没有找到对应的访问路径");
            } else if (ex instanceof AuthorizationException) {
                responseData.put("errCode", ErrorCodeEnum.NO_AUTH.code());
                responseData.put("errMsg", ErrorCodeEnum.NO_AUTH.message());
            } else {
                responseData.put("errCode", ErrorCodeEnum.UNKNOWN_ERROR.code());
                responseData.put("errMsg", ex.getMessage());
            }
            return Result.error(500, responseData);
        }
    }

}
