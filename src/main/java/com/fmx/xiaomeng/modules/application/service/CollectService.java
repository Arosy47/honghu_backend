package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.CollectModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CollectPageQueryParam;

import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public interface CollectService {
    // 查询话题被多少人收藏过
    List<CollectModel> selectByArticleId(Long articleId);

    // 查询用户是否收藏过某个话题
    CollectModel selectByArticleIdAndUserId(Long articleId, Long userId);

    // 收藏话题
    @Deprecated
    void collect(Long articleId, UserModel user, Long targetUserId);

    // 删除（取消）收藏
    void cancel(Long articleId, Long userId);

    // 根据话题id删除收藏记录
    void deleteByArticleId(Long articleId);

    // 根据用户id删除收藏记录
    void deleteByUserId(Long userId);

    // 查询用户收藏的话题数
    Long countByUserId(Long userId);

    // 查询用户收藏的话题
    PageList<ArticleModel> pageQuery(CollectPageQueryParam pageQueryParam);
}
