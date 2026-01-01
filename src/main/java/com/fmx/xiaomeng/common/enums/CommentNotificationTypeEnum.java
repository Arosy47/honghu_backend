package com.fmx.xiaomeng.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// @AllArgsConstructor
@Getter
//@Deprecated
public enum CommentNotificationTypeEnum {
    COMMENT_ARTICLE(1, "评论文章"),
;
    /**
     * code用于在数据库中索引
     */
    private final Integer code;
    private final String desc;


    private static Map<Integer, CommentNotificationTypeEnum> zyMap = new HashMap<>();
    static {
        for (CommentNotificationTypeEnum value : CommentNotificationTypeEnum.values()) {
            zyMap.put(value.getCode(),value);
        }
    }
    public static CommentNotificationTypeEnum getByCode(Integer code){
        return zyMap.get(code);
    }


    CommentNotificationTypeEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
    public Integer getCode() { return code; }
}
