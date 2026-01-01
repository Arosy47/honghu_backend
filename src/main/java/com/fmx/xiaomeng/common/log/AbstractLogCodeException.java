package com.fmx.xiaomeng.common.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public class AbstractLogCodeException extends RuntimeException {
    private static final String SEPARATOR = " | ";

    /**
     * 日志码
     */
    private final LogCode errorCode;

    /**
     * 错误信息(一般是包括上下文信息的)
     */
    private String message;

    /**
     * 错误属性（不一定用）
     */
    private ErrorAttributes errorAttributes;

    public AbstractLogCodeException(Throwable cause, LogCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public AbstractLogCodeException(Throwable cause, LogCode errorCode, String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage(), cause);
        this.errorCode = errorCode;
    }

    public AbstractLogCodeException(LogCode errorCode, String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage());
        this.errorCode = errorCode;
    }

    public AbstractLogCodeException(Throwable cause, LogCode errorCode,
                                    ErrorAttributes errorAttributes,
                                    String format,
                                    Object... arguments) {

        super(errorAttributes.toString() + Optional.ofNullable(format)
                .map(e -> SEPARATOR + MessageFormatter.arrayFormat(format, arguments).getMessage())
                .orElse(""), cause);
        this.errorCode = errorCode;
        this.errorAttributes = errorAttributes;
    }

    public AbstractLogCodeException(LogCode errorCode, ErrorAttributes errorAttributes, String format, Object... arguments) {
        super(errorAttributes.toString() + Optional.ofNullable(format)
                .map(e -> SEPARATOR + MessageFormatter.arrayFormat(format, arguments).getMessage())
                .orElse(""));
        this.errorCode = errorCode;
        this.errorAttributes = errorAttributes;
    }

    public LogCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return Objects.nonNull(message) ? message : super.getMessage();
    }

    public ErrorAttributes getErrorAttributes() {
        return errorAttributes;
    }

    /**
     * 附加异常信息
     */


    public void appendMessage(String format, Object... arguments) {
        if (Objects.nonNull(format)) {
            String message = this.getMessage();
            String appendMessage = MessageFormatter.arrayFormat(format, arguments).getMessage();
            if (Objects.isNull(message)) {
                this.message = appendMessage;
            } else {
                this.message = message + SEPARATOR + appendMessage;
            }
        }
    }

    /**
     * 错误属性
     */
    public static class ErrorAttributes {
        private final Map<String, Object> attributes = new LinkedHashMap<>(1 << 2);

        public static ErrorAttributes errorAttributes(Object... arguments) {
            ErrorAttributes errorAttributes = new ErrorAttributes();
            LinkedList queue = new LinkedList<>(Arrays.asList(arguments));
            while (!queue.isEmpty()) {
                String key = queue.pop().toString();
                Object value = Objects.nonNull(queue.peek()) ? queue.poll() : null;
                errorAttributes.append(key, value);
            }
            return errorAttributes;
        }

        public static ErrorAttributes errorAttributes(Map<String, Object> attributes) {
            ErrorAttributes errorAttributes = new ErrorAttributes();
            errorAttributes.attributes.putAll(attributes);
            return errorAttributes;
        }

        public static ErrorAttributes parse(String errorAttributesJsonString) {
            return Optional.ofNullable(errorAttributesJsonString).map(e -> {
                Map<String, Object> attributes = JSON.parseObject(e, new TypeReference<HashMap<String, Object>>() {
                });
                return errorAttributes(attributes);
            }).orElse(null);
        }

        public ErrorAttributes append(String key, Object value) {
            attributes.put(key, value);
            return this;
        }

        public Map<String, Object> get() {
            return attributes;
        }

        /**
         * 转换json字符串
         */
        public String toJsonString() {
            return attributes.isEmpty() ? null : JSON.toJSONString(attributes);
        }

        @Override
        public String toString() {
            return attributes.entrySet().stream().map(entry -> entry.getKey() + ":" + entry.getValue())
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
        }

    }

}
