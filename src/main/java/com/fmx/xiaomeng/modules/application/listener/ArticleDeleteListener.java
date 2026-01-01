package com.fmx.xiaomeng.modules.application.listener;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
public class ArticleDeleteListener extends EventBus.EventHandler<Long> {


    @Autowired
    private CollectService collectService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private ArticleService articleService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_DELETE;
    }

    @Override
    public void onMessage(Long articleId) {

        collectService.deleteByArticleId(articleId);
        ArticleDO articleDO = articleDOMapper.selectByPrimaryKey(articleId);
        Integer schoolId = articleDO.getSchoolId();

//        判断是否十大热帖之一
        String top_ten_day_article_id_list = redisUtil.get("TOP_TEN_DAY_ARTICLE_ID_LIST" + schoolId);
        if (!StringUtils.isEmpty(top_ten_day_article_id_list)) {
            List<Long> idList = JSON.parseArray(top_ten_day_article_id_list, Long.class);
            if (idList.contains(articleId)) {
                redisUtil.delete("TOP_TEN_DAY" + schoolId);
                redisUtil.delete("TOP_TEN_DAY_ARTICLE_ID_LIST" + schoolId);
                articleService.topTenSchedule();
            }
        }


        articleService.deleteDoc(String.valueOf(articleId));
    }

}

