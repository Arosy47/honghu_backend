package com.fmx.xiaomeng.modules.application.controller.request;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

@Data
public class CommentRequestDTO {
//    文章和组局id这两个必填一个，回复评论也要加上，用于页面跳转

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 学校id
     */
    private Integer schoolId;

    /**
     * 组局id
     */
    private Long organizationId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 帖子作者id
     */
    private Long articleUserId;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户昵称（这里如果是匿名帖，前端传过来的就是匿名昵称）
     */
    private String userNickName;

    /**
     * 用户id
     */
    private Long toUserId;

    /**
     * 被回复用户昵称(这里如果是匿名帖，前端传过来的就是匿名昵称)
     */
    private String toUserNickName;

    /**
     * 如果是给某条评论回复，则这里指向目标评论，默认是0，如果是父评论，这个字段就是0
     */
    private Long parentCommentId;

    /**
     * 被回复的评论id，默认是0
     */
    private Long replyCommentId;

    /**
     * 评论内容
     */
    private String content;


    /**
     * 评论图片url
     */
    private OssFileModel imgUrl;

    /**
     * 评论类型(文字/图文)
     * @see
     */
//    @NotNull(message = "评论类型不能为空")
    private String commentType;


//
//    /**
//     * 评论类型 1.评论文章 2.回复评论 3.回复回复
//     * @see CommentNotificationTypeEnum
//     */
////    @NotBlank(message = "评论类型不能为空")
////    private Integer commentType;

    /**
     * 是否匿名(由帖子是否匿名决定，这个字段暂时不用)
     */
    private Boolean anonymous;


    public Long getUserId() { return userId; }


    public Long getArticleUserId() { return articleUserId; }
    public String getContent() { return content; }

}
