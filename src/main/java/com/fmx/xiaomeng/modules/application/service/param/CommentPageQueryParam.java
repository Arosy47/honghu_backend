package com.fmx.xiaomeng.modules.application.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentPageQueryParam {


    /**
     * 用户id
     */
    private Long userId;
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章id
     */
    // private Long organizationId;

    /**
     * 父评论id
     */
    private Long  parentCommentId;


    private Integer schoolId;
    /**
     * 查组局评论时用， 是否内部评论
     */
    // private Boolean internal;

    /**
     *
     */
    private String orderFieldName;

    /**
     *
     */
    private String order;

    /**
     * 被回复的评论id  查询的时候貌似不需要这个字段
     */
//    private Long replyCommentId;


    private Boolean hasDelete;


    @NotNull
    private PageParam pageParam;

    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
    public void setOrderFieldName(String orderFieldName) { this.orderFieldName = orderFieldName; }
    public void setOrder(String order) { this.order = order; }

    public void setHasDelete(boolean hasDelete) { this.hasDelete = hasDelete; }
    public void setUserId(Long userId) { this.userId = userId; }

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }


    public void setParentCommentId(long parentCommentId) { this.parentCommentId = parentCommentId; }
    public Long getArticleId() { return articleId; }

}
