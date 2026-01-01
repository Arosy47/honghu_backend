package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class NotificationModel {

    private Long id;

    /**
     * 这个字段只为评论通知使用，包括评论文章，回复评论，评论组局
     * 如果是评论或者回复评论，当将这条评论删除时，对应的通知也要删除！（或者删除通知里面的content）
     */
    private Long sourceCommentId;

    private Long articleId;



    //被回复或者点赞的评论id，
    private Long articleCommentId;

    private Long userId;

    private OssFileModel avatar;

    private String userNickName;
    // 通知对象ID
    @NotNull(message = "消息接收方id")
    private Long targetUserId;

    // 类型: MESSAGE,REPLY, COMMENT, COLLECT, TOPIC_UP, COMMENT_UP
    /**
     * @see com.fmx.xiaomeng.common.enums.NotificationTypeEnum
     */
    @NotNull(message = "消息类型不能为空")
    private Integer noticeType;

    private Date createTime;

    /**
     * 是否已读
     */
    private Boolean alreadyRead;


    /**
     * 是否已删除
     */
    private Boolean hasDelete;

    //这个内容是指：**点赞了你的文章，**评论了你
    //这里面保存回复，评论的内容，点赞不保存内容，但保存记录
    //前端根据type来提示：***点赞了你的评论/文章   ***评论了你  ***私信了你
    private String content;


    //具体的点赞人信息（名字），评论内容等,聚合在这里，DO模型只有上面的字段
    private ArticleModel articleModel;

    private ArticleCommentModel articleCommentModel;

    private UserModel userModel;

    /**
     * 该消息的发出者是不是所属主题的创建者，比如帖子的作者或者局长
     */
    private Boolean isOwner;

    public void setContent(String content) { this.content = content; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }


    public void setNoticeType(Integer noticeType) { this.noticeType = noticeType; }
    public void setSourceCommentId(Long sourceCommentId) { this.sourceCommentId = sourceCommentId; }

    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }

    public void setArticleCommentId(Long articleCommentId) { this.articleCommentId = articleCommentId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }

    public void setUserNickName(String userNickName) { this.userNickName = userNickName; }
    public void setAvatar(OssFileModel avatar) { this.avatar = avatar; }
    public void setAlreadyRead(Boolean alreadyRead) { this.alreadyRead = alreadyRead; }

    public void setAvatar(String avatarUrl) { this.avatar = new OssFileModel(); this.avatar.setUrl(avatarUrl); }


    public Long getUserId() { return userId; }
    public Long getTargetUserId() { return targetUserId; }
    public Integer getNoticeType() { return noticeType; }
    public Long getArticleId() { return articleId; }
    public Long getArticleCommentId() { return articleCommentId; }

    public void setArticleModel(ArticleModel articleModel) { this.articleModel = articleModel; }
    public void setArticleCommentModel(ArticleCommentModel articleCommentModel) { this.articleCommentModel = articleCommentModel; }

    public void setIsOwner(boolean isOwner) { this.isOwner = isOwner; } public boolean getIsOwner() { return isOwner; }
}
