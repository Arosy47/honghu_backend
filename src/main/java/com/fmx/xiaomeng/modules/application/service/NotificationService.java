package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.common.enums.CommentNotificationTypeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.application.service.model.CommentModel;
import com.fmx.xiaomeng.modules.application.service.model.NotificationModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.NotificationPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.NotificationParam;

import java.util.List;

public interface NotificationService {

    /**
     * 评论通知 (如果是组局中，局长的评论不走这个接口，因为要对所有人发通知)
     */
    void commentNotice(CommentModel model, CommentNotificationTypeEnum commentType);

    NotificationModel getNoticeMessage(Long id);

    void deleteByArticleId(Long articleId);

    Long createNotification(NotificationModel model) throws BusinessException;

    void deleteById(Long id);

    void deleteByParam(NotificationParam param);

    PageList<NotificationModel> listNoticeMessage(NotificationPageQueryParam param, UserModel userModel);



    void updateReadStatus(Long targetUserId, List<Integer> noticeTypeList);

    List<Integer> getUnreadCount(Long id);

    PageList<NotificationModel> getViolationNotifications(NotificationPageQueryParam param, UserModel userModel);


    PageList<NotificationModel> getSysNotifications(NotificationPageQueryParam param, UserModel userModel);

    void updateAlreadyReadViolationNotification(Long userId);

    NotificationModel getNewestSystemNotice(NotificationParam param);
}
