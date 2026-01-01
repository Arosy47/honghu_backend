package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.model.WXSessionModel;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * Created by hzllb on 2018/11/11.
 */
public interface UserService {

    WXSessionModel login(String code, UserModel model) throws BusinessException;

    String certificatePhone(String code, UserModel model) throws BusinessException, WxErrorException;


    //通过用户ID获取用户对象的方法
    /**
     * 获取用户信息
     */
    UserModel getUserInfo(Long userId);

    //获取自己全部信息
    UserModel getAllUserInfo(Long id);



    UserModel queryByOpenId(String openId);

    UserModel queryByUnionId(String unionId);

    PageList<UserModel> pageQuery(UserPageQueryParam params);


    void changeUserInfo(UserModel userModel);


    void addViolationTimes(Long userId);

    void authenticate(UserModel userModel);

    void updateStudentAccount(UserModel userModel);


    void createMockUser(Integer num);

    void initHistoryUserAnonymousAvatar();

    void banUser(Long userId);
    void unbanUser(Long userId);

    void blackUser(Long userId, Long blackedUserId);
    void unblackUser(Long userId, Long blackedUserId);
    Boolean isBlacked(Long userId, Long blackedUserId);

    /**
     * 设置用户蓝V状态（管理员操作）
     */
    void setBlueV(Long userId, Boolean blueV);

    Integer indexDate(Integer days);
}
