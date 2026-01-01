package com.fmx.xiaomeng.modules.application.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class CollectPageQueryParam {
    private Long userId;

    private Long articleId;

    @NotNull
    private PageParam pageParam;

    public void setUserId(Long userId) { this.userId = userId; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }
}
