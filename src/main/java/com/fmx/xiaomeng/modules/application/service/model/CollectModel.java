package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class CollectModel implements Serializable {

    private static final long serialVersionUID = 7610730966340643542L;
    private Long articleId;
    private Long userId;
    private Long toUserId;
    private Date createTime;

    public Long getToUserId() { return toUserId; }

    public Long getArticleId() { return articleId; }
}
