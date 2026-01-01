package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCommentModel extends CommentModel {

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 如果是父评论，这里是底下的回复
     */
    List<ArticleCommentModel> replyList;

    public Long getArticleId() { return articleId; }
    public void setAnonymous(Boolean anonymous) { this.anonymous = anonymous; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserNickName(String userNickName) { this.userNickName = userNickName; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }

    public Boolean getAnonymous() { return anonymous; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setAdmin(Boolean admin) { this.admin = admin; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public void setThumbUpStatus(Boolean thumbUpStatus) { this.thumbUpStatus = thumbUpStatus; }

    public void setThumbUpCount(Integer thumbUpCount) { this.thumbUpCount = thumbUpCount; }

    public Boolean getHasDelete() { return hasDelete; }

    public Long getToUserId() { return toUserId; }
    public String getContent() { return content; }
    public com.fmx.xiaomeng.common.enums.CommentTypeEnum getCommentType() { return commentType; }
    public Long getParentCommentId() { return parentCommentId; }
    public String getUserNickName() { return userNickName; }
    public Long getReplyCommentId() { return replyCommentId; }

    public Integer getSchoolId() { return schoolId; }

    public void setReplyList(java.util.List<ArticleCommentModel> replyList) { this.replyList = replyList; }
}
