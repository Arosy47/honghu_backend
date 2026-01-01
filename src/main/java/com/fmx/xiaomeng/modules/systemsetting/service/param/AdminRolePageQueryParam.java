package com.fmx.xiaomeng.modules.systemsetting.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class AdminRolePageQueryParam {
    Long createUserId;

    String roleName;

    @NotNull
    private PageParam pageParam;

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }

    public void setRoleName(String roleName) { this.roleName = roleName; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }

    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
}
