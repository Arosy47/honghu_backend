package com.fmx.xiaomeng.modules.application.controller.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleCommentVO extends CommentVO{

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 如果是父评论，这里是底下的回复
     */
    private List<ArticleCommentVO> replyList;

    /**
     * 返回帖子的图片url（仅个人主页查看评论使用）
     */
    private String originArticleImgUrl;

    /**
     * 返回帖子的内容（仅个人主页查看评论使用）
     */
    private String originArticleContent;

}
