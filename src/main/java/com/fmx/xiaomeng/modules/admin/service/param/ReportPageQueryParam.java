package com.fmx.xiaomeng.modules.admin.service.param;
import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;
@Data
public class ReportPageQueryParam {
    private PageParam pageParam;
    private Integer handleStatus;
    private Integer schoolId;
    
    // Manual setters just in case
    public void setPageParam(PageParam pageParam) { this.pageParam = pageParam; }
    public PageParam getPageParam() { return pageParam; }
    public void setHandleStatus(Integer handleStatus) { this.handleStatus = handleStatus; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
}