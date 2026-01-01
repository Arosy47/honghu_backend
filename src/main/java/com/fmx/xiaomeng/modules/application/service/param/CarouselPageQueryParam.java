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
public class CarouselPageQueryParam {
    private Integer schoolId;

    @NotNull
    private PageParam pageParam;

    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
}
