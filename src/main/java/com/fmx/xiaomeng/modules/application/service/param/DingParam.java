package com.fmx.xiaomeng.modules.application.service.param;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class DingParam {
    private Long userId;

    private Long articleId;



        public void setArticleId(Long articleId) { this.articleId = articleId; }
        public void setUserId(Long userId) { this.userId = userId; }
    
}
