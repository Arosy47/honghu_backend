package com.fmx.xiaomeng.common.exception;

import com.fmx.xiaomeng.common.log.AbstractLogCodeException;
import com.fmx.xiaomeng.common.log.LogCode;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public class BusinessException extends AbstractLogCodeException {
    public BusinessException(Throwable cause, LogCode errorCode) {
        super(cause, errorCode);
    }

    public BusinessException(Throwable cause, LogCode errorCode, String format, Object... arguments) {
        super(cause, errorCode, format, arguments);
    }

    public BusinessException(LogCode errorCode, String format, Object... arguments) {
        super(errorCode, format, arguments);
    }

    public BusinessException(LogCode errorCode, ErrorAttributes errorAttributes, String format, Object... arguments) {
        super(errorCode, errorAttributes, format, arguments);
    }



}
