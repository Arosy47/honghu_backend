package com.fmx.xiaomeng.modules.application.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.shortlink.GenerateShortLinkRequest;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.alibaba.fastjson.TypeReference;
import com.fmx.xiaomeng.common.enums.*;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.exception.ExceptionResolver;
import com.fmx.xiaomeng.common.utils.*;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.admin.service.ArticleAdminService;
import com.fmx.xiaomeng.modules.application.repository.dao.*;
import com.fmx.xiaomeng.modules.application.repository.model.*;
import com.fmx.xiaomeng.modules.application.service.*;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.fmx.xiaomeng.modules.application.service.param.*;
import com.fmx.xiaomeng.modules.application.utils.CurrentUser;
import com.fmx.xiaomeng.modules.systemsetting.service.BgConfigService;
import com.google.common.collect.Lists;
import lombok.CustomLog;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.mybatis.spring.SqlSessionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fmx.xiaomeng.common.enums.NotificationTypeEnum.ARTICLE_THUMB_UP;
import static com.fmx.xiaomeng.common.enums.NotificationTypeEnum.COLLECT_ARTICLE;
import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.*;

@Service
@CustomLog
/**
 * @author honghu
 * @date 2025-12-20
 */
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    // @Autowired
    // private SchoolService schoolService;
    @Autowired
    private ThumbUpService thumbUpService;
    @Autowired
    private DingDOMapper dingDOMapper;
    @Autowired
    private Converter converter;
    @Autowired
    private ArticleDOMapper articleDOMapper;



    // @Autowired
    // private SecondHandDOMapper secondHandDOMapper;

    @Autowired
    private CollectService collectService;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private RedissonLockUtil redissonLockUtil;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ArticleCategoryDOMapper categoryDOMapper;
    @Resource
    private CollectDOMapper collectMapper;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private WxMsgSubscribeService wxMsgSubscribeService;

    @Autowired
    private SubscribeMessageUtil subscribeMessageUtil;

    @Autowired
    private BgConfigService bgConfigService;

    @Autowired
    private ArticleAdminService articleAdminService;

    @Override
    @Transactional
    public void createArticle(ArticleModel articleModel) throws BusinessException {
        String lockKey = String.format("%s==createArticle", articleModel.getUserId());
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createArticle, tryLock:%s failed", lockKey));
            }
            ValidationResult result = validator.validate(articleModel);
            if (result.isHasErrors()) {
                throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
            }

            articleModel.setCreateTime(new Date());
            articleModel.setModifiedTime(new Date());

            ArticleDO articleDO = converter.convert(articleModel);
            articleDOMapper.insertSelective(articleDO);

            EventBus.emit(EventBus.Topic.ARTICLE_CREATE, converter.convert(articleDO));

        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_ARTICLE_ERROR);
            throw resolver.decorateException("userId: {}", articleModel.getUserId());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public ArticleModel detail(Long articleId) throws BusinessException {
        ArticleModel articleModel = redisUtil.get("article_detail==" + articleId, ArticleModel.class);
        if (articleModel == null) {
            articleModel = this.queryByPrimaryKey(articleId);
            if (articleModel == null) {
                return null;
            }
            this.fillArticleModel(articleModel);

            redisUtil.set("article_detail==" + articleId, articleModel, 60 * 10);
        }
        return articleModel;
    }



    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        redisUtil.delete("article_detail==" + articleId);
        articleDOMapper.delete(articleId);

        EventBus.emit(EventBus.Topic.ARTICLE_DELETE, articleId);
    }

    @Override
    @Transactional
    public void thumbUpArticle(Long articleId, UserModel userModel, Long targetUserId, Boolean thumbUpStatus) throws BusinessException {

        Long userId = userModel.getUserId();

        String lockKey = String.format("%s==thumbUpArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("thumbUpArticle, tryLock:%s failed", lockKey));
            }

            ThumbUpParam thumbUpParam = new ThumbUpParam();
            thumbUpParam.setArticleId(articleId);
            thumbUpParam.setUserId(userId);
            thumbUpParam.setThumbUpType(ThumbUpTypeEnum.ARTICLE.getCode());
            ThumbUpModel existModel = thumbUpService.queryByParam(thumbUpParam);
            if (Boolean.TRUE.equals(thumbUpStatus)) {
                if (Objects.nonNull(existModel)) {
                    return;
                }
                //点赞
                this.plusThumbUpCount(articleId);
                //记录点赞
                ThumbUpModel thumbUpModel = new ThumbUpModel();
                thumbUpModel.setArticleId(articleId);
                thumbUpModel.setUserId(userId);
                thumbUpModel.setToUserId(targetUserId);
                thumbUpModel.setCreateTime(new Date());
                thumbUpModel.setThumbUpType(ThumbUpTypeEnum.ARTICLE.getCode());
                thumbUpService.addThumbUpMark(thumbUpModel);

                if (!userId.equals(targetUserId)) {
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setArticleId(articleId);
                    notificationModel.setUserId(userId);
                    notificationModel.setUserNickName(userModel.getNickName());
                    notificationModel.setAvatar(Objects.nonNull(userModel.getAvatar()) ? userModel.getAvatar().getUrl() : null);

                    notificationModel.setCreateTime(new Date());
                    notificationModel.setAlreadyRead(Boolean.FALSE);

                    notificationModel.setTargetUserId(targetUserId);
                    notificationModel.setNoticeType(ARTICLE_THUMB_UP.getCode());
                    notificationModel.setCreateTime(new Date());
                    notificationModel.setContent("");
                    //创建消息通知
                    notificationService.createNotification(notificationModel);
                    EventBus.emitSync(EventBus.Topic.THUMB_UP, thumbUpModel);
                }
            } else if (Objects.nonNull(existModel)) {
                this.minusThumbUpCount(articleId);
                //删除通知
                NotificationParam param = new NotificationParam();
                param.setArticleId(articleId);
                param.setUserId(userId);
                param.setTargetUserId(targetUserId);
                param.setNoticeType(ARTICLE_THUMB_UP.getCode());
                notificationService.deleteByParam(param);
                //删除点赞记录
                thumbUpService.deleteByParam(thumbUpParam);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("thumbUpArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, THUMB_UP_ARTICLE_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void collectArticle(Long articleId, UserModel userModel, Long targetUserId, Boolean collectStatus) throws BusinessException {

        Long userId = userModel.getUserId();

        String lockKey = String.format("%s==collectArticle", articleId);
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("collectArticle, tryLock:%s failed", lockKey));
            }

            CollectParam collectParam = new CollectParam();
            collectParam.setUserId(userId);
            collectParam.setArticleId(articleId);
            List<CollectDO> existModel = collectMapper.select(collectParam);

            if (Boolean.TRUE.equals(collectStatus) && CollectionUtils.isEmpty(existModel)) {
                this.plusCollectCount(articleId);
                CollectDO collect = new CollectDO();
                collect.setArticleId(articleId);
                collect.setUserId(userId);
                collect.setCreateTime(new Date());
                collect.setToUserId(targetUserId);

                collectMapper.insertSelective(collect);

                if (!userId.equals(targetUserId)) {
                    NotificationModel notificationModel = new NotificationModel();
                    notificationModel.setUserId(userModel.getUserId());
                    notificationModel.setTargetUserId(targetUserId);
                    notificationModel.setArticleId(articleId);
                    notificationModel.setUserNickName(userModel.getNickName());
                    notificationModel.setAvatar(userModel.getAvatar() != null ? userModel.getAvatar().getUrl() : null);
                    notificationModel.setAlreadyRead(Boolean.FALSE);
                    notificationModel.setNoticeType(COLLECT_ARTICLE.getCode());
                    notificationModel.setCreateTime(new Date());
                    notificationModel.setContent("");
                    notificationService.createNotification(notificationModel);
                    EventBus.emitSync(EventBus.Topic.COLLECT_ARTICLE, converter.convert(collect));
                }
            } else if (!CollectionUtils.isEmpty(existModel)) {
                this.minusCollectCount(articleId);
                NotificationParam param = new NotificationParam();
                param.setArticleId(articleId);
                param.setUserId(userId);
                param.setTargetUserId(targetUserId);
                param.setNoticeType(COLLECT_ARTICLE.getCode());
                notificationService.deleteByParam(param);

                collectMapper.delete(collectParam);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("collectArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, COLLECT_ARTICLE_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional
    public void dingArticle(Long articleId, Long userId) throws BusinessException {

        String lockKey = String.format("%s==dingArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("dingArticle, tryLock:%s failed", lockKey));
            }

            DingParam dingParam = new DingParam();
            dingParam.setArticleId(articleId);
            dingParam.setUserId(userId);
            List<DingDO> existModel = dingDOMapper.queryByParam(dingParam);
            if (CollectionUtils.isEmpty(existModel)) {
                //若没ding
                DingDO dingDO = new DingDO();
                dingDO.setArticleId(articleId);
                dingDO.setUserId(userId);
                dingDO.setCreateTime(new Date());
                dingDOMapper.insertSelective(dingDO);
            } else {
                //若已钉，取消钉
                dingDOMapper.delete(dingParam);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("dingArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, DING_ARTICLE_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public ArticleModel queryByPrimaryKey(Long articleId) {
        ArticleDO articleDO = articleDOMapper.selectByPrimaryKey(articleId);
        if (Objects.isNull(articleDO)) {
            return null;
        }
        ArticleModel articleModel = converter.convert(articleDO);
        return articleModel;

    }

    /**
     * 会查匿名，置顶
     *
     * @param param
     * @return
     */
    @Override
    public PageList<ArticleModel> listArticle(ArticlePageQueryParam param) {


        if (Objects.isNull(param.getCategory())) {
            //   category==null说明是首页查询，置顶帖子后面会单独查询，所以这里分页查询的时候只查非置顶的
            param.setTop(false);
        }

        long total = articleDOMapper.countByParam(param);
        List<ArticleModel> articleModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<ArticleDO> articleDOList = articleDOMapper.pageQuery(param);
            articleModelList = converter.convertArticleList(articleDOList);


            // 首页查询才查置顶  在pagequery一下直接查出来
            if (Objects.isNull(param.getCategory()) && param.getPageParam().getPage() == 1
                    && Objects.isNull(param.getFunctionType())) {

                List<ArticleModel> articleModels = this.topArticle(param.getSchoolId());
                if (CollectionUtils.isNotEmpty(articleModels)) {
                    Optional.of(articleModels).orElse(Collections.emptyList()).stream().peek(this::fillArticleModel);
                    articleModels.addAll(articleModelList);
                    articleModelList = articleModels;
                }
            }

            //还需要用户和学校信息
            articleModelList.forEach(model -> {
                this.fillArticleModel(model);
            });

        }

        return new PageList<>(articleModelList, new Paginator(param.getPageParam(), total));
    }

    /**
     * 不单独查置顶
     *
     * @param param
     * @return
     */
    @Override
    public PageList<ArticleModel> listArticleWithoutTop(ArticlePageQueryParam param) {

        long total = articleDOMapper.countByParam(param);
        List<ArticleModel> articleModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<ArticleDO> articleDOList = articleDOMapper.pageQuery(param);
            articleModelList = converter.convertArticleList(articleDOList);
            //还需要用户和学校信息
            articleModelList.forEach(model -> {
                this.fillArticleModel(model);
            });
        }

        return new PageList<>(articleModelList, new Paginator(param.getPageParam(), total));
    }

    public void fillArticleModel(ArticleModel model) {
        UserModel userInfo = userService.getUserInfo(model.getUserId());
        model.setAdmin(userInfo.getAdmin());
        model.setBlueV(userInfo.getBlueV());

        UserModel user = currentUser.getUser();


        if (Objects.nonNull(user)) {
            Long articleId = model.getId();
            Long userId = user.getUserId();
            ThumbUpParam thumbUpParam = new ThumbUpParam();
            thumbUpParam.setArticleId(articleId);
            thumbUpParam.setThumbUpType(ThumbUpTypeEnum.ARTICLE.getCode());
            thumbUpParam.setUserId(userId);
            ThumbUpModel existModel = thumbUpService.queryByParam(thumbUpParam);
            model.setThumbUpStatus(Objects.nonNull(existModel));

            CollectModel collectModel = collectService.selectByArticleIdAndUserId(articleId, userId);
            model.setCollectStatus(Objects.nonNull(collectModel));
        }
    }

//    private void fillSecondHandArticle(ArticleModel model, Long userId) {
//        SecondHandDO secondHandDO = secondHandDOMapper.queryByArticleId(model.getId());
//        SecondHandModel secondHandModel = converter.convert(secondHandDO);
//        model.setSecondHand(secondHandModel);
//
//    }

    @Override
    public void plusThumbUpCount(Long articleId) {
        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusThumbUpCount, tryLock:%s failed", lockKey));
            }
            redisUtil.delete("article_detail==" + articleId);
            articleDOMapper.plusThumbUpCount(articleId);

        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusThumbUpCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }


    @Override
    public void minusThumbUpCount(Long articleId) {
        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusThumbUpCount, tryLock:%s failed", lockKey));
            }
            redisUtil.delete("article_detail==" + articleId);
            articleDOMapper.minusThumbUpCount(articleId);
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusThumbUpCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void plusCommentCount(Long articleId) {
        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusCommentCount, tryLock:%s failed", lockKey));
            }
            redisUtil.delete("article_detail==" + articleId);
            articleDOMapper.plusCommentCount(articleId);
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusCommentCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void minusCommentCount(Long articleId) {

    }

    @Override
    public void plusCollectCount(Long articleId) {
        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusCollectCount, tryLock:%s failed", lockKey));
            }
            redisUtil.delete("article_detail==" + articleId);
            articleDOMapper.plusCollectCount(articleId);
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusCollectCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void minusCollectCount(Long articleId) {
        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusCollectCount, tryLock:%s failed", lockKey));
            }

            redisUtil.delete("article_detail==" + articleId);
            articleDOMapper.minusCollectCount(articleId);

        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("minusCollectCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void plusViewCount(Long articleId, Integer count) {

        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusViewCount, tryLock:%s failed", lockKey));
            }

            redisUtil.delete("article_detail==" + articleId);
            Random random = new Random();
            articleDOMapper.plusViewCount(articleId, count, count * (random.nextInt(7) + 12)); //12-18


        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("plusViewCount, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void update(ArticleModel articleModel) {
        String lockKey = String.format("%s==updateArticle", articleModel.getId());
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("update, tryLock:%s failed", lockKey));
            }

//        articleModel.setModifiedTime(new Date());
            Integer thumbUpCount = articleModel.getThumbUpCount();
            Integer viewCount = articleModel.getViewCount();
            Integer commentCount = articleModel.getCommentCount();
            int hot = thumbUpCount * 20 + viewCount / 15 + commentCount * 100;
            articleModel.setHot((long) hot);

//      管理后台不动这个值
            articleModel.setCommentCount(null);
            articleDOMapper.updateByPrimaryKeySelective(converter.convert(articleModel));





//        if (Boolean.TRUE.equals(articleModel.getTop())) {
//            String key = "TOP_ARTICLE_" + articleModel.getSchoolId();
//            redisUtil.delete(key);
//        }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("update, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleModel.getId());
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }


    }

    @Override
    public Long count(ArticlePageQueryParam param) {
        return articleDOMapper.countByParam(param);
    }

    public Long countYesterdayArticleNumByDate(Integer schoolId) {
        return articleDOMapper.countYesterdayArticleNum(schoolId);
    }

    public Long countTodayArticleNumByDate(Integer schoolId) {
        return articleDOMapper.countTodayArticleNum(schoolId);
    }

    @Override
    public List<ArticleModel> topArticle(Integer schoolId) {
        // TODO: 2023/4/11   先查询该学校管理员对应的userId；  不用非要管理员发的帖子，如果有的学生有特别着急的事儿，可以帮忙置顶
//        Long adminUserId = 7777777L;
//        return converter.convertArticleList(articleDOMapper.topArticle(adminUserId, schoolId));
        return converter.convertArticleList(articleDOMapper.topArticle(schoolId));
    }

    @Override
    public List<ArticleModel> topTen(String period, Integer schoolId) {
        // TODO: 2024/3/1 这里完全依赖缓存，还要考虑帖子删除的情况（包括违规）

        List<ArticleModel> articleModelList = new ArrayList<>();
        if (period.equals("TOP_TEN_DAY")) {
            // 从缓存里取
            articleModelList = redisUtil.get("TOP_TEN_DAY" + schoolId, new TypeReference<List<ArticleModel>>() {
            }.getType());

            if (CollectionUtils.isEmpty(articleModelList)) {
//                有了deleteArticle里面的兜底逻辑，基本不会走到这一步，除非并发很高
                this.topTenSchedule();
                articleModelList = redisUtil.get("TOP_TEN_DAY" + schoolId, new TypeReference<List<ArticleModel>>() {
                }.getType());
            }

        } else if (period.equals("TOP_TEN_WEEK")) {
            //        从缓存里取
            articleModelList = redisUtil.get("TOP_TEN_WEEK" + schoolId, new TypeReference<List<ArticleModel>>() {
            }.getType());

        } else if (period.equals("TOP_TEN_MONTH")) {
            // 从缓存里取
            articleModelList = redisUtil.get("TOP_TEN_MONTH" + schoolId, new TypeReference<List<ArticleModel>>() {
            }.getType());

        }

        return articleModelList;
    }

    /**
     * 每30分钟更新一次
     */
//    @Scheduled(cron = "0 */1 * * * ?") //每1分钟更新一次
    // @Scheduled(cron = "0 0/20 * * * ?") //每20分钟更新一次
    // public void topTenSchedule() {
    //     String lockKey = "topTenSchedule";
    //     RLock lock = redissonLockUtil.lock(lockKey);
    //     try {
    //         boolean locked = lock.tryLock(20, TimeUnit.SECONDS);
    //         if (!locked) {
    //             return;
    //         }
    //         // removed school-based ranking aggregation
    //     } catch (Exception e) {
    //         throw new BusinessException(
    //                 ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("topTenSchedule, tryLock:%s failed", lockKey));
    //     } finally {
    //         if (lock.isLocked() && lock.isHeldByCurrentThread()) {
    //             lock.unlock();
    //         }
    //     }
    // }


    /**
     * 每天10点执行一次
     */
    // public void sendHotArticleTemplateMsg() {
    //     String lockKey = String.format("sendHotArticleTemplateMsg");
    //     RLock lock = redissonLockUtil.lock(lockKey);
    //     try {
    //         boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
    //         if (!locked) {
    //             throw new BusinessException(
    //                     ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("sendHotArticleTemplateMsg, tryLock:%s failed", lockKey));
    //         }
    //         // removed school-based template message dispatch
    //     } catch (InterruptedException e) {
    //         throw new BusinessException(
    //                 ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("sendHotArticleTemplateMsg, tryLock:%s failed", lockKey));
    //     } catch (Exception e) {
    //         ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_ARTICLE_ERROR);
    //         throw resolver.decorateException("");
    //     } finally {
    //         if (lock.isLocked() && lock.isHeldByCurrentThread()) {
    //             lock.unlock();
    //         }
    //     }
    // }

    private void judgeAndSend(ArticleModel articleModel) {

        // TODO: 2025/1/19 mysql不适合用Cursor便利，影响其他事务读写
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        String scanStmt = "com.fmx.xiaomeng.modules.application.repository.dao.UserDOMapper.scan";
        Cursor<UserDO> cursor = sqlSession.selectCursor(scanStmt);
//        String page = "/pages/topic/index?topicId=" + articleModel.getId() + "&shareUserId=7777777"; //这里的shareUserId只是为了兼容分享的情况，通过消息通知点开后也是先到主页再到明细
        String page = "/pages/topic/index?topicId=" + articleModel.getId(); //不需要shareUserId，有topicId就会先到主页再到明细
        for (UserDO userDO : cursor) {
            if (!userDO.getUserId().equals(7777777L)) {
                continue;
            }
            WxMsgSubscribeRecordModel record = wxMsgSubscribeService.query(userDO.getUserId(), WxMsgTemplateTypeEnum.HOT_ARTICLE);
            if (Objects.nonNull(record) && record.getSubscribe() && record.getLeftTimes() > 0) {
                this.subscribeMessageUtil.sendSubscribeMessage(userDO.getUserId(), WxMaSubscribeMessage.builder()
                        .templateId(record.getTemplateId())
                        .lang(WxMaConstants.MiniProgramLang.ZH_CN)
                        .miniprogramState(WxMaConstants.MiniProgramState.FORMAL)
                        .data(
                                Lists.newArrayList(
                                        new WxMaSubscribeMessage.MsgData("date2", DateUtil.dateToStr(articleModel.getCreateTime(), DateUtil.DATE_FORMAT_HHMM)),
                                        new WxMaSubscribeMessage.MsgData("name3", articleModel.getUserNickName()),
                                        new WxMaSubscribeMessage.MsgData("thing4", articleModel.getContent().length() <= 15 ?
                                                articleModel.getContent() :
                                                StringUtils.substring(articleModel.getContent(), 0, 13) + "..")

                                )
                        )
//                                    new WxMaSubscribeMessage.MsgData("time4",time)))
                        .toUser(userDO.getOpenId())
                        .page(page)
                        .build());

            }

        }
    }

    /**
     * 每30分钟更新一次
     */
//    @Scheduled(cron = "0 */1 * * * ?") //每1分钟更新一次
//    @Scheduled(cron = "0 0 12 * * ?") //每天12点执行一次  todo 先不执行，会给服务器搞挂，可能因为两台服务器都执行，数据库被占满
    public void sendAnonymousChatStartTemplateMsg() {
        String lockKey = String.format("sendAnonymousChatStartTemplateMsg");
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("sendAnonymousChatStartTemplateMsg, tryLock:%s failed", lockKey));
            }
//            遍历订阅表，所有订阅活动通知的用户发模板消息

        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("sendAnonymousChatStartTemplateMsg, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_ARTICLE_ERROR);
            throw resolver.decorateException("");
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }


    }

    @Override
    public void setTop(Long articleId, Integer schoolId) {
//        String key = "TOP_ARTICLE_" + schoolId;
//        redisUtil.delete(key);
        articleDOMapper.setTop(articleId);
    }


//    下面是es操作

    @Override
    public void cancelTop(Long articleId, Integer schoolId) {
        articleDOMapper.cancelTop(articleId);
    }




    public PageList<ArticleModel> searchArticle(String order,
                                                String keyword,
                                                CategoryEnum category,
                                                ArticleFunctionTypeEnum functionType,
                                                Integer schoolId,
                                                PageParam param) throws IOException {

        // DB search implementation
        ArticlePageQueryParam queryParam = new ArticlePageQueryParam();
        queryParam.setKeyword(keyword);
        queryParam.setCategory(category != null ? category.getCode() : null);
        queryParam.setFunctionType(functionType != null ? functionType.getCode() : null);
        queryParam.setSchoolId(schoolId);
        queryParam.setPageParam(param);

        long totalCount = articleDOMapper.countByParam(queryParam);
        List<ArticleModel> articleModelList = new ArrayList<>();
        if (totalCount > param.getOffset()) {
            List<ArticleDO> articleDOList = articleDOMapper.pageQuery(queryParam);
            articleModelList = converter.convertArticleList(articleDOList);
            articleModelList.forEach(this::fillArticleModel);
        }

        return new PageList<>(articleModelList, new Paginator(param, totalCount));
    }







    @Override
    public void endSecondHand(Long articleId) {
        redisUtil.delete("article_detail==" + articleId);
    }

    @Override
    public List<WxGroupMsgModel> queryWxGroupArticleInfo(Integer schoolId, Integer seconds) {

        //获取同步最近多少分钟的消息
//        Integer minutes = 20; //默认找20分钟内发布的
        String wxRobotMsgSyncLatestMinutes = bgConfigService.getValue(schoolId, "wx_robot_msg_sync_latest_minutes");
        if (StringUtils.isNotBlank(wxRobotMsgSyncLatestMinutes) && !wxRobotMsgSyncLatestMinutes.equals("0")) {
            seconds = Integer.parseInt(wxRobotMsgSyncLatestMinutes) * 60;
        }

        Date startTime = DateUtil.addSecond(new Date(), -seconds);
//        Date tenMinutesAgo = DateUtil.addSecond(new Date(), -60*60*5); // 5小时内发布的
        List<ArticleDO> articleDOList = articleDOMapper.queryWxGroupArticleInfo(schoolId, startTime);

        List<WxGroupMsgModel> result = Optional.ofNullable(articleDOList).orElse(Collections.emptyList()).stream().map(article -> {
            WxGroupMsgModel wxGroupMsgModel = new WxGroupMsgModel();
            wxGroupMsgModel.setContent(article.getContent());
            wxGroupMsgModel.setCategory(CategoryEnum.getById(article.getCategory()).getDesc());
            wxGroupMsgModel.setType("ARTICLE");

//            这里是跳转到主页，然后主页会根据topicId再跳转到对应详情页
            GenerateShortLinkRequest request = GenerateShortLinkRequest.builder()
                    .pageUrl("pages/topic/index?topicId=" + article.getId())
                    .isPermanent(false)
                    .build();

//            GenerateUrlLinkRequest request = GenerateUrlLinkRequest.builder().path("/pages/topic/index").query("topicId=" + article.getId()).build();
            String link = null;
            try {
                link = wxService.getLinkService().generateShortLink(request);
            } catch (WxErrorException e) {
                log.error("queryWxGroupArticleInfo error", e);
                return null;
            }
            wxGroupMsgModel.setLink(link);
            return wxGroupMsgModel;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return result;
    }

    @Override
    public String queryUrlLink(Long articleId) {

//            这里是跳转到主页，然后主页会根据topicId再跳转到对应详情页
        GenerateUrlLinkRequest request = GenerateUrlLinkRequest.builder()
                .path("pages/topic/index")
                .query("topicId=" + articleId)
                .build();

//            GenerateUrlLinkRequest request = GenerateUrlLinkRequest.builder().path("/pages/topic/index").query("topicId=" + article.getId()).build();
        String link = null;
        try {
            link = wxService.getLinkService().generateUrlLink(request);
        } catch (WxErrorException e) {
            log.error("queryWxGroupArticleInfo error", e);
            return null;
        }

        return link;
    }

    @Override
    public CategoryModel queryCategory(String categoryCode) {
        return converter.convert(categoryDOMapper.selectByPrimaryKey(CategoryEnum.valueOf(categoryCode).getId()));
    }



    @Override
    public void reportSuccess(Long articleId, ReportRecordDO recordDO) {

        String lockKey = String.format("%s==updateArticle", articleId);
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("reportSuccess, tryLock:%s failed", lockKey));
            }
            redisUtil.delete("article_detail==" + articleId);

            ArticleModel detail = this.detail(articleId);
            if (Objects.nonNull(detail) && Boolean.FALSE.equals(detail.getHasDelete())) {
                // TODO: 2023/10/15 失败重试
                this.deleteArticle(articleId);
                if (ArticleFunctionTypeEnum.TASK.equals(detail.getFunctionType())) {

//                    todo 如果是任务，不能直接简单删除，还要退款！ 而且要考虑接单情况
                    taskDOMapper.delOrder(articleId);
                }

//                已经被删除，就不再通知了，多次举报只通知一次
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setNoticeType(NotificationTypeEnum.ARTICLE_VIOLATION.getCode());
                notificationModel.setContent("您的帖子被举报，确认违规，请注意言论，多次违规将会封号");
                notificationModel.setTargetUserId(recordDO.getReportedUserId());
                notificationModel.setCreateTime(new Date());
                notificationService.createNotification(notificationModel);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("reportSuccess, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, UNKNOWN_ERROR);
            throw resolver.decorateException("articleId: {}", articleId);
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }


    @Scheduled(cron = "0 0/20 * * * ?")
    public void publishMockArticle() {
    }


    @Scheduled(cron = "0 0/20 * * * ?") //每20分钟更新一次
    public void autoIncreaseThumbUp() {
        String lockKey = "publishMockArticle";
        //锁的作用，防止重复多次点击
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            //十秒后自动释放，防止锁住永远不释放
            boolean locked = lock.tryLock(20, TimeUnit.SECONDS);
            if (!locked) {
                return;
            }
            List<ArticleModel> articleModels = this.topArticle(null);
            if (CollectionUtils.isNotEmpty(articleModels)) {
                Optional.of(articleModels).orElse(Collections.emptyList()).forEach(articleModel -> {
                    if (articleModel.getViewCount() < 5999) {
                        articleDOMapper.plusViewCount(articleModel.getId(), 0, 299);
                    } else if (articleModel.getViewCount() < 9999) {
                        articleDOMapper.plusViewCount(articleModel.getId(), 0, 199);
                    } else if (articleModel.getViewCount() < 15999) {
                        articleDOMapper.plusViewCount(articleModel.getId(), 0, 99);
                    } else if (articleModel.getViewCount() < 19999) {
                        articleDOMapper.plusViewCount(articleModel.getId(), 0, 39);
                    }
                });
            }

        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("autoIncreaseThumbUp, tryLock:%s failed, error:%s", lockKey, e));
        } finally {
//            https://www2.jianshu.com/p/28770fc01473
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override public void deleteDoc(String id) {}


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ArticleServiceImpl.class);
    @org.springframework.beans.factory.annotation.Autowired
    private com.fmx.xiaomeng.modules.application.repository.dao.TaskDOMapper taskDOMapper;



    public void topTenSchedule() {}
    @Override public void sendHotArticleTemplateMsg() {}

}
