package com.fmx.xiaomeng.modules.admin.service.impl;

import com.fmx.xiaomeng.modules.admin.service.CommentAdminService;
import com.fmx.xiaomeng.modules.application.service.CommentService;
import com.fmx.xiaomeng.modules.application.service.model.ArticleCommentModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author honghu
 * @date 2025-12-19
 */
@Service("commentAdminService")
public class CommentAdminServiceImpl implements CommentAdminService {

    @Autowired
    private CommentService commentService;


    /**
     * 管理端批量删除评论
     *
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleCommentByAdmin(List<Long> list) {
        list.forEach(id -> {
            ArticleCommentModel articleCommentModel = commentService.queryByPrimaryKey(id);
            commentService.deleteComment(id, articleCommentModel.getUserId());
        });
    }



}
