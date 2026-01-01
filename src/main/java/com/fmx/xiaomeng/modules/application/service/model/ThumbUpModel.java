package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/

@Data
public class ThumbUpModel {

    private Long userId;

    private Long toUserId;


    private Long articleId;

    private Long commentId;

    private Date createTime;

    /**
     * 文章/评论
     */
    private Integer thumbUpType;


    public void setCommentId(Long commentId) { this.commentId = commentId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public void setThumbUpType(int thumbUpType) { this.thumbUpType = thumbUpType; }

    public Long getToUserId() { return toUserId; }

    public void setArticleId(Long articleId) { this.articleId = articleId; }
}
