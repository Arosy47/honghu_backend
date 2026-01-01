/**
 * @author honghu
 * @date 2025-12-19
 */
package com.fmx.xiaomeng.modules.admin.service.impl;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.entity.LinkEntity;
import com.fmx.xiaomeng.modules.admin.model.CarouselModel;
import com.fmx.xiaomeng.modules.admin.repository.dao.CarouselDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.CarouselDO;
import com.fmx.xiaomeng.modules.admin.service.CarouselAdminService;
import com.fmx.xiaomeng.modules.application.service.param.CarouselPageQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("linkService")
public class CarouselAdminServiceImpl implements CarouselAdminService {
    @Autowired
    private CarouselDOMapper carouselDOMapper;

    @Override
    public PageList<LinkEntity> queryPage(CarouselPageQueryParam param) {
        return new PageList(null, new Paginator(null,1));
    }

    @Override
    public void addCarousel(CarouselModel model) {
        CarouselDO carouselDO = new CarouselDO();
        BeanUtils.copyProperties(model, carouselDO);
        carouselDOMapper.insertSelective(carouselDO);
    }


}