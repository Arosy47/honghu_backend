package com.fmx.xiaomeng.common.enums;
import lombok.Getter;
@Getter
public enum MatchScopeEnum {
    SCOPE_1(1, "Scope1");
    private final Integer code;
    private final String desc;
    MatchScopeEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
}