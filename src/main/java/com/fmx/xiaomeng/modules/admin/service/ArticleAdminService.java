package com.fmx.xiaomeng.modules.admin.service;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;

/**
 *
 *
 * @author honghu
 * @date 2025-12-20
 */
public interface ArticleAdminService {

    MockArticleModel detailMock(Long id);

    void addMockArticle(MockArticleModel articleModel);

    void updateMock(MockArticleModel articleModel);

    void deleteMockArticle(Long articleId);

    MockArticleModel queryOneBySchool(Integer schoolId);

    PageList<MockArticleModel> listMockArticle(ArticlePageQueryParam param);
}

