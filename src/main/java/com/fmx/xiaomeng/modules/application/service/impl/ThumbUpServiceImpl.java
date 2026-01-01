package com.fmx.xiaomeng.modules.application.service.impl;

import com.fmx.xiaomeng.modules.application.repository.dao.ThumbUpDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ThumbUpDO;

import com.fmx.xiaomeng.modules.application.service.ThumbUpService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.ThumbUpModel;
import com.fmx.xiaomeng.modules.application.service.param.ThumbUpPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.ThumbUpParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Service
public class ThumbUpServiceImpl implements ThumbUpService {
    @Autowired
    ThumbUpDOMapper thumbUpDAO;



    @Autowired
    Converter converter;

    @Override
    public void addThumbUpMark(ThumbUpModel thumbUpModel) {
        thumbUpDAO.insertSelective(converter.convert(thumbUpModel));
    }

    @Override
    public ThumbUpModel queryByParam(ThumbUpParam param) {
        ThumbUpDO thumbUpDO = thumbUpDAO.queryByParam(param);
        return converter.convert(thumbUpDO);
    }

    @Override
    public void deleteByParam(ThumbUpParam thumbUpParam) {
        thumbUpDAO.deleteByParam(thumbUpParam);
    }

    @Override
    public Integer countByParam(ThumbUpPageQueryParam param) {
        return thumbUpDAO.countByParam(param);
    }

    @Override
    public void deleteThumbUpMark(ThumbUpModel thumbUpModel) {
        ThumbUpDO thumbUpDO = converter.convert(thumbUpModel);
        thumbUpDAO.delete(thumbUpDO);
    }


}
