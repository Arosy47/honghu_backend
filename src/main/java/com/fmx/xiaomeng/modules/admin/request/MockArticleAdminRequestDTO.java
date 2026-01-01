package com.fmx.xiaomeng.modules.admin.request;

import com.fmx.xiaomeng.modules.application.controller.request.ArticleRequestDTO;
import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class MockArticleAdminRequestDTO extends ArticleRequestDTO {
    private Long userId;

    private Integer schoolId;



    public Long getUserId() { return userId; }
    public Integer getSchoolId() { return schoolId; }
}
