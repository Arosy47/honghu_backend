package com.fmx.xiaomeng.modules.application.service.param;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class ReportQueryParam {
    private Long userId;
    private Long reportedUserId;
    private Integer reportType;

    private Long reportTypeId;


    public void setUserId(Long userId) { this.userId = userId; }
    public void setReportType(Integer reportType) { this.reportType = reportType; }
    public void setReportTypeId(Long reportTypeId) { this.reportTypeId = reportTypeId; }
}
