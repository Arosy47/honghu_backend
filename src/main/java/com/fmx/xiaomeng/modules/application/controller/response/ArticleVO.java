package com.fmx.xiaomeng.modules.application.controller.response;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import com.fmx.xiaomeng.modules.application.service.model.PositionModel;
import lombok.Data;

import java.util.List;

@Data
/**
 * @author honghu
 */
public class ArticleVO {

    private Integer id;

    private String title;

    private String content;

    private String createTime;

    private Integer userId;

    private String userNickName;

    private String anonymousName;

    private String avatar;

    private List<OssFileModel> imgUrlList;

    private Integer schoolId;

    private String schoolName;

    private String categoryCode;

    private String categoryName;

    private String displayType;

    private String functionType;

    private Integer commentCount;

    private Integer thumbUpCount;

    private Integer viewCount;

    private Integer collectCount;

    private Long hot;

    private Boolean thumbUpStatus;

    private Boolean collectStatus;

    private Boolean dingStatus;

    private Boolean top;

    private Boolean good;

    private boolean anonymous;

    private Boolean admin;

    private Boolean blueV;

    private List<ArticleCommentVO> articleCommentVOList;

    private Boolean hasDelete;

    private PositionModel positionInfo;
}
