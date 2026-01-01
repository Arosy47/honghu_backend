package com.fmx.xiaomeng.common.enums;
import lombok.Getter;
@Getter
public enum RefundOrderStatusEnum {
    STATUS_1(1, "Status1");
    private final Integer code;
    private final String desc;
    RefundOrderStatusEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
}