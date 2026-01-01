package com.fmx.xiaomeng.modules.application.service.impl;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.modules.application.repository.dao.CollectDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.CollectDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.CollectService;
import com.fmx.xiaomeng.modules.application.service.NotificationService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.CollectModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CollectPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.CollectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectDOMapper collectMapper;

    @Resource
    private Converter converter;

    @Resource
    @Lazy
    private ArticleService articleService;
    @Resource
    private NotificationService notificationService;

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Resource
    @Lazy
    private UserService userService;

    @Override
    public List<CollectModel> selectByArticleId(Long articleId) {
        CollectParam param = new CollectParam();
        param.setArticleId(articleId);
        List<CollectDO> collects = collectMapper.select(param);
        return converter.convertCollectList(collects);
    }

    @Override
    public CollectModel selectByArticleIdAndUserId(Long articleId, Long userId) {
        CollectParam param = new CollectParam();
        param.setArticleId(articleId);
        param.setUserId(userId);

        List<CollectDO> collects = collectMapper.select(param);
        if (!CollectionUtils.isEmpty(collects)) {
            return converter.convert(collects.get(0));
        }
        return null;
    }

    @Override
    @Transactional
    public void collect(Long articleId, UserModel user, Long targetUserId) {
        CollectDO collectDO = new CollectDO();
        collectDO.setArticleId(articleId);
        collectDO.setUserId(user.getId());
        collectDO.setCreateTime(new Date());
        collectMapper.insert(collectDO);
        articleService.plusCollectCount(articleId);
    }

    @Override
    public void cancel(Long articleId, Long userId) {
        CollectParam collectParam = new CollectParam();
        collectParam.setUserId(userId);
        collectParam.setArticleId(articleId);
        collectMapper.delete(collectParam);
        articleService.minusCollectCount(articleId);
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        CollectParam collectParam = new CollectParam();
        collectParam.setArticleId(articleId);
        collectMapper.delete(collectParam);
    }

    @Override
    public void deleteByUserId(Long userId) {
        CollectParam collectParam = new CollectParam();
        collectParam.setUserId(userId);
        collectMapper.delete(collectParam);
    }

    @Override
    public Long countByUserId(Long userId) {
        return collectMapper.count(userId);
    }

    @Override
    public PageList<ArticleModel> pageQuery(CollectPageQueryParam pageQueryParam) {
        long total = collectMapper.countByParam(pageQueryParam);
        List<ArticleModel> articleModelList = new ArrayList<>();
        PageParam pageParam = pageQueryParam.getPageParam();
        if (total > pageParam.getOffset()) {
            List<CollectDO> collectDOList = collectMapper.pageQuery(pageQueryParam);
            List<CollectModel> collectModelList = converter.convertCollectList(collectDOList);
            Optional.ofNullable(collectModelList).orElse(Collections.emptyList()).forEach(collectModel -> {
                ArticleModel articleModel = articleService.queryByPrimaryKey(collectModel.getArticleId());
                if (Objects.nonNull(articleModel) && Boolean.FALSE.equals(articleModel.getHasDelete())) {

                    articleService.fillArticleModel(articleModel);
                    articleModelList.add(articleModel);
                }
            });
        }
        return new PageList<>(articleModelList, new Paginator(pageQueryParam.getPageParam(), total));
    }


}
