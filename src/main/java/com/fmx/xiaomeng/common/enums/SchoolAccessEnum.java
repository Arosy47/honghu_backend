package com.fmx.xiaomeng.common.enums;

public enum SchoolAccessEnum {
    SCHOOL(1, "本校"),
    CITY(2, "同城"),
    ALL(3,"全国"),
    ;
    private final Integer code;
    private final String desc;

    SchoolAccessEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() { return code; }
    public String getDesc() { return desc; }
}