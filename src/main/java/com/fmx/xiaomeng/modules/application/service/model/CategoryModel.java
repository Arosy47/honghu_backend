package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class CategoryModel {
    /**
     *
     */
    private String categoryCode;

    /**
     *
     */
    private String categoryName;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 帖子数量
     */
    private Integer articleNum;

    /**
     * 背景图片
     */
    private String bgPicture;
}
