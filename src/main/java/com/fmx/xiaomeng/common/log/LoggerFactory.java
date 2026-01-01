package com.fmx.xiaomeng.common.log;

/**
 * 自定义日志工厂
 * <hr>
 * <p>
 * 通过 lombok 包下的 @CustomLog 注解自动生成 log 对象
 * <p>
 * Example:
 * <pre>
 * &#64;CustomLog
 * public class LogExample {
 * }
 * </pre>
 * 在项目根目录下（与 root pom.xml 平级）新建 lombok.config 文件，增加配置项：
 * <pre>
 * lombok.log.custom.declaration=com.alibaba.c2m.util.commons.log.Logger com.alibaba.c2m.util.commons.log.LoggerFactory.getLogger(NAME)
 * </pre>
 * <hr>
 * 配置等价于：
 * <pre>
 * public class LogExample {
 *     private static final com.alibaba.c2m.util.commons.log.Logger log = com.alibaba.c2m.util.commons.log.LoggerFactory.getLogger(LogExample.class.getName());
 * }
 * </pre>
 *
 * @author honghu
 * @date 2025-12-20
 */
public class LoggerFactory {

    public static Logger getLogger(String name) {
        return new LoggerImpl(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(clazz.getName());
    }

}
