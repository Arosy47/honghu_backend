package com.fmx.xiaomeng.modules.application.controller;

import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.controller.response.ArticleVO;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CollectPageQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description 收藏模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/app/collect")
public class CollectController {
    @Autowired
    private com.fmx.xiaomeng.modules.application.service.CollectService collectService;
    @Autowired
    private VOConverter voConverter;

    /**
     * 查看某人收藏的文章，暂时不开放查看其他人的收藏？
     *
     * @param userId
     * @param pageNum
     * @param userModel
     * @return
     */
    @NeedLogin
    @GetMapping("/listCollectArticle")
    public Result<PageList<ArticleVO>> listCollectArticle(@RequestParam(name = "userId") Long userId,
                                                          @RequestParam(name = "pageNum") Integer pageNum,
                                                          @RequestParam(name = "pageSize") Integer pageSize,
                                                          @UserInfo UserModel userModel) {
        CollectPageQueryParam pageQueryParam = new CollectPageQueryParam();
        pageQueryParam.setUserId(userId);
        pageQueryParam.setPageParam(new PageParam(pageNum, pageSize));
        PageList<ArticleModel> articleModelPageList = collectService.pageQuery(pageQueryParam);
        List<ArticleVO> articleVOList = Optional.ofNullable(articleModelPageList.getDataList()).orElse(Collections.emptyList())
                .stream().map(voConverter::convert).collect(Collectors.toList());

        return Result.ok(new PageList<>(articleVOList, articleModelPageList.getPaginator()));

    }
}
