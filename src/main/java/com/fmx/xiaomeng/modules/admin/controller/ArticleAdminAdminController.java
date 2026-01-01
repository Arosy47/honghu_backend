/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
package com.fmx.xiaomeng.modules.admin.controller;


import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.common.enums.CategoryEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.exception.ExceptionResolver;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.common.utils.RandomNameUtil;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.admin.request.ArticleAdminRequestDTO;
import com.fmx.xiaomeng.modules.admin.request.MockArticleAdminRequestDTO;
import com.fmx.xiaomeng.modules.admin.service.ArticleAdminService;
import com.fmx.xiaomeng.modules.application.controller.response.ArticleVO;
import com.fmx.xiaomeng.modules.application.controller.response.MockArticleVO;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleCategoryDOMapper;
// import com.fmx.xiaomeng.modules.application.repository.dao.TaskDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleCategoryDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.ArticlePageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fmx.xiaomeng.common.constant.GlobalConstants.initAnonymousAvatarList;
import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.CREATE_ARTICLE_ERROR;

@CustomLog
@RestController
@RequestMapping("admin/article")
public class ArticleAdminAdminController extends AbstractAdminController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleAdminService articleAdminService;

    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArticleCategoryDOMapper categoryDOMapper;

    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @GetMapping("/search")
    @RequiresPermissions("admin:post:list")
    public Result<PageList<ArticleVO>> search(@RequestParam(name = "keyword", required = false) String keyword,
                                              @RequestParam(name = "categoryCode", required = false) String categoryCode,
                                              @RequestParam(name = "functionType", required = false) String functionType,
                                              @RequestParam(name = "schoolId") Integer schoolId,
                                              @RequestParam(name = "order", required = false) String order,
                                              @RequestParam(name = "pageNum") Integer pageNum,
                                              @RequestParam(name = "pageSize") Integer pageSize) {
        CategoryEnum category = null;
        ArticleFunctionTypeEnum functionTypeEnum = null;

        if (StringUtils.isNotBlank(categoryCode)) {
            category = CategoryEnum.valueOf(categoryCode);
        }
        if (StringUtils.isNotBlank(functionType)) {
            functionTypeEnum = ArticleFunctionTypeEnum.valueOf(functionType);
        }
        AdminUserModel user = getUser();
        Integer searchSchoolId = schoolId;
        if (!user.getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
            searchSchoolId = user.getSchoolId();
        }

        ArticlePageQueryParam param = new ArticlePageQueryParam();
        param.setSchoolId(searchSchoolId);
        param.setPageParam(new PageParam(pageNum, pageSize));
        if (category != null) {
            param.setCategory(category.getId());
        }
        if (functionTypeEnum != null) {
            param.setFunctionType(functionTypeEnum.getCode());
        }
        if (StringUtils.isNotBlank(keyword)) {
            param.setKeyword(keyword);
        }
        param.setOrder("time");
        PageList<ArticleModel> articleModelPageList = articleService.listArticle(param);

        List<ArticleVO> articleVOList = Optional.ofNullable(articleModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> voConverter.sensitiveConvert(model))
                .collect(Collectors.toList());
        Paginator paginator = articleModelPageList.getPaginator();

        return Result.ok(new PageList<>(articleVOList, paginator));
    }

    @SysLog("设置置顶")
    @GetMapping("/setTop")
    @RequiresPermissions("sys:article:setTop")
    @Deprecated
    public Result<Void> setTop(@RequestParam(name = "articleId") Long articleId,
                               @RequestParam(name = "schoolId") Integer schoolId) {
        Long userId = getUser().getUserId();
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("无权限");
        }

        articleService.setTop(articleId, schoolId);

        return Result.ok();
    }

    @SysLog("取消置顶")
    @GetMapping("/cancelTop")
    @RequiresPermissions("sys:article:cancelTop")
    @Deprecated
    public Result<Void> cancelTop(@RequestParam(name = "articleId") Long articleId,
                                  @RequestParam(name = "schoolId") Integer schoolId) {
        Long userId = getUser().getUserId();
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("无权限");
        }

        articleService.cancelTop(articleId, schoolId);

        return Result.ok();
    }

    @GetMapping("/getAllCategory")
    public Result<List<ArticleCategoryDO>> getAllCategories() {
        return Result.ok(categoryDOMapper.list());
    }

    @GetMapping("/detail")
    @RequiresPermissions("admin:post:info")
    public Result<ArticleVO> detail(@RequestParam("id") Long id) {
        ArticleModel articleModel = articleService.queryByPrimaryKey(id);
        return Result.ok(voConverter.sensitiveConvert(articleModel));
    }

    @SysLog("更新帖子")
    @Deprecated
    @PostMapping("/update")
    @RequiresPermissions("admin:post:update")
    public Result<Void> update(@RequestBody ArticleAdminRequestDTO articleRequestDTO) {
        ArticleModel articleModel = converter.convert(articleRequestDTO);
        articleService.update(articleModel);
        return Result.ok();
    }

    @SysLog("创建mock帖")
    @Deprecated
    @PostMapping("/createMock")
    @RequiresPermissions("admin:post:createMock")
    public Result<Void> mockArticle(@RequestBody ArticleAdminRequestDTO articleRequestDTO) {
        String lockKey = String.format("%s==createArticle", articleRequestDTO.getUserId());
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createArticle, tryLock:%s failed", lockKey));
            }
            ArticleModel articleModel = converter.convert(articleRequestDTO);
            articleModel.setFunctionType(ArticleFunctionTypeEnum.NORMAL);
            articleModel.setCommentCount(null);

            String key = "TOP_ARTICLE_" + articleRequestDTO.getSchoolId();
            redisUtil.delete(key);

            UserModel userInfo = userService.getUserInfo(articleModel.getUserId());
            articleModel.setUserNickName(userInfo.getNickName());
            articleModel.setAvatar(userInfo.getAvatar().getUrl());

            articleModel.setAnonymousName(RandomNameUtil.generateNickname());
            if (Boolean.TRUE.equals(articleRequestDTO.getAnonymous())) {
                Random random2 = new Random();
                int randomIndex2 = random2.nextInt(initAnonymousAvatarList.length);
                articleModel.setAvatar(initAnonymousAvatarList[randomIndex2]);
            }

            articleModel.setSchoolId(articleRequestDTO.getSchoolId());
            articleService.createArticle(articleModel);

            return Result.ok();
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("createArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_ARTICLE_ERROR);
            throw resolver.decorateException("userId: {}", articleRequestDTO.getUserId());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @SysLog("删除帖子")
    @GetMapping("/delete")
    @RequiresPermissions("admin:post:delete")
    public Result<Void> delete(@RequestParam(name = "ids") String ids) throws IOException {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Long articleId = Long.valueOf(id);
            ArticleModel detail = articleService.detail(articleId);
            if (detail != null) {
                articleService.deleteArticle(articleId);

//                if (ArticleFunctionTypeEnum.TASK.equals(detail.getFunctionType())) {
//                    taskDOMapper.delOrder(articleId);
//                }
            }
            log.info("管理员删除帖子，ids:{}", ids);
        }
        return Result.ok();
    }

    @GetMapping("/detailMock")
    @RequiresPermissions("admin:post:info")
    public Result<MockArticleVO> detailMock(@RequestParam("id") Long id) {
        MockArticleModel articleModel = articleAdminService.detailMock(id);
        return Result.ok(voConverter.convert(articleModel));
    }

    @SysLog("更新帖子")
    @Deprecated
    @PostMapping("/updateMock")
    @RequiresPermissions("admin:post:update")
    public Result<Void> updateMock(@RequestBody MockArticleAdminRequestDTO articleRequestDTO) {
        MockArticleModel articleModel = converter.convert(articleRequestDTO);
        articleAdminService.updateMock(articleModel);
        return Result.ok();
    }

    @SysLog("创建mock帖")
    @PostMapping("/addMockArticle")
    @RequiresPermissions("admin:post:createMock")
    public Result<Void> addMockArticle(@RequestBody MockArticleAdminRequestDTO articleRequestDTO) {
        String lockKey = String.format("%s==addMockArticle", articleRequestDTO.getUserId());
        RLock lock = redissonLockUtil.lock(lockKey);
        try {
            boolean locked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(
                        ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("addMockArticle, tryLock:%s failed", lockKey));
            }

            AdminUserModel user = getUser();
            MockArticleModel articleModel = converter.convert(articleRequestDTO);
            articleModel.setSchoolId(articleRequestDTO.getSchoolId());
            articleModel.setCreateTime(new Date());
            articleModel.setAdminUserId(user.getUserId());
            articleAdminService.addMockArticle(articleModel);

            return Result.ok();
        } catch (InterruptedException e) {
            throw new BusinessException(
                    ErrorCodeEnum.DISTRIBUTE_LOCK_FAILED, String.format("addMockArticle, tryLock:%s failed", lockKey));
        } catch (Exception e) {
            ExceptionResolver resolver = ExceptionResolver.resolve(e, CREATE_ARTICLE_ERROR);
            throw resolver.decorateException("userId: {}", articleRequestDTO.getUserId());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @SysLog("删除帖子")
    @GetMapping("/deleteMock")
    @RequiresPermissions("admin:post:delete")
    public Result<Void> deleteMock(@RequestParam(name = "ids") String ids) throws IOException {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Long articleId = Long.valueOf(id);
            MockArticleModel detail = articleAdminService.detailMock(articleId);
            if (detail != null) {
                articleAdminService.deleteMockArticle(articleId);
            }
            log.info("管理员删除帖子，ids:{}", ids);
        }
        return Result.ok();
    }

    @GetMapping("/searchMock")
    @RequiresPermissions("admin:post:list")
    public Result<PageList<MockArticleVO>> searchMock(
                                              @RequestParam(name = "categoryCode") String categoryCode,
                                              @RequestParam(name = "schoolId") Integer schoolId,
                                              @RequestParam(name = "order") String order,
                                              @RequestParam(name = "pageNum") Integer pageNum,
                                              @RequestParam(name = "pageSize") Integer pageSize) throws IOException {
        CategoryEnum category = null;

        if (StringUtils.isNotBlank(categoryCode)) {
            category = CategoryEnum.valueOf(categoryCode);
        }

        AdminUserModel user = getUser();
        Integer searchSchoolId = schoolId;
        if (!user.getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
            searchSchoolId = user.getSchoolId();
        }

        List<MockArticleVO> articleVOList;

        ArticlePageQueryParam param = new ArticlePageQueryParam();
        param.setSchoolId(searchSchoolId);

        param.setPageParam(new PageParam(pageNum));
        if (category != null) {
            param.setCategory(category.getId());
        }
        param.setOrder("time");
        PageList<MockArticleModel> articleModelPageList = articleAdminService.listMockArticle(param);

        articleVOList = Optional.ofNullable(articleModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> voConverter.convert(model))
                .collect(Collectors.toList());
        Paginator paginator = articleModelPageList.getPaginator();
        return Result.ok(new PageList<>(articleVOList, paginator));
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ArticleAdminAdminController.class);
}
