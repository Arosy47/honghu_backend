package com.fmx.xiaomeng.common.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {
    ARTICLE_THUMB_UP(1, "点赞文章"),
    COMMENT_THUMB_UP(2, "点赞评论"),
    COMMENT_ARTICLE(3, "评论文章"),
    REPLY(4, "回复评论"),
    COLLECT_ARTICLE(5, "收藏"),

    JUBAO(31, "举报"),
    ARTICLE_VIOLATION(41, "帖子违规"),
    COMMENT_VIOLATION(43, "评论违规"),
    AVATAR_VIOLATION(44, "头像违规"),
    CERTIFICATION_PASS(46, "认证审核通过"),
    CERTIFICATION_UNPASS(47, "认证审核不通过"),
    REPORT_SUCCESS(48, "举报成功");

    private final Integer code;
    private final String desc;

    NotificationTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() { return code; }
}
