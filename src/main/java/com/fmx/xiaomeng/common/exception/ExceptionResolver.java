package com.fmx.xiaomeng.common.exception;

import com.fmx.xiaomeng.common.log.AbstractLogCodeException;
import com.fmx.xiaomeng.common.log.LogCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @Description 异常解析器
 * @Date 2025-12-20
 * @Author honghu
 **/
@Getter
public class ExceptionResolver {
    /**
     * 原始异常
     */
    private Throwable origin;
    /**
     * 目标异常
     */
    private Throwable target;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 错误码
     */
    private LogCode errorCode;
    /**
     * 错误属性
     */
    AbstractLogCodeException.ErrorAttributes errorAttributes;

    private ExceptionResolver(Throwable throwable, LogCode defaultErrorCode){
        this.origin = throwable;
        this.errorCode = defaultErrorCode;
        this.resolve();
    }


    public static ExceptionResolver resolve(Throwable throwable, @NotNull LogCode defaultErrorCode){
        return new ExceptionResolver(throwable,defaultErrorCode);
    }

    public static LogCode resolveErrorCode(Throwable throwable, @NotNull LogCode defaultErrorCode){
        ExceptionResolver resolve = resolve(throwable, defaultErrorCode);
        return resolve.getErrorCode();
    }



    private void resolve() {
        Throwable target = origin;
        while (target instanceof InvocationTargetException){
            //去反射异常wrap，获取真实异常
            InvocationTargetException targetException = (InvocationTargetException)target;
            target = targetException.getTargetException();
        }
        this.target = target;
        this.errorMsg = Optional.ofNullable(target.getMessage()).orElse(target.getClass().getName());
        if(target instanceof AbstractLogCodeException){
            //解析自定义异常
            AbstractLogCodeException errorCodeException = (AbstractLogCodeException)target;
            this.errorCode = errorCodeException.getErrorCode();
            this.errorAttributes = errorCodeException.getErrorAttributes();
        }
    }


    public AbstractLogCodeException decorateException(String format, Object... arguments){
        if(this.target instanceof AbstractLogCodeException){
            AbstractLogCodeException errorCodeException = (AbstractLogCodeException)this.target;
            errorCodeException.appendMessage(format, arguments);
            return errorCodeException;
        }
        return new BusinessException(target, errorCode,format,arguments);
    }


    public com.fmx.xiaomeng.common.log.LogCode getErrorCode() { return errorCode; }
}
