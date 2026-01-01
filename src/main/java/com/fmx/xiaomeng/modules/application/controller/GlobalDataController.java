package com.fmx.xiaomeng.modules.application.controller;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.enums.CategoryEnum;

import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleCategoryDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleCategoryDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.model.WxGroupMsgModel;
import com.fmx.xiaomeng.modules.application.service.model.configmodel.NavItem;
import com.fmx.xiaomeng.modules.application.service.model.configmodel.ShowInfoConfig;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.redis.SysConfigRedis;
import com.fmx.xiaomeng.modules.systemsetting.service.BgConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 客户端通用数据查看模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/app/data")
public class GlobalDataController {

    @Autowired
    private ArticleCategoryDOMapper categoryDOMapper;
    @Autowired
    private BgConfigService bgConfigService;

    @Autowired
    private SysConfigRedis sysConfigRedis;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisUtil redisUtil;



    /**
     * 获取所有话题类别(用户端)
     *
     * @return
     */
    @GetMapping("/getAllCategory")
    public Result<List<ArticleCategoryDO>> getAllCategories() {
        String category_list = redisUtil.get("CATEGORY_LIST");
        List<ArticleCategoryDO> articleCategoryDOS;
        if (StringUtils.isNotBlank(category_list)) {
            articleCategoryDOS = JSON.parseArray(category_list, ArticleCategoryDO.class);
        } else {
            articleCategoryDOS = categoryDOMapper.list();
            redisUtil.set("CATEGORY_LIST", JSON.toJSONString(articleCategoryDOS), 24 * 60 * 10);
        }

        return Result.ok(articleCategoryDOS.stream()
                .filter(item -> !CategoryEnum.SECOND_HAND_MARKET.name().equals(item.getCategoryCode())
                        && !CategoryEnum.NONE.name().equals(item.getCategoryCode()))
                .collect(Collectors.toList()));
    }

    /**
     * 获取所有组局主题
     *
     * @return
     */


    /**
     * 获取轮播图
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/getCarousel")
    public Result<List<Integer>> getCarousel(@UserInfo UserModel userModel) {
        Integer schoolId = userModel.getStrollSchoolId();
        return Result.ok(null);
    }

    /**
     * 是否展示匹配聊天
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/getShowInfoConfig")
    public Result<ShowInfoConfig> getShowInfoConfig(@UserInfo UserModel userModel) {
        if(Objects.isNull(userModel.getStrollSchoolId())){
            return Result.ok(new ShowInfoConfig());
        }
        String originConfig = bgConfigService.getValue(userModel.getStrollSchoolId(), "show_info_config");
        if (StringUtils.isNotBlank(originConfig)) {
            ShowInfoConfig showInfoConfig = JSON.parseObject(originConfig, ShowInfoConfig.class);
            return Result.ok(showInfoConfig);
        } else {
            return Result.ok(null);
        }
    }

    /**
     * 是否展示匹配聊天
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/getNavList")
    public Result<List<NavItem>> getNavList(@UserInfo UserModel userModel) {

        String originConfig = bgConfigService.getValue(userModel.getStrollSchoolId(), "NAV_LIST");
        if (StringUtils.isNotBlank(originConfig)) {
            List<NavItem> navItems = JSON.parseArray(originConfig, NavItem.class);
            return Result.ok(navItems);
        } else {
            String defaultNavItemList = "[]";
            List<NavItem> navItems = JSON.parseArray(defaultNavItemList, NavItem.class);

            return Result.ok(navItems);
        }
    }

    /**
     * 是否展示匹配聊天
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/getWxGroupQrCode")
    public Result<String> getWxGroupQrCode(@UserInfo UserModel userModel) {
        String qrCode = bgConfigService.getValue(userModel.getStrollSchoolId(), "wx_group_qr_code");
        if (StringUtils.isNotBlank(qrCode)) {
            OssFileModel ossFileModel = JSON.parseObject(qrCode, OssFileModel.class);
            return Result.ok(ossFileModel.getUrl());
        } else {
            return Result.ok(null);
        }
    }


    /**
     * 查看最近的帖子
     *
     * @return
     */
    @GetMapping("/queryWxGroupArticleInfo")
    public Result<List<WxGroupMsgModel>> queryWxGroupArticleInfo(@RequestParam(name = "schoolId") Integer schoolId,
                                                                 @RequestParam(name = "seconds") Integer seconds) {
        ArticlePageQueryParam param = new ArticlePageQueryParam();

        param.setSchoolId(schoolId);
        List<WxGroupMsgModel> result = new ArrayList<>();
        List<WxGroupMsgModel> articleInfo = articleService.queryWxGroupArticleInfo(schoolId, seconds);
        result.addAll(articleInfo);
        return Result.ok(result);
    }
}
