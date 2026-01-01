package com.fmx.xiaomeng.modules.admin.model;

import com.fmx.xiaomeng.common.enums.CategoryEnum;
import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class MockArticleModel {
    private Long id;

    private Integer schoolId;

    private String content;

    /**
     * 图片链接  OssFileModel
     */
    private List<OssFileModel> imgUrlList;

    private Date createTime;

    private Long userId;

    private String userNickName;

    private Long adminUserId;

    private CategoryEnum category;

    private Boolean hasDelete;


    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public void setAdminUserId(Long adminUserId) { this.adminUserId = adminUserId; }

    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
}
