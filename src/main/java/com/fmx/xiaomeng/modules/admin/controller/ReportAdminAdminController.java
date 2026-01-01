package com.fmx.xiaomeng.modules.admin.controller;
import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.ReportHandleStatusEnum;
import com.fmx.xiaomeng.common.enums.ReportTypeEnum;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.admin.response.ReportRecordVO;
import com.fmx.xiaomeng.modules.admin.service.ReportAdminService;
import com.fmx.xiaomeng.modules.admin.service.param.ReportPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.model.ReportRecordModel;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/report")
public class ReportAdminAdminController extends AbstractAdminController {

    @Autowired
    private VOConverter voConverter;
    /**
     * 举报分页查询
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("admin:report:list")
    public Result<PageList<ReportRecordVO>> listReport(
            @RequestParam(name = "handleStatus") String handleStatus,
            @RequestParam(name = "schoolId") Integer schoolId,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize) {

        ReportPageQueryParam param = new ReportPageQueryParam();
        param.setPageParam(new PageParam(pageNum, pageSize));

        if (StringUtils.isNotBlank(handleStatus)) {
            param.setHandleStatus(ReportHandleStatusEnum.valueOf(handleStatus).getCode());
        }

        Integer searchSchoolId = schoolId;
        if (!getUser().getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
            //            非超级管理员只能看自己学校的帖子
            searchSchoolId = getUser().getSchoolId();
        }
        param.setSchoolId(searchSchoolId);

//        不给管理员开放
        PageList<ReportRecordModel> reportRecordModelPageList = reportAdminService.listReport(param);

        List<ReportRecordVO> reportRecordVOList = Optional.ofNullable(reportRecordModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> {
                    return voConverter.convert(model);
                })
                .collect(Collectors.toList());

        return Result.ok(new PageList<>(reportRecordVOList, reportRecordModelPageList.getPaginator()));
    }


    /**
     * 举报有效
     *
     * @param reportId
     * @return
     */
    @SysLog("举报有效")
    @GetMapping("/reportValid")
    @RequiresPermissions("admin:report:valid")
    public Result reportValid(
            @RequestParam("id") Integer reportId,
            @RequestParam("reportType") String reportType,
            @RequestParam("reportTypeId") Long reportTypeId) {

        reportAdminService.reportValid(reportId, ReportTypeEnum.valueOf(reportType).getCode(), reportTypeId);
        return Result.ok();
    }

    /**
     * 举报无效
     *
     * @param reportId
     * @return
     */
    @SysLog("举报无效")
    @GetMapping("/reportInvalid")
    @RequiresPermissions("admin:report:invalid")
    public Result reportInValid(@RequestParam("id") Integer reportId) {

//        不给管理员开放
        reportAdminService.reportInvalid(reportId);
        return Result.ok();
    }


    @org.springframework.beans.factory.annotation.Autowired
    private com.fmx.xiaomeng.modules.admin.service.ReportAdminService reportAdminService;

}
