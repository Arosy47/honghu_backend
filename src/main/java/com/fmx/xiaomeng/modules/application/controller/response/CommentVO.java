package com.fmx.xiaomeng.modules.application.controller.response;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class CommentVO {

    /**
     * 评论id
     */
    private Long id;

    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    private Long userId;


    /**
     * 学校id
     */
    private Integer schoolId;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 头像
     */
    private String avatar;


    /**
     * 用户id
     */
    private Long toUserId;

    /**
     * 被回复用户昵称
     */
    private String toUserNickName;

    /**
     * 如果是给某条评论回复，则这里指向目标评论，默认是0
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
     * 评论类型
     * @see
     */
    private String commentType;

    /**
     * 是否匿名
     */
    private boolean anonymous;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     *    点赞数
     */
    private Integer thumbUpCount;

    /**
     * 是否已点赞，
     */
    private Boolean thumbUpStatus;

    /**
     * 是否有回复，对于父评论来说有用，决定是否展示"展开"
     */
    private Boolean hasReply;

    /**
     * 置顶
     */
    private Boolean top;

    /**
     * 管理员
     */
    private Boolean admin;

    /**
     * 是否已删除
     */
    private Boolean hasDelete;

}
