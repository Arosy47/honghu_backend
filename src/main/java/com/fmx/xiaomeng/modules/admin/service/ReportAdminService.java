package com.fmx.xiaomeng.modules.admin.service;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.service.param.ReportPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.model.ReportRecordModel;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public interface ReportAdminService {

    void reportValid(Integer reportId, Integer reportType, Long reportTypeId);

    void reportInvalid(Integer reportId);

    PageList<ReportRecordModel> listReport(ReportPageQueryParam param);
}
