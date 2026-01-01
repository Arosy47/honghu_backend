package com.fmx.xiaomeng.modules.admin.response;

import com.fmx.xiaomeng.common.enums.ReportTypeEnum;
import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class ReportRecordVO {

    private Integer id;

    private String createTime;

    private String reason;

    private ReportTypeEnum reportType;

    private Long reportTypeId;

    private Integer handleStatus;

    private String content;

    private List<OssFileModel> imgUrlList;

    private Integer schoolId;
    private Boolean reportValid;

}
