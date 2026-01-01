package com.fmx.xiaomeng.modules.admin.request;

import com.fmx.xiaomeng.modules.application.controller.request.ArticleRequestDTO;
import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class ArticleAdminRequestDTO extends ArticleRequestDTO {
    private Long userId;

    private Integer schoolId;

    /**
     *    点赞数
     */
    private Integer thumbUpCount;
    /**
     *      浏览数
     */
    private Integer viewCount;

    /**
     *     收藏数
     */
    private Integer collectCount;

    /**
     * 评论数（用于计算热度，前端不会修改）
     */
    private Integer commentCount;


    /**
     *     热度
     */
    private Long hot;



    /**
     * 置顶
     */
    private Boolean top;



}
