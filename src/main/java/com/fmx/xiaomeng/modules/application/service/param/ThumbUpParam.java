package com.fmx.xiaomeng.modules.application.service.param;

import lombok.Data;

@Data
public class ThumbUpParam {
    private Long userId;

//    articleId和commentId只存一个，所以搜的时候只传一个
    private Long articleId;

    private Long commentId;


    private Integer matchCardId;

    /**
     * 文章/评论
     */
    private Integer thumbUpType;

    public void setUserId(Long userId) { this.userId = userId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public void setThumbUpType(int thumbUpType) { this.thumbUpType = thumbUpType; }

    public void setArticleId(Long articleId) { this.articleId = articleId; }
}
