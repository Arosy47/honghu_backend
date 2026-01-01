package com.fmx.xiaomeng.common.enums;
import lombok.Getter;
@Getter
public enum WxMsgTemplateTypeEnum {
    TYPE_1(1, "Type1"),
    HOT_ARTICLE(2, "热门帖子"),
    NEW_COMMENT(3, "新评论");
    
    private final Integer code;
    private final String desc;
    WxMsgTemplateTypeEnum(Integer code, String desc) { this.code = code; this.desc = desc; }
    public static WxMsgTemplateTypeEnum getByTemplateId(String templateId) { return TYPE_1; } // Stub
}