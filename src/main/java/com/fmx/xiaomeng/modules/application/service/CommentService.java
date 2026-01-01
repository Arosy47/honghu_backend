package com.fmx.xiaomeng.modules.application.service;

import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.application.repository.model.ReportRecordDO;
import com.fmx.xiaomeng.modules.application.service.model.ArticleCommentModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CommentPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.CommentParam;

import java.util.List;


public interface CommentService {

    Long createComment(ArticleCommentModel articleCommentModel, UserModel userModel) throws BusinessException;

    /**
     * 可以根据articleId，或者articleId+commentId
     */

    PageList<ArticleCommentModel> pageQuery(CommentPageQueryParam commentPageQueryParam, Long userId);



    ArticleCommentModel queryByPrimaryKey(Long id);

    @Deprecated
    List<ArticleCommentModel> queryArticleCommentByParam(Long userId, CommentParam param);

    Long insert(ArticleCommentModel model) throws BusinessException;

    @Deprecated
    void deleteByArticleId(Long articleId);


    void deleteByCommentId(Long commentId);


    void deleteComment(Long commentId, Long userId);

    /**
     * 点赞+1
     * @param articleId
     */
    void plusThumbUpCount(Long articleId);

    /**
     * 点赞-1
     * @param articleId
     */
    void minusThumbUpCount(Long articleId);


    void thumbUpComment(Long articleId, Long commentId, UserModel userModel, Long targetUserId, Boolean thumbUpStatus) throws BusinessException;


    Long countByUserId(Long userId);

    void reportSuccess(Long commentId, ReportRecordDO recordDO);
}
