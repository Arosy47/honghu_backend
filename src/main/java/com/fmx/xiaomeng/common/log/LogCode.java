package com.fmx.xiaomeng.common.log;

/**
 * @Description 日志码
 * @Date 2025-12-20
 * @Author honghu
 **/
public interface LogCode {

    /**
     * 日志码名称
     * 内部日志打印可读性好的日志码名称
     * @return
     */
    String name();

    /**
     * 日志码code
     * 外部透出符合阿里编码规范的错误码
     * @return
     */
    String code();

    /**
     * 日志码信息
     * @return
     */
    String message();
}
