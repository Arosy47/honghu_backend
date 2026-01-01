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
public enum ArticleDisplayTypeEnum {

    WORD(1, "文字"),

    PICTURE(2, "图文"),

    VIDEO(3, "视频"),

    ;

    /**
     * code用于在数据库中索引
     */
    private final Integer code;
    private final String desc;

    ArticleDisplayTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<Integer, ArticleDisplayTypeEnum> zyMap = new HashMap<>();

    static {
        for (ArticleDisplayTypeEnum value : ArticleDisplayTypeEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }


    public static ArticleDisplayTypeEnum getByCode(Integer code) {
        return zyMap.get(code);
    }

    public Integer getCode() { return code; }
    public String getDesc() { return desc; }
}
