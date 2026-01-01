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
@Getter
// @AllArgsConstructor
public enum ReportTypeEnum {
    ARTICLE(1),

    ARTICLE_COMMENT(2),
    ;

    private Integer code;

    ReportTypeEnum(Integer code) {
        this.code = code;
    }

    private static Map<Integer, ReportTypeEnum> zyMap = new HashMap<>();

    static {
        for (ReportTypeEnum value : ReportTypeEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }


    public static ReportTypeEnum getByCode(Integer code) {
        return zyMap.get(code);
    }

    public Integer getCode() { return code; }
}
