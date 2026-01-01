
package com.fmx.xiaomeng.modules.admin.service.impl;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.admin.service.ArticleAdminService;
import com.fmx.xiaomeng.modules.application.repository.dao.MockArticleDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.MockArticleDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author honghu
 * @date 2025-12-19
 */
@Service("postService")
public class ArticleAdminServiceImpl implements ArticleAdminService {

    @Autowired
    private MockArticleDOMapper mockArticleDOMapper;

    @Autowired
    private Converter converter;

    @Override
    public MockArticleModel detailMock(Long id) {
        MockArticleDO mockArticleDO = mockArticleDOMapper.selectByPrimaryKey(id);
        return converter.convert(mockArticleDO);
    }

    @Override
    public void addMockArticle(MockArticleModel articleModel) {
        mockArticleDOMapper.insertSelective(converter.convert(articleModel));
    }

    @Override
    public void updateMock(MockArticleModel articleModel) {
        mockArticleDOMapper.updateByPrimaryKeySelective(converter.convert(articleModel));
    }

    @Override
    public void deleteMockArticle(Long articleId) {
        mockArticleDOMapper.deleteById(articleId);
    }



    @Override
    public MockArticleModel queryOneBySchool(Integer schoolId) {
        MockArticleDO mockArticleDO = mockArticleDOMapper.queryOneBySchool(schoolId);
        return converter.convert(mockArticleDO);
    }

    @Override
    public PageList<MockArticleModel> listMockArticle(ArticlePageQueryParam param) {
        long total = mockArticleDOMapper.countByParam(param);
        List<MockArticleModel> articleModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<MockArticleDO> articleDOList = mockArticleDOMapper.pageQuery(param);
            articleModelList = converter.convertMockArticleList(articleDOList);

        }

        return new PageList<>(articleModelList, new Paginator(param.getPageParam(), total));
    }

}
