package com.fmx.xiaomeng.modules.systemsetting.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BgRolePageQueryParam {
    Long createUserId;

    String roleName;

    @NotNull
    private PageParam pageParam;



    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
}
