package com.fmx.xiaomeng.modules.admin.request;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPageQueryParam {

    private Integer schoolId;

    private Integer authStatus;

    private String keyword;

    @NotNull
    private PageParam pageParam;

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }

    public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
}
