package com.fmx.xiaomeng.modules.admin.service.impl;


import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.enums.AuthStatusEnum;
import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.admin.response.StatisticResponse;
import com.fmx.xiaomeng.modules.admin.service.AppUserAdminService;
import com.fmx.xiaomeng.modules.application.repository.dao.UserDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.UserDO;
import com.fmx.xiaomeng.modules.application.repository.model.UserDOExample;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.NotificationService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.NotificationModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.fmx.xiaomeng.modules.application.websocket.NewMessageRemindWebSocket.NEW_MSG_ONLINE_USER_ID_SET_KEY;


/**
 * @author honghu
 * @date 2025-12-20
 */
@Service
@Slf4j
public class AppUserAdminServiceImpl implements AppUserAdminService {

    @Autowired
    private UserDOMapper userDAO;


    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Converter converter;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public StatisticResponse indexDate(Integer schoolId) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();

        ArticlePageQueryParam param = new ArticlePageQueryParam();
        param.setSchoolId(schoolId);
        Long ArticleCount = articleService.count(param);

        StatisticResponse response = new StatisticResponse();
        response.setTotalPostOfReview(0L);

        response.setTotalPost(ArticleCount);
        response.setNewUserNum(this.getRegisterNumByDate(today, schoolId));
        response.setYesterdayNewUserNum(this.getRegisterNumByDate(yesterday, schoolId));
        response.setTotalUser(this.getTotalNum(schoolId));

        response.setNewArticleNum(articleService.countTodayArticleNumByDate(schoolId));
        response.setYesterdayNewArticleNum(articleService.countYesterdayArticleNumByDate(schoolId));

        response.setAuthedUserCount(this.getAuthedUserCount(schoolId));
        response.setOnlineUserCount(this.getOnlineUserCount(schoolId));
        return response;
    }

    private Long getOnlineUserCount(Integer schoolId) {
        if (Objects.isNull(schoolId)) {
            schoolId = 2;
        }
        String newMsgOnlineUserIdKey = NEW_MSG_ONLINE_USER_ID_SET_KEY + schoolId;
        return redisUtil.zsetSize(newMsgOnlineUserIdKey);
    }

    private Long getAuthedUserCount(Integer schoolId) {
        UserDOExample userDOExample = new UserDOExample();
        UserDOExample.Criteria criteria = userDOExample.createCriteria();
        if (Objects.nonNull(schoolId)) {
            criteria.andSchoolIdEqualTo(schoolId);
        }
        criteria.andAuthStatusEqualTo(3);
        return userDAO.countByExample(userDOExample);
    }

    /**
     * 分页查询用户
     *
     * @param params
     * @return
     */
    @Override
    public PageList<UserModel> pageQuery(UserPageQueryParam params) {
        long total = userDAO.count(params);
        @NotNull PageParam pageParam = params.getPageParam();
        List<UserModel> userModelList = null;
        if (total > pageParam.getOffset()) {
            List<UserDO> userDOList = userDAO.pageQuery(params);
            userModelList = converter.convertToUserList(userDOList);
        }
        return new PageList<>(userModelList, new Paginator(params.getPageParam(), total));
    }

    @Override
    public void removeByIds(List<Long> userIds) {

    }


    @Override
    public void certificationPass(Long userId) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        userDO.setAuthStatus(AuthStatusEnum.AUTHED.getCode());
        userDAO.updateByUserId(userDO);
        redisUtil.delete("userId:" + userId);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setNoticeType(NotificationTypeEnum.CERTIFICATION_PASS.getCode());
        notificationModel.setContent("恭喜认证已通过");
        notificationModel.setTargetUserId(userId);
        notificationModel.setCreateTime(new Date());
        notificationService.createNotification(notificationModel);
    }

    @Override
    public void certificationUnpass(Long userId) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        userDO.setAuthStatus(AuthStatusEnum.UNAUTH.getCode());
        userDAO.updateByUserId(userDO);
        redisUtil.delete("userId:" + userId);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setNoticeType(NotificationTypeEnum.CERTIFICATION_UNPASS.getCode());
        notificationModel.setContent("认证未通过，请重新认证");
        notificationModel.setTargetUserId(userId);
        notificationModel.setCreateTime(new Date());
        notificationService.createNotification(notificationModel);

    }

    @Override
    public void ban(Long userId) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        userDO.setAccountStatus(AccountStatusEnum.NO_EDIT.getCode());
        userDAO.updateByUserId(userDO);
        redisUtil.delete("userId:" + userId);
    }

    @Override
    public void openBan(Long userId) {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        userDO.setAccountStatus(AccountStatusEnum.NO_EDIT.getCode());
        userDAO.updateByUserId(userDO);
    }

    @Override
    public List<UserModel> listMockUser() {

        return converter.convertToUserList(userDAO.listMockUser());
    }


    private Long getRegisterNumByDate(Date date, Integer schoolId) {
        UserDOExample userDOExample = new UserDOExample();
        UserDOExample.Criteria criteria = userDOExample.createCriteria();

        if (Objects.nonNull(schoolId)) {
            criteria.andSchoolIdEqualTo(schoolId);
        }

        TimeZone curTimeZone = TimeZone.getTimeZone("GMT+8");
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();

        criteria.andCreateTimeBetween(start, end);

        return userDAO.countByExample(userDOExample);
    }

    private Long getTotalNum(Integer schoolId) {
        UserDOExample userDOExample = new UserDOExample();
        UserDOExample.Criteria criteria = userDOExample.createCriteria();
        if (Objects.nonNull(schoolId)) {
            criteria.andSchoolIdEqualTo(schoolId);
        }
        return userDAO.countByExample(userDOExample);
    }

}
