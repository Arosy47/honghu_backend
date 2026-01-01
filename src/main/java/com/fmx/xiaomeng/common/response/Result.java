package com.fmx.xiaomeng.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.util.annotation.Nullable;

@Data
@NoArgsConstructor
public class Result<T> {
    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 错误信息
     */
    private String message;


    /**
     * 返回值
     */
    private T data;


    public Result(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok(@Nullable String message, @Nullable T data) {
        return new Result<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(HttpStatus.OK.value(), "OK", null);
    }

//    public static <T> Result<T> ok(@Nullable String message) {
//        return new Result<>(HttpStatus.OK.value(), message, null);
//    }

    public static <T> Result<T> unAuth(@Nullable String message) {
        return new Result<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "未认证", null);
    }


    public static <T> Result<T> ok(@Nullable T data) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> Result<T> error(@Nullable String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(int statusCode, @Nullable String message) {
        return new Result<>(statusCode, message, null);
    }

    public static <T> Result<T> error(int statusCode,@Nullable T data) {
        return new Result<>(statusCode, null, data);
    }


}
