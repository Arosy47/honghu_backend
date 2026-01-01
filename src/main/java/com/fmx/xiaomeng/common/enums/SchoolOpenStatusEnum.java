package com.fmx.xiaomeng.common.enums;
public enum SchoolOpenStatusEnum {
    OPEN(1, "开放"),
    CLOSE(0, "关闭"),
    ;
    private int code;
    private String desc;
    SchoolOpenStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() { return code; }
}