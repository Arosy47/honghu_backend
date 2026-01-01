package com.fmx.xiaomeng.common.log;


/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public interface Logger extends org.slf4j.Logger {
    /**
     * 打印 trace 级别日志
     * @param logCode
     * @param format
     * @param argArray
     */
    void trace(LogCode logCode, String format, Object... argArray);
    /**
     * 打印 debug 级别日志
     * @param logCode
     * @param format
     * @param argArray
     */
    void debug(LogCode logCode, String format, Object... argArray);

    /**
     * 打印 info 级别日志
     * @param logCode
     * @param format
     * @param argArray
     */
    void info(LogCode logCode, String format, Object... argArray);

    /**
     * 打印 warn 级别日志
     * @param logCode
     * @param format
     * @param argArray
     */
    void warn(LogCode logCode, String format, Object... argArray);

    /**
     * 打印 error 级别日志 (用在不需要抛异常，不做处理，异常也没事的地方，只需要有个错误日志记录就行)
     * @param logCode
     * @param format
     * @param argArray
     */
    void error(LogCode logCode, String format, Object... argArray);


    /**
     * 打印摘要日志
     * @param digestLog
     */
    void digest(DigestLog digestLog);

    /**
     * 打印Exception 日志
     * @param t
     * @param format
     * @param argArray
     */
    void exception(Throwable t, String format, Object... argArray);



}
