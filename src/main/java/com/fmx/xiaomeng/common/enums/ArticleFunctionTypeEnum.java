package com.fmx.xiaomeng.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
// @AllArgsConstructor
@Getter
public enum ArticleFunctionTypeEnum { TASK(1, "任务"),
    NORMAL(1, "普通帖"),
    VOTE(2, "投票帖"),
    ;

    /**
     * code用于在数据库中索引
     */
    private final Integer code;
    private final String desc;

    ArticleFunctionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<Integer, ArticleFunctionTypeEnum> zyMap = new HashMap<>();

    static {
        for (ArticleFunctionTypeEnum value : ArticleFunctionTypeEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }


    public static ArticleFunctionTypeEnum getByCode(Integer code) {
        return zyMap.get(code);
    }

    public Integer getCode() { return code; }
    public String getDesc() { return desc; }
}
