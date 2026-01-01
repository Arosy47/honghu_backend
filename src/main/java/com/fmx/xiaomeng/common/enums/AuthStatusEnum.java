package com.fmx.xiaomeng.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// @AllArgsConstructor
@Getter
public enum  AuthStatusEnum {
    UNAUTH(1, "未认证"),

    AUTHING(2,"认证中"),

    AUTHED(3,"认证完成"),

//    AUTH_FAIL(4,"认证失败"),  //是不是不需要这个
    ;

    /**
     * code用于在数据库中索引
     */
    private final Integer code;
    private final String desc;

    private static Map<Integer, AuthStatusEnum> zyMap = new HashMap<>();
    static {
        for (AuthStatusEnum value : AuthStatusEnum .values()) {
            zyMap.put(value.getCode(),value);
        }
    }
    public static AuthStatusEnum getByCode(Integer code){
        return zyMap.get(code);
    }

    AuthStatusEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
    public Integer getCode() { return code; }
}
