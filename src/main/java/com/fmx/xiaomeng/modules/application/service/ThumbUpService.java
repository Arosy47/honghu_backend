package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.modules.application.service.model.ThumbUpModel;
import com.fmx.xiaomeng.modules.application.service.param.ThumbUpPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.ThumbUpParam;

public interface ThumbUpService {

    void addThumbUpMark(ThumbUpModel thumbUpModel);

    ThumbUpModel queryByParam(ThumbUpParam param);


    void deleteByParam(ThumbUpParam thumbUpParam);

    Integer countByParam(ThumbUpPageQueryParam param);

    void deleteThumbUpMark(com.fmx.xiaomeng.modules.application.service.model.ThumbUpModel thumbUpModel);
}
