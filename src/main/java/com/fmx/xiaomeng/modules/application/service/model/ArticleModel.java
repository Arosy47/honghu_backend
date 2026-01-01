package com.fmx.xiaomeng.modules.application.service.model;

import com.fmx.xiaomeng.common.enums.ArticleDisplayTypeEnum;
import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.common.enums.CategoryEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class ArticleModel implements Serializable {

    private Long id;

    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private Date createTime;

    private Date modifiedTime;

    private Date polishTime;

    private Long userId;

    private String userNickName;

    private String anonymousName;

    private String avatar;

    private List<OssFileModel> imgUrlList;

    @NotNull
    private Integer schoolId;

    private String schoolName;

    private CategoryEnum category;

    @NotNull(message = "帖子展示类型不能为空")
    private ArticleDisplayTypeEnum displayType;

    @NotNull(message = "帖子功能类型不能为空")
    private ArticleFunctionTypeEnum functionType;

    private Integer commentCount;

    private Integer thumbUpCount;

    private Integer viewCount;

    private Integer collectCount;

    private Boolean thumbUpStatus;

    private Boolean collectStatus;

    private Boolean dingStatus;

    private Boolean top;

    private Boolean good;

    private Boolean anonymous;

    private Boolean hasDelete;

    private PositionModel positionInfo;

    private Long hot;

    private Boolean admin;

    private Boolean blueV;

    public Boolean getAnonymous() { return anonymous; }
    public Integer getSchoolId() { return schoolId; }

    public Long getUserId() { return userId; }
    public String getAvatar() { return avatar; }
    public String getAnonymousName() { return anonymousName; }

    public void setFunctionType(com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum functionType) { this.functionType = functionType; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
    public void setUserNickName(String userNickName) { this.userNickName = userNickName; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setAnonymousName(String anonymousName) { this.anonymousName = anonymousName; }

    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }

    public java.util.List<OssFileModel> getImgUrlList() { return imgUrlList; }
    public Long getId() { return id; }
    public String getContent() { return content; }
    public Boolean getHasDelete() { return hasDelete; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setAdmin(boolean admin) { this.admin = admin; }


    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public void setModifiedTime(java.util.Date modifiedTime) { this.modifiedTime = modifiedTime; }



    public void setBlueV(Boolean blueV) { this.blueV = blueV; }
    public void setThumbUpStatus(boolean thumbUpStatus) { this.thumbUpStatus = thumbUpStatus; }
    public void setCollectStatus(boolean collectStatus) { this.collectStatus = collectStatus; }
    public Integer getThumbUpCount() { return thumbUpCount; }
    public Integer getViewCount() { return viewCount; }
    public Integer getCommentCount() { return commentCount; }
    public void setHot(long hot) { this.hot = hot; }
    public java.util.Date getCreateTime() { return createTime; }
    public String getUserNickName() { return userNickName; }
    public Integer getFunctionType() { return functionType != null ? functionType.getCode() : null; }

}
