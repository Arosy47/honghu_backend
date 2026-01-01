package com.fmx.xiaomeng.common.enums;
public enum ThumbUpTypeEnum {
    ARTICLE(1, "帖子"),
    COMMENT(2, "评论"),
    ;
    private int code;
    private String desc;
    ThumbUpTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() { return code; }
}