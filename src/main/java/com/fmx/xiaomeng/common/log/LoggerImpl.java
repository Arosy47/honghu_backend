package com.fmx.xiaomeng.common.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.util.StringJoiner;

import static org.slf4j.spi.LocationAwareLogger.*;

/**
 * 自定义日志实现
 *
 * @author honghu
 * @date 2025-12-20
 */
public class LoggerImpl implements Logger {

    /**
     * 日志门面类名
     */
    private static final String FQCN = LoggerImpl.class.getName();

    /**
     * 日志分隔符
     */
    private static final String LOG_SEPARATOR = " | ";

    /**
     * 业务摘要日志 logger name
     */
    private static final String BIZ_DIGEST_LOGGER_NAME = "bizDigestLog";

    /**
     * logger name
     */
    private final String name;

    /**
     * 实际日志代理
     */
    private final LocationAwareLogger delegate;

    /**
     * 业务摘要日志代理
     */
    private final LocationAwareLogger bizDigestLogger;

    public LoggerImpl(String name) {
        this.name = name;
        this.delegate = (LocationAwareLogger)LoggerFactory.getLogger(name);
        this.bizDigestLogger = (LocationAwareLogger)LoggerFactory.getLogger(BIZ_DIGEST_LOGGER_NAME);
    }

    /**
     * 格式化错误码日志
     *
     * @param logCode
     * @param format
     * @param argArray
     * @return
     */
    private FormattingTuple buildFormattingTuple(LogCode logCode, String format, Object... argArray) {
        StringJoiner joiner = new StringJoiner(LOG_SEPARATOR);
        if (null != logCode) {
            joiner.add(logCode.name())
                .add(logCode.message());
        }
        if (null != format) {
            joiner.add(format);
        }
        return MessageFormatter.arrayFormat(joiner.toString(), argArray);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        delegate.log(null, FQCN, TRACE_INT, msg, null, null);
    }

    @Override
    public void trace(String format, Object arg) {
        delegate.log(null, FQCN, TRACE_INT, format, new Object[] {arg}, null);
    }


    @Override
    public void trace(String format, Object arg1, Object arg2) {
        delegate.log(null, FQCN, TRACE_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void trace(String format, Object... arguments) {
        delegate.log(null, FQCN, TRACE_INT, format, arguments, null);
    }

    @Override
    public void trace(String msg, Throwable t) {
        delegate.log(null, FQCN, TRACE_INT, msg, null, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        delegate.log(marker, FQCN, TRACE_INT, msg, null, null);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        delegate.log(marker, FQCN, TRACE_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        delegate.log(marker, FQCN, TRACE_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        delegate.log(marker, FQCN, TRACE_INT, format, argArray, null);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        delegate.log(marker, FQCN, TRACE_INT, msg, null, t);
    }

    @Override
    public void trace(LogCode logCode, String format, Object... argArray) {
        FormattingTuple tuple = this.buildFormattingTuple(logCode, format, argArray);
        delegate.log(null, FQCN, TRACE_INT, tuple.getMessage(), null, tuple.getThrowable());
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        delegate.log(null, FQCN, DEBUG_INT, msg, null, null);
    }

    @Override
    public void debug(String format, Object arg) {
        delegate.log(null, FQCN, DEBUG_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        delegate.log(null, FQCN, DEBUG_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void debug(String format, Object... arguments) {
        delegate.log(null, FQCN, DEBUG_INT, format, arguments, null);
    }

    @Override
    public void debug(String msg, Throwable t) {
        delegate.log(null, FQCN, DEBUG_INT, msg, null, null);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        delegate.log(marker, FQCN, DEBUG_INT, msg, null, null);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        delegate.log(marker, FQCN, DEBUG_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        delegate.log(marker, FQCN, DEBUG_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        delegate.log(marker, FQCN, DEBUG_INT, format, arguments, null);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        delegate.log(marker, FQCN, DEBUG_INT, msg, null, t);
    }

    @Override
    public void debug(LogCode logCode, String format, Object... argArray) {
        FormattingTuple tuple = this.buildFormattingTuple(logCode, format, argArray);
        delegate.log(null, FQCN, DEBUG_INT, tuple.getMessage(), null, tuple.getThrowable());
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        delegate.log(null, FQCN, INFO_INT, msg, null, null);
    }

    @Override
    public void info(String format, Object arg) {
        delegate.log(null, FQCN, INFO_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        delegate.log(null, FQCN, INFO_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void info(String format, Object... arguments) {
        delegate.log(null, FQCN, INFO_INT, format, arguments, null);
    }

    @Override
    public void info(String msg, Throwable t) {
        delegate.log(null, FQCN, INFO_INT, msg, null, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String msg) {
        delegate.log(marker, FQCN, INFO_INT, msg, null, null);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        delegate.log(marker, FQCN, INFO_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        delegate.log(marker, FQCN, INFO_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        delegate.log(marker, FQCN, INFO_INT, format, arguments, null);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        delegate.log(marker, FQCN, INFO_INT, msg, null, t);
    }

    @Override
    public void info(LogCode logCode, String format, Object... argArray) {
        FormattingTuple tuple = this.buildFormattingTuple(logCode, format, argArray);
        delegate.log(null, FQCN, INFO_INT, tuple.getMessage(), null, tuple.getThrowable());
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        delegate.log(null, FQCN, WARN_INT, msg, null, null);
    }

    @Override
    public void warn(String format, Object arg) {
        delegate.log(null, FQCN, WARN_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void warn(String format, Object... arguments) {
        delegate.log(null, FQCN, WARN_INT, format, arguments, null);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        delegate.log(null, FQCN, WARN_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void warn(String msg, Throwable t) {
        delegate.log(null, FQCN, WARN_INT, msg, null, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        delegate.log(marker, FQCN, WARN_INT, msg, null, null);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        delegate.log(marker, FQCN, WARN_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        delegate.log(marker, FQCN, WARN_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        delegate.log(marker, FQCN, WARN_INT, format, arguments, null);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        delegate.log(marker, FQCN, WARN_INT, msg, null, t);
    }

    @Override
    public void warn(LogCode logCode, String format, Object... argArray) {
        FormattingTuple tuple = this.buildFormattingTuple(logCode, format, argArray);
        delegate.log(null, FQCN, WARN_INT, tuple.getMessage(), null, tuple.getThrowable());
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        delegate.log(null, FQCN, ERROR_INT, msg, null, null);
    }

    @Override
    public void error(String format, Object arg) {
        delegate.log(null, FQCN, ERROR_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        delegate.log(null, FQCN, ERROR_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void error(String format, Object... arguments) {
        delegate.log(null, FQCN, ERROR_INT, format, arguments, null);
    }

    @Override
    public void error(String msg, Throwable t) {
        delegate.log(null, FQCN, ERROR_INT, msg, null, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String msg) {
        delegate.log(marker, FQCN, ERROR_INT, msg, null, null);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        delegate.log(marker, FQCN, ERROR_INT, format, new Object[] {arg}, null);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        delegate.log(marker, FQCN, ERROR_INT, format, new Object[] {arg1, arg2}, null);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        delegate.log(marker, FQCN, ERROR_INT, format, arguments, null);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        delegate.log(marker, FQCN, ERROR_INT, msg, null, t);
    }

    @Override
    public void error(LogCode logCode, String format, Object... argArray) {
        FormattingTuple tuple = this.buildFormattingTuple(logCode, format, argArray);
        delegate.log(null, FQCN, ERROR_INT, tuple.getMessage(), null, tuple.getThrowable());
    }

    @Override
    public void digest(DigestLog digestLog) {
        String content = digestLog.build();
        bizDigestLogger.log(null, FQCN, INFO_INT, content, null, null);
    }

    @Override
    public void exception(Throwable t, String format, Object... argArray) {
        StringJoiner joiner = new StringJoiner(LOG_SEPARATOR);
        if (t instanceof AbstractLogCodeException) {
            AbstractLogCodeException errorCodeException = (AbstractLogCodeException)t;
            LogCode logCode = errorCodeException.getErrorCode();
            if (null != logCode) {
                joiner.add(logCode.name())
                    .add(logCode.message());
            }
        }

        FormattingTuple tuple = this.buildFormattingTuple(null, format, argArray);
        joiner.add(tuple.getMessage());
        delegate.log(null, FQCN, ERROR_INT, joiner.toString(), null, t);
    }
}
