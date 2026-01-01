package com.fmx.xiaomeng.modules.application.service.model;

import com.fmx.xiaomeng.common.enums.CommentTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class CommentModel {

    /**
     * 评论id
     */
    protected Long id;


    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    protected Long userId;


    /**
     * 学校id
     */
    protected Integer schoolId;

    /**
     * 用户昵称
     */
    protected String userNickName;

    /**
     * 匿名昵称(如果要用这个字段，那toUserAnonymousName也要新增一个字段)
     */
    @Deprecated
    private String anonymousName;

    /**
     * 头像
     */
    protected String avatar;


    /**
     * 用户id
     */
    protected Long toUserId;

    /**
     * 被回复用户昵称(如果是匿名帖地下，这个字段就是保存对方的匿名昵称！！！！)
     */
    private String toUserNickName;

    /**
     * 所属的父评论id，本身是父评论，这个字段就是0
     */
    protected Long parentCommentId;

    /**
     * 被回复的评论id，本身是父评论的话，这个字段是0
     */
    protected Long replyCommentId;

    /**
     * 评论内容
     */
    protected String content;

    /**
     * 评论图片url
     */
    private OssFileModel imgUrl;

    /**
     * 评论类型(文字还是图文)
     * @see CommentTypeEnum
     */
    protected CommentTypeEnum commentType;


    /**
     * 是否匿名
     */
    protected Boolean anonymous;

    /**
     * 创建时间
     */
    protected Date createTime;


    /**
     *    点赞数
     */
    protected Integer thumbUpCount;

    /**
     * 是否已点赞，
     */
    protected Boolean thumbUpStatus;

    /**
     * 是否有回复，对于父评论来说有用，决定是否展示"展开"    废弃，这个字段不用了
     */
    @Deprecated
    private Boolean hasReply;

    /**
     * 置顶
     */
    private Boolean top;

    /**
     * 管理员
     */
    protected Boolean admin;

    /**
     * 是否已删除
     */
    protected Boolean hasDelete;


    public Long getUserId() { return userId; }
    public OssFileModel getImgUrl() { return imgUrl; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getAvatar() { return avatar; }
    public String getContent() { return content; }
    public com.fmx.xiaomeng.common.enums.CommentTypeEnum getCommentType() { return commentType; }



    public Long getToUserId() { return toUserId; }
    public Long getReplyCommentId() { return replyCommentId; }
    public String getUserNickName() { return userNickName; }

}
