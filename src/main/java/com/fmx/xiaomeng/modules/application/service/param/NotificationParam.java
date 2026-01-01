package com.fmx.xiaomeng.modules.application.service.param;

import lombok.Data;

import java.util.List;

@Data
public class NotificationParam {

    private Long userId;

    private Long targetUserId;

    private Long sourceCommentId;

    private Long articleId;

    private Long articleCommentId;

    private Integer noticeType;

    private List<Integer> noticeTypeList;

    public void setSourceCommentId(Long sourceCommentId) { this.sourceCommentId = sourceCommentId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public void setArticleCommentId(Long articleCommentId) { this.articleCommentId = articleCommentId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public void setNoticeType(Integer noticeType) { this.noticeType = noticeType; }
    public void setNoticeTypeList(java.util.List<Integer> noticeTypeList) { this.noticeTypeList = noticeTypeList; }

    public void setArticleId(Long articleId) { this.articleId = articleId; }
}
