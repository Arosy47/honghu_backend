package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.common.enums.CategoryEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleDO;
import com.fmx.xiaomeng.modules.application.repository.model.ReportRecordDO;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.CategoryModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.model.WxGroupMsgModel;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    ArticleModel detail(Long articleId) throws BusinessException;

    void deleteArticle(Long articleId);

    void thumbUpArticle(Long articleId, UserModel userModel, Long targetUserId, Boolean status) throws BusinessException;

    void collectArticle(Long articleId, UserModel userModel, Long targetUserId, Boolean status) throws BusinessException;

    void dingArticle(Long articleId, Long userId) throws BusinessException;

    ArticleModel queryByPrimaryKey(Long articleId);

    PageList<ArticleModel> listArticle(ArticlePageQueryParam articlePageQueryParam);

    PageList<ArticleModel> listArticleWithoutTop(ArticlePageQueryParam articlePageQueryParam);

    void fillArticleModel(ArticleModel model);

    void plusThumbUpCount(Long articleId);

    void minusThumbUpCount(Long articleId);

    void plusViewCount(Long articleId, Integer count);

    void plusCommentCount(Long articleId);

    void minusCommentCount(Long articleId);

    void plusCollectCount(Long articleId);

    void minusCollectCount(Long articleId);

    void createArticle(ArticleModel articleModel) throws BusinessException;

    void update(ArticleModel articleModel);

    Long count(ArticlePageQueryParam param);

    Long countYesterdayArticleNumByDate(Integer schoolId);

    Long countTodayArticleNumByDate(Integer schoolId);

    List<ArticleModel> topArticle(Integer schoolId);

    List<ArticleModel> topTen(String period, Integer schoolId);

    void topTenSchedule();

    void sendHotArticleTemplateMsg();

    @Deprecated
    void setTop(Long articleId, Integer schoolId);

    @Deprecated
    void cancelTop(Long articleId, Integer schoolId);

    String queryUrlLink(Long articleId);

    CategoryModel queryCategory(String categoryCode);




    List<WxGroupMsgModel> queryWxGroupArticleInfo(Integer schoolId, Integer minutes);


    void reportSuccess(Long articleId, ReportRecordDO recordDO);


    void endSecondHand(Long articleId);

    void deleteDoc(String id);
}
