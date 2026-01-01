package com.fmx.xiaomeng.common.enums;
import lombok.Getter;
@Getter
public enum MsgTypeEnum {
    HEART_BEAT(6, "HEART_BEAT");
    
    private final Integer code;
    private final String desc;
    MsgTypeEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
}