package com.fmx.xiaomeng.modules.application.controller;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.response.Result;
import lombok.CustomLog;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author honghu
 */
@ControllerAdvice
@CustomLog
public class GlobalExceptionHandler {
    /**
     * 定义exceptionhandler解决未被controller层吸收的exception
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Result<Map<String, Object>> doError(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String serverName = request.getServerName(); //域名或IP
        String requestURI = request.getRequestURI();

        String url = serverName + requestURI;

        log.error("URL:{}, param:{}", url, JSON.toJSONString(parameterMap), ex);

        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrorCode().code());
            responseData.put("errMsg", businessException.getErrorCode().message());
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

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);
}
