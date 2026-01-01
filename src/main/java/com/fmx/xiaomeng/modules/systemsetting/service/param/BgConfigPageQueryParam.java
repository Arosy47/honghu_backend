package com.fmx.xiaomeng.modules.systemsetting.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 后台配置管理
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class BgConfigPageQueryParam extends BgConfigQueryParam{

    @NotNull
    private PageParam pageParam;

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }

    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
}
