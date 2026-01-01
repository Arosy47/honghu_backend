package com.fmx.xiaomeng.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// @AllArgsConstructor
@Getter
public enum AccountStatusEnum {
    NORMAL(1, "正常"),

    NO_EDIT(2,"禁止编辑"),

    NO_VIEW(3,"禁止浏览"),
    ;

    /**
     * code用于在数据库中索引
     */
    private final Integer code;
    private final String desc;

    private static Map<Integer, AccountStatusEnum> zyMap = new HashMap<>();
    static {
        for (AccountStatusEnum value : AccountStatusEnum.values()) {
            zyMap.put(value.getCode(),value);
        }
    }
    public static AccountStatusEnum getByCode(Integer code){
        if(Objects.isNull(code)){
            return AccountStatusEnum.NORMAL;
        }
        return zyMap.get(code);
    }

    AccountStatusEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
    public Integer getCode() { return code; }
}
