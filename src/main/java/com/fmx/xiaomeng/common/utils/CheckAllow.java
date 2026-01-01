package com.fmx.xiaomeng.common.utils;

import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.modules.application.model.WXSessionModel;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 检查是否拉黑
 */
public class CheckAllow {

    public static Long checkAllow(UserService userService, HttpServletRequest request) {
        WXSessionModel user = (WXSessionModel) request.getSession().getAttribute("user");

        // TODO: 2023/2/18 检查是否允许这个操作可以暂时去掉，以后是必须的，用户禁言这种操作，  现在加缓存，
        UserModel userModel = userService.getUserInfo(user.getUserId());
        if (Objects.isNull(userModel) || (Objects.nonNull(userModel) && AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus()))) {
            return -1L;
        }
        return user.getUserId();
    }
//
//    public static UserMessage getUserMessage(UserMessageOperationService userMessageOperationService, HttpServletRequest request) {
//        WXSessionModel user = (WXSessionModel) request.getSession().getAttribute("user");
//        UserMessage userMessage = userMessageOperationService.getById(user.getUserId());
//        if (userMessage.getUserAllow().equals("2")) {
//            return null;
//        }
//        return userMessage;
//    }
}
