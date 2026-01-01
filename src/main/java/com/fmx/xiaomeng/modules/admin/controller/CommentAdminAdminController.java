package com.fmx.xiaomeng.modules.admin.controller;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.admin.service.CommentAdminService;
import com.fmx.xiaomeng.modules.application.controller.request.CommentRequestDTO;
import com.fmx.xiaomeng.modules.application.controller.response.ArticleCommentVO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.CommentService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.ArticleCommentModel;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CommentPageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import lombok.CustomLog;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @Description 后台评论管理模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/comment")
@CustomLog
public class CommentAdminAdminController extends AbstractAdminController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentAdminService commentAdminService;



    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;

    /**
     * 列表
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("admin:comment:list")
    public Result<PageList<ArticleCommentVO>> list(@RequestParam(name = "pageNum") Integer pageNum,
                                                   @RequestParam(name = "pageSize") Integer pageSize,
                                                   @RequestParam(name = "articleId") Long articleId,
                                                   // @RequestParam(name = "organizationId") Long organizationId,
                                                   @RequestParam(name = "parentCommentId") Long parentCommentId) {
        CommentPageQueryParam param = new CommentPageQueryParam();

        if (-1 != articleId) {
            param.setArticleId(articleId);
        }

        // if (-1 != organizationId) {
        //     param.setArticleId(organizationId);
        // }

        if (-1 != parentCommentId) {
            param.setArticleId(parentCommentId);
        }

        AdminUserModel user = getUser();
        Integer searchSchoolId = null;
        if (!user.getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
            searchSchoolId = user.getSchoolId();
        }

        param.setSchoolId(searchSchoolId);
        param.setPageParam(new PageParam(pageNum));
        param.setOrderFieldName("time");
        param.setOrder("desc");
        PageList<ArticleCommentModel> commentModelPageList = commentService.pageQuery(param, null);


        List<ArticleCommentVO> commentVOList = Optional.ofNullable(commentModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> {
                    return voConverter.convert(model);
                })
                .collect(Collectors.toList());

        return Result.ok(new PageList<>(commentVOList, commentModelPageList.getPaginator()));
    }

    /**
     * 创建评论(虚拟用户给真实用户评论或者回复)，这有点麻烦，但有必要，不能都用匿名的方式，管理后台能控制用哪个用户评论某个帖子或者回复某条评论！
     *
     * @param commentRequestDTO
     * @return
     */
    @SysLog("创建评论")
    @PostMapping("/createComment")
    public Result<Long> createComment(@RequestBody CommentRequestDTO commentRequestDTO) throws WxErrorException {

        ArticleCommentModel articleCommentModel = converter.convert(commentRequestDTO);

        articleCommentModel.setUserId(commentRequestDTO.getUserId());

        ArticleModel articleModel = articleService.queryByPrimaryKey(articleCommentModel.getArticleId());
        UserModel userInfo = userService.getUserInfo(articleCommentModel.getUserId());
        if(Boolean.TRUE.equals(articleModel.getAnonymous())){
            articleCommentModel.setUserNickName(userInfo.getAnonymousName());
            articleCommentModel.setAvatar(userInfo.getAnonymousAvatar().getUrl());
            articleCommentModel.setAnonymous(true);
        }

        articleCommentModel.setSchoolId(articleModel.getSchoolId());
        return Result.ok(commentService.createComment(articleCommentModel, userInfo));
    }

    /**
     * 删除
     */
    @SysLog("删除评论")
    @RequestMapping("/delete")
    @RequiresPermissions("admin:comment:delete")
    public Result<Void> delete(@RequestParam(name = "ids") String ids){
        String[] idList = ids.split(",");
        for (String id : idList) {
            commentService.deleteByCommentId(Long.valueOf(id));
            log.info("管理员删除帖子，ids:{}", ids);
        }
        return Result.ok();
    }



    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommentAdminAdminController.class);
}
