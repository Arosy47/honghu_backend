package com.fmx.xiaomeng.modules.application.service.impl;

import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.ThumbUpTypeEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.exception.ExceptionResolver;
import com.fmx.xiaomeng.common.utils.*;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.application.model.WXProperties;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleCommentDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleCommentDO;
import com.fmx.xiaomeng.modules.application.repository.model.ReportRecordDO;
import com.fmx.xiaomeng.modules.application.service.*;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.fmx.xiaomeng.modules.application.service.param.*;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.*;


@Service
@CustomLog
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class CommentServiceImpl implements CommentService {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @Resource
    private NotificationService notificationService;


    @Autowired
    private ThumbUpService thumbUpService;

    @Resource
    private WXProperties wxProperties;

    @Autowired
    private Converter converter;

    @Autowired
    private ArticleCommentDOMapper commentDAO;

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Autowired
    private EnvUtil envUtil;


    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Override
    @Transactional
    public Long createComment(ArticleCommentModel articleCommentModel, UserModel userModel) throws BusinessException {
        String lockKey = String.format("%s==createComment", articleCommentModel.getArticleId());
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createComment, tryLock:%s failed", lockKey));
            }
            ValidationResult result = validator.validate(articleCommentModel);
            if (result.isHasErrors()) {
                throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
            }

            if (Boolean.TRUE.equals(articleCommentModel.getAnonymous())) {
                ArticleModel articleModel = articleService.queryByPrimaryKey(articleCommentModel.getArticleId());
                if(articleModel.getUserId().equals(userModel.getUserId())){
                    articleCommentModel.setAvatar(articleModel.getAvatar());
                    articleCommentModel.setUserNickName(articleModel.getAnonymousName());
                }else {
                    articleCommentModel.setAvatar(userModel.getAnonymousAvatar().getUrl());
                    articleCommentModel.setUserNickName(userModel.getAnonymousName());
                }
            } else {
                articleCommentModel.setAvatar(Objects.nonNull(userModel.getAvatar()) ? userModel.getAvatar().getUrl() : null);
                articleCommentModel.setUserNickName(userModel.getNickName());
            }
            if (userModel.getAdmin()) {
                articleCommentModel.setAdmin(true);
            }

            articleCommentModel.setCreateTime(new Date());

            if(Objects.isNull(articleCommentModel.getSchoolId())){
                articleCommentModel.setSchoolId(2);
            }

            Long commentId = this.insert(articleCommentModel);
            articleCommentModel.setId(commentId);

            articleService.plusCommentCount(articleCommentModel.getArticleId());


            EventBus.emit(EventBus.Topic.ARTICLE_COMMENT_CREATE, articleCommentModel);

            return commentId;

        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createComment, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_COMMENT_ERROR);
            throw resolver.decorateException("articleId: {}, commentId: {}",
                    articleCommentModel.getArticleId(), articleCommentModel.getId());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public PageList<ArticleCommentModel> pageQuery(CommentPageQueryParam param, Long userId) {
        long total = commentDAO.count(param);
        List<ArticleCommentModel> articleCommentModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<ArticleCommentDO> articleCommentDOList = commentDAO.pageQuery(param);
            articleCommentModelList = articleCommentDOList.stream().map(commentDO -> {
                ArticleCommentModel articleCommentModel = converter.convert(commentDO);

                if (Objects.nonNull(userId) && userId != -1L) {
                    ThumbUpParam thumbUpParam = new ThumbUpParam();
                    thumbUpParam.setUserId(userId);
                    thumbUpParam.setCommentId(commentDO.getId());
                    thumbUpParam.setThumbUpType(ThumbUpTypeEnum.COMMENT.getCode());
                    ThumbUpModel thumbUpModel = thumbUpService.queryByParam(thumbUpParam);
                    if (Objects.isNull(thumbUpModel)) {
                        articleCommentModel.setThumbUpStatus(false);
                    } else {
                        articleCommentModel.setThumbUpStatus(true);
                    }
                }

                return articleCommentModel;
            }).collect(Collectors.toList());
        }

        return new PageList<>(articleCommentModelList, new Paginator(param.getPageParam(), total));
    }


    @Override
    public ArticleCommentModel queryByPrimaryKey(Long id) {
        ArticleCommentDO articleCommentDO = commentDAO.selectByPrimaryKey(id);

        if (Objects.isNull(articleCommentDO)) {
            return null;
        }
        ArticleCommentModel articleCommentModel = converter.convert(articleCommentDO);

        return articleCommentModel;

    }

    @Override
    @Deprecated
    public List<ArticleCommentModel> queryArticleCommentByParam(Long userId, CommentParam param) {
        //返回点赞量和是否已点赞
        List<ArticleCommentDO> articleCommentDOList = commentDAO.queryByParam(param);
        articleCommentDOList.stream().map(commentDO -> {
            ArticleCommentModel articleCommentModel = converter.convert(commentDO);
            ThumbUpPageQueryParam thumbUpParam = new ThumbUpPageQueryParam();
            thumbUpParam.setCommentId(commentDO.getId());
            //统计点赞数

            Integer count = thumbUpService.countByParam(thumbUpParam);
            articleCommentModel.setThumbUpCount(count);
            //是否已点赞
            if (Objects.nonNull(userId)) {
                thumbUpParam.setUserId(userId);
                thumbUpParam.setThumbUpType(ThumbUpTypeEnum.COMMENT.getCode());
                ThumbUpModel thumbUpModel = thumbUpService.queryByParam(thumbUpParam);
                if (Objects.nonNull(thumbUpModel)) {
                    articleCommentModel.setThumbUpStatus(true);
                } else {
                    articleCommentModel.setThumbUpStatus(false);
                }
            }
            return articleCommentModel;
        }).collect(Collectors.toList());
        return null;
    }

    @Override
    public Long insert(ArticleCommentModel model) throws BusinessException {
        ValidationResult result = validator.validate(model);
        if (result.isHasErrors()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        ArticleCommentDO commentDO = converter.convert(model);
        commentDAO.insertSelective(commentDO);
        return commentDO.getId();
    }


    @Override
    public void deleteByArticleId(Long articleId) {
        CommentParam param = new CommentParam();
        param.setArticleId(articleId);
        int affectedRow = commentDAO.deleteByParam(param);
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        CommentParam param = new CommentParam();
        param.setId(commentId);
        commentDAO.deleteByParam(param);
    }


    @Override
    public void deleteComment(Long commentId, Long userId) {
        CommentParam param = new CommentParam();
        param.setId(commentId);
        commentDAO.deleteByParam(param);

        //删除这条评论 发出的通知,  那就要知道是给谁发的，targetUserId
        NotificationParam notificationParam = new NotificationParam();
        notificationParam.setSourceCommentId(commentId);
        notificationParam.setUserId(userId);

        notificationService.deleteByParam(notificationParam);
    }


    @Override
    public void thumbUpComment(Long articleId, Long commentId, UserModel userModel, Long targetUserId, Boolean thumbUpStatus) throws BusinessException {

//        点赞记录不用保存吧，内容模型里面的点在数加一即可，收藏才需要保存，收藏了用户可以在自己收藏中看到
//        暂时模型简单点，不用保存点赞用户列表，只统计个数就行了
//        ThumbUpModel thumbUpModel = new ThumbUpModel();

//        if(userId.equals(targetUserId)){
////            不能给自己点赞，涉及到后面发消息，给自己点赞也不能创建新通知
//            return;
//        }
        Long userId = userModel.getUserId();

        String lockKey = String.format("%s==thumbUpComment", commentId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("thumbUpComment, tryLock:%s failed", lockKey));
            }

            ThumbUpPageQueryParam thumbUpParam = new ThumbUpPageQueryParam();
//            thumbUpParam.setArticleId(articleId);
            thumbUpParam.setCommentId(commentId);
            thumbUpParam.setUserId(userId);
            thumbUpParam.setThumbUpType(ThumbUpTypeEnum.COMMENT.getCode());
            ThumbUpModel existModel = thumbUpService.queryByParam(thumbUpParam);

//            同一个评论，同一个用户，如果两次点赞都是true，就会进入else逻辑，删除，所以要防止这个问题
            if (Boolean.TRUE.equals(thumbUpStatus)) {
                if (Objects.nonNull(existModel)) {
//                    如果还是点赞请求，且已经有点赞记录了，就直接返回
                    return;
                }
                this.plusThumbUpCount(commentId);
                //记录点赞
                ThumbUpModel thumbUpModel = new ThumbUpModel();
                thumbUpModel.setCommentId(commentId);
                thumbUpModel.setUserId(userId);
                thumbUpModel.setToUserId(targetUserId);
                thumbUpModel.setCreateTime(new Date());
                thumbUpModel.setThumbUpType(ThumbUpTypeEnum.COMMENT.getCode());
                thumbUpService.addThumbUpMark(thumbUpModel);

                // 如果是点赞自己的评论则不通知
                if (!userId.equals(targetUserId)) {
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setArticleId(articleId);
                    notificationModel.setArticleCommentId(commentId);
                    notificationModel.setTargetUserId(targetUserId);
                    notificationModel.setNoticeType(NotificationTypeEnum.COMMENT_THUMB_UP.getCode());
                    notificationModel.setUserId(userId);

                    notificationModel.setUserNickName(userModel.getNickName());
                    notificationModel.setAvatar(Objects.nonNull(userModel.getAvatar()) ? userModel.getAvatar().getUrl() : (String)null);

                    notificationModel.setContent("");
                    //创建消息通知
                    notificationService.createNotification(notificationModel);
                    EventBus.emitSync(EventBus.Topic.THUMB_UP, thumbUpModel);
                }
            } else if (Objects.nonNull(existModel)) {
                //取消点赞删除通知
                this.minusThumbUpCount(commentId);
                //取消点赞删除通知
                NotificationParam param = new NotificationParam();
                //userId和articleId和type可以唯一确定一条记录
                param.setArticleCommentId(commentId);
                param.setUserId(userId);
                param.setTargetUserId(targetUserId);
                param.setNoticeType(NotificationTypeEnum.COMMENT_THUMB_UP.getCode());
                notificationService.deleteByParam(param);

                //删除点赞记录
                thumbUpService.deleteByParam(thumbUpParam);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("thumbUpComment, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, THUMB_UP_COMMENT_ERROR);
            throw resolver.decorateException("articleId: {}, commentId: {}", articleId, commentId);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Long countByUserId(Long userId) {
        CommentPageQueryParam param = new CommentPageQueryParam();
        param.setHasDelete(false);
        param.setUserId(userId);
        return commentDAO.count(param);
    }


    @Override
    public void plusThumbUpCount(Long commentId) {
        String lockKey = String.format("updateComment==%s", commentId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusThumbUpCount, tryLock:%s failed", lockKey));
            }

            commentDAO.plusThumbUpCount(commentId);
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusThumbUpCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UPDATE_COMMENT_ERROR);
            throw resolver.decorateException("commentId: {}", commentId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void minusThumbUpCount(Long commentId) {
        String lockKey = String.format("updateComment==%s", commentId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusThumbUpCount, tryLock:%s failed", lockKey));
            }

            commentDAO.minusThumbUpCount(commentId);
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusThumbUpCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UPDATE_COMMENT_ERROR);
            throw resolver.decorateException("commentId: {}", commentId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }




    @Override
    public void reportSuccess(Long commentId, ReportRecordDO recordDO){

        String lockKey = String.format("updateComment==%s", commentId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("reportSuccess, tryLock:%s failed", lockKey));
            }

            ArticleCommentModel articleCommentModel = queryByPrimaryKey(commentId);
            if(Objects.nonNull(articleCommentModel) && Boolean.FALSE.equals(articleCommentModel.getHasDelete())) {
                this.deleteByCommentId(commentId);
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setNoticeType(NotificationTypeEnum.COMMENT_VIOLATION.getCode());
                notificationModel.setContent("您的评论被举报，确认违规，请注意言论，多次违规将会封号");
                notificationModel.setTargetUserId(recordDO.getReportedUserId());
                notificationModel.setCreateTime(new Date());
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("reportSuccess, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("commentId: {}", commentId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
