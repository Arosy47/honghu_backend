package com.fmx.xiaomeng.modules.admin.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class CarouselModel {

    /**
     * id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 路径
     */
    private String url;
    /**
     * 图片
     */
    private String imgUrl;
    /**
     * 3圈子页轮播图
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;

    private Integer schoolId;

    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
}
