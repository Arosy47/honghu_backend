package com.fmx.xiaomeng.modules.application.service.impl;

import com.fmx.xiaomeng.common.enums.CommentNotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.CommentTypeEnum;
import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.application.repository.dao.NotificationDOMapper;

import com.fmx.xiaomeng.modules.application.repository.model.NotificationDO;
import com.fmx.xiaomeng.modules.application.service.*;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.fmx.xiaomeng.modules.application.service.param.NotificationPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.NotificationParam;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
@Service
@CustomLog
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;



    @Autowired
    private NotificationDOMapper notificationDAO;

    @Autowired
    private Converter converter;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void commentNotice(CommentModel commentModel, CommentNotificationTypeEnum commentType) {
        Long userId = commentModel.getUserId();
        if (CommentNotificationTypeEnum.COMMENT_ARTICLE.equals(commentType)) {
            ArticleCommentModel articleCommentModel = (ArticleCommentModel) commentModel;
            if (commentModel.getToUserId().equals(userId)) {
                //评论的人和被评论的是一个人则返回
                return;
            }
            NotificationModel notificationModel = new NotificationModel();

            if (articleCommentModel.getReplyCommentId() == 0) { //说明是评论帖子
                notificationModel.setNoticeType(NotificationTypeEnum.COMMENT_ARTICLE.getCode());
                notificationModel.setArticleId(articleCommentModel.getArticleId());
            } else {//说明是回复评论
                notificationModel.setNoticeType(NotificationTypeEnum.REPLY.getCode());
                notificationModel.setArticleCommentId(commentModel.getReplyCommentId());
                notificationModel.setArticleId(articleCommentModel.getArticleId());
            }
            notificationModel.setUserId(userId);
            notificationModel.setTargetUserId(articleCommentModel.getToUserId());
            notificationModel.setSourceCommentId(commentModel.getId());

            notificationModel.setUserNickName(commentModel.getUserNickName());
            notificationModel.setAvatar(commentModel.getAvatar()); //帖子评论需要考虑匿名问题，匿名的话直接前端传匿名头像

            if (CommentTypeEnum.WORD.equals(commentModel.getCommentType())) {
                notificationModel.setContent(commentModel.getContent());
            } else if (CommentTypeEnum.PICTURE.equals(commentModel.getCommentType())) {
                notificationModel.setContent("[图片]");
            }
            notificationService.createNotification(notificationModel);

        }
    }

    @Override
    public NotificationModel getNoticeMessage(Long id) {
        return converter.convert(notificationDAO.selectByPrimaryKey(id));
    }


    @Override
    public void deleteByArticleId(Long articleId) {
        
    }

    @Override
    public Long createNotification(NotificationModel model) throws BusinessException {
        if (Objects.nonNull(model.getUserId()) && model.getUserId().equals(model.getTargetUserId())) {
            return null;
        }
        //校验入参
        ValidationResult result = validator.validate(model);
        if (result.isHasErrors()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        model.setCreateTime(new Date());
        NotificationDO notificationDO = converter.convert(model);
        notificationDAO.insertSelective(notificationDO);
        return notificationDO.getId();
    }

    @Override
    public void deleteById(Long id) {
        notificationDAO.deleteById(id);
    }

    //
    @Override
    public void deleteByParam(NotificationParam param) {
        notificationDAO.deleteByParam(param);
    }

    @Override
    public PageList<NotificationModel> listNoticeMessage(NotificationPageQueryParam param, UserModel userModel) {
        long total = notificationDAO.count(param);
        @NotNull PageParam pageParam = param.getPageParam();
        List<NotificationModel> notificationModelList = null;
        if (total > pageParam.getOffset()) {
            List<NotificationDO> notificationDOList = notificationDAO.pageQuery(param);
            notificationModelList = converter.convertNotificationList(notificationDOList);
        }
        List<NotificationModel> result = Optional.ofNullable(notificationModelList).orElse(Collections.emptyList()).stream()
                .peek(model -> {
                    switch (model.getNoticeType()) {
                        case 1:
                        case 3:
                        case 5:
                            ArticleModel articleModel = articleService.queryByPrimaryKey(model.getArticleId());
                            if (Objects.nonNull(articleModel) && Boolean.FALSE.equals(articleModel.getHasDelete())) {
                                model.setArticleModel(articleModel);
                            }
                            break;
                        case 2:
                        case 4:
                            if (Objects.nonNull(model.getArticleCommentId())) {
                                ArticleCommentModel articleCommentModel = commentService.queryByPrimaryKey(model.getArticleCommentId());
                                if (Objects.nonNull(articleCommentModel) && Boolean.FALSE.equals(articleCommentModel.getHasDelete())) {
                                    model.setArticleCommentModel(articleCommentModel);

                                    ArticleModel article = articleService.queryByPrimaryKey(articleCommentModel.getArticleId());
                                    if (Objects.nonNull(article)) {
                                        model.setIsOwner(model.getUserId().equals(article.getUserId()));
                                    }
                                }
                            } else {


                            }

                            break;
                        default:
                            break;
                    }
                }).collect(Collectors.toList());

//        全部更新为已读
        this.updateReadStatus(userModel.getUserId(), param.getNoticeTypeList());
        return new PageList<>(result, new Paginator(param.getPageParam(), total));
    }


    @Override
    public PageList<NotificationModel> getSysNotifications(NotificationPageQueryParam param, UserModel userModel) {
        long total = notificationDAO.count(param);
        @NotNull PageParam pageParam = param.getPageParam();
        List<NotificationModel> notificationModelList = null;
        if (total > pageParam.getOffset()) {
            List<NotificationDO> notificationDOList = notificationDAO.pageQuery(param);
            notificationModelList = converter.convertNotificationList(notificationDOList);
        }

        this.updateReadStatus(userModel.getUserId(), param.getNoticeTypeList());
        return new PageList<>(notificationModelList, new Paginator(param.getPageParam(), total));
    }


    @Override
    public void updateReadStatus(Long targetUserId, List<Integer> noticeTypeList) {
        notificationDAO.setAlreadyRead(targetUserId, noticeTypeList);
    }

    @Override
    public List<Integer> getUnreadCount(Long userId) {
        Integer unReadCommentCount = notificationDAO.countUnreadMsgCount(userId, Arrays.asList(3, 4));
        Integer unReadThumbUpOrCollectCount = notificationDAO.countUnreadMsgCount(userId, Arrays.asList(1, 2, 5));

        Integer unReadSysCount = notificationDAO.countUnreadMsgCount(userId, Arrays.asList(41, 42, 43, 44, 45, 46, 47, 48));
        return Arrays.asList(0, unReadSysCount, unReadCommentCount, unReadThumbUpOrCollectCount, 0, 0);
    }

    @Override
    public PageList<NotificationModel> getViolationNotifications(NotificationPageQueryParam param, UserModel userModel) {
        long total = notificationDAO.count(param);
        @NotNull PageParam pageParam = param.getPageParam();
        List<NotificationModel> notificationModelList = null;
        if (total > pageParam.getOffset()) {
            List<NotificationDO> notificationDOList = notificationDAO.pageQuery(param);
            notificationModelList = converter.convertNotificationList(notificationDOList);
        }

        return new PageList<>(notificationModelList, new Paginator(param.getPageParam(), total));
    }

    @Override
    public void updateAlreadyReadViolationNotification(Long targetUserId) {
        notificationDAO.updateAlreadyReadViolationNotification(targetUserId);
    }

    @Override
    public NotificationModel getNewestSystemNotice(NotificationParam param) {
        NotificationDO newestSystemNotice = notificationDAO.getNewestSystemNotice(param);
        return Objects.nonNull(newestSystemNotice) ? converter.convert(newestSystemNotice) : null;
    }






}
