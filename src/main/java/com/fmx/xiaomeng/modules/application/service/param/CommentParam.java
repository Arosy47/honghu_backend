package com.fmx.xiaomeng.modules.application.service.param;

import lombok.Data;

@Data
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class CommentParam {
    /**
     * 评论id
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章id
     */
    private Long organizationId;



    private Integer schoolId;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 父评论
     */
    private Long parentCommentId;

    /**
     * 查组局评论时用， 是否内部评论
     */
    private Boolean internal;

    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public void setId(Long id) { this.id = id; }
}
