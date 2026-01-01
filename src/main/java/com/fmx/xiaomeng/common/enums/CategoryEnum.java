package com.fmx.xiaomeng.common.enums;
public enum CategoryEnum {
    CATEGORY_1(1, "Cat1"),
    SECOND_HAND_MARKET(2, "二手市场"),
    NONE(0, "无");
    
    private final Integer code;
    private final String desc;
    CategoryEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
    public Integer getCode() { return code; }
    public Integer getId() { return code; } // Alias
    public String getDesc() { return desc; }
    
    public static CategoryEnum getById(Integer id) {
        for (CategoryEnum e : values()) {
            if (e.code.equals(id)) return e;
        }
        return NONE;
    }
}