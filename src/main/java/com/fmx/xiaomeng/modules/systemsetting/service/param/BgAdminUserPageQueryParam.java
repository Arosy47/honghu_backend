package com.fmx.xiaomeng.modules.systemsetting.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BgAdminUserPageQueryParam {

    String userName;

    Long createUserId;

    @NotNull
    private PageParam pageParam;


    public void setUserName(String userName) { this.userName = userName; }
    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }
}
