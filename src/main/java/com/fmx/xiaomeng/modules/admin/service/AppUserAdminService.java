package com.fmx.xiaomeng.modules.admin.service;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.admin.response.StatisticResponse;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;

import java.util.List;

/**
 * 用户管理
 *
 * @author honghu
 * @date 2025-12-20
 */
public interface AppUserAdminService {

    /**
     * 首页数据
     * @return HomeRateResponse
     */
    StatisticResponse indexDate(Integer schoolId);

    PageList<UserModel> pageQuery(UserPageQueryParam params);

    void removeByIds(List<Long> userIds);

    void certificationPass(Long userId);

    void certificationUnpass(Long userId);

    void ban(Long userId);

    void openBan(Long userId);

    List<UserModel> listMockUser();


}

