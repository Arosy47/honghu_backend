package com.fmx.xiaomeng.modules.systemsetting.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class SysLogPageQueryParam {


    @NotNull
    private PageParam pageParam;

    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
}
