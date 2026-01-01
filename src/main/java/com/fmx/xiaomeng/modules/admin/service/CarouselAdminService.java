package com.fmx.xiaomeng.modules.admin.service;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.entity.LinkEntity;
import com.fmx.xiaomeng.modules.admin.model.CarouselModel;
import com.fmx.xiaomeng.modules.application.service.param.CarouselPageQueryParam;

/**
 * 轮播图
 *
 * @author honghu
 * @date 2025-12-20
 */
public interface CarouselAdminService {

    PageList<LinkEntity> queryPage(CarouselPageQueryParam param);


    void addCarousel(CarouselModel model);

}

