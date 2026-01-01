package com.fmx.xiaomeng.modules.application.service.model;

import com.fmx.xiaomeng.common.enums.ReportTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class ReportRecordModel {

    private Integer id;

    private Date createTime;

    private String reason;

    private Long userId;

    private Long reportedUserId;

    private ReportTypeEnum reportType;

    private Long reportTypeId;

    private Integer handleStatus;

    private String content;

    private List<OssFileModel> imgUrlList;

    private Integer schoolId;
    private Boolean reportValid;



    public void setContent(String content) { this.content = content; }
    public void setImgUrlList(java.util.List<OssFileModel> imgUrlList) { this.imgUrlList = imgUrlList; }

}
