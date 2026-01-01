package com.fmx.xiaomeng.modules.application.controller.response;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Date 2025/4/2 22:31
 * @Author honghu
 **/
@Data
public class MockArticleVO {
    private Long id;

    private Integer schoolId;

    private String content;

    /**
     * 图片链接
     */
    private List<OssFileModel> imgUrlList;

    private String createTime;

    private Long userId;

    private String userNickName;

    private Long adminUserId;

    private String categoryCode;
    private String categoryName;

    private Boolean hasDelete;
}
