package com.fmx.xiaomeng.modules.application.controller;

import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.enums.ReportTypeEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.service.WxInterfaceOperateService;
import com.fmx.xiaomeng.common.utils.EnvUtil;
import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.controller.request.CommentRequestDTO;
import com.fmx.xiaomeng.modules.application.controller.response.ArticleCommentVO;
import com.fmx.xiaomeng.modules.application.repository.dao.ReportRecordDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ReportRecordDO;
import com.fmx.xiaomeng.modules.application.service.CommentService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.ArticleCommentModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.param.CommentPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.ReportQueryParam;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.CONTENT_VIOLATION;
import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.NO_AUTH;

/**
 * @Description 客户端评论模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/app/comment")
public class ArticleCommentController {

    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;
    @Resource
    private CommentService commentService;

    @Resource
    private ReportRecordDOMapper reportRecordDOMapper;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private WxInterfaceOperateService wxInterfaceOperateService;

    @Autowired
    private UserService userService;


    /**
     * 创建评论
     *
     * @param commentRequestDTO
     * @return
     */
    @NeedLogin
    @RequestMapping("/createComment")
    public Result<Long> createComment(@RequestBody CommentRequestDTO commentRequestDTO, @UserInfo UserModel userModel) throws WxErrorException {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }

        if(userService.isBlacked(commentRequestDTO.getArticleUserId(), userModel.getUserId())) {
            throw new BusinessException(ErrorCodeEnum.USER_BLACKED, "您已被对方拉黑");
        }

        ArticleCommentModel articleCommentModel = converter.convert(commentRequestDTO);

        if (envUtil.isOnline()) {
            if (StringUtils.isNotBlank(commentRequestDTO.getContent())) {
                if (!wxInterfaceOperateService.
                        checkMessage(articleCommentModel.getContent(), userModel.getOpenId())) {
                    throw new BusinessException(CONTENT_VIOLATION, "评论内容违规, userId:{}, content:{}", userModel.getUserId(), commentRequestDTO.getContent());
                }
            }
        }

        articleCommentModel.setUserId(userModel.getUserId());



        return Result.ok(commentService.createComment(articleCommentModel, userModel));
    }


    /**
     * 分页查询所有评论
     *
     * @param articleId 文章id
     * @param pageNum   页码
     * @return
     */
    @GetMapping("/listArticleComment")
    public Result<PageList<ArticleCommentVO>> listArticleComment(@RequestParam(name = "articleId") Long articleId,
                                                                 @RequestParam(name = "pageNum") Integer pageNum,
                                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                                 @RequestParam(name = "userId") Long userId,
                                                                 @RequestParam(name = "order") String order
    ) {
        CommentPageQueryParam param = new CommentPageQueryParam();
        param.setPageParam(new PageParam(pageNum, pageSize));
        param.setArticleId(articleId);
        param.setOrderFieldName(order);
        param.setOrder("desc");
        param.setParentCommentId(0L);

        PageList<ArticleCommentModel> commentModelPageList = commentService.pageQuery(param, userId);

        List<ArticleCommentVO> articleCommentVOList = Optional.ofNullable(commentModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream()
                .map(commentModel -> {
                    // TODO: 2023/10/8    查询评论的回复，放在一个接口里的好处是不用多次查询，
                    //  坏处是rt长，可以在点击“展开”的时候再单独查询回复
                    CommentPageQueryParam replyQueryParam = new CommentPageQueryParam();
                    replyQueryParam.setArticleId(param.getArticleId());
                    replyQueryParam.setParentCommentId(commentModel.getId());
                    replyQueryParam.setPageParam(new PageParam(1, 20));
                    replyQueryParam.setOrder("asc");
                    replyQueryParam.setOrderFieldName("time");
                    PageList<ArticleCommentModel> commentReplyModelList = commentService.pageQuery(replyQueryParam, userId);
                    commentModel.setReplyList(commentReplyModelList.getDataList());
                    return voConverter.convert(commentModel);
                }).collect(Collectors.toList());
        return Result.ok(new PageList<>(articleCommentVOList, commentModelPageList.getPaginator()));
    }

    /**
     * 分页查询所有回复 （回复评论和回复回复都一样，是一级，都有commentId，区别是前端分层展示时，回复评论不用写“回复”，回复回复的话要标注“回复某某”）
     *
     * @param articleId       帖子id  加上这个入参是为了数据库索引方便，查询速度更快
     * @param parentCommentId 评论id
     * @param pageNum         页码
     * @return
     */
    @NeedLogin
    @GetMapping("/listReply")
    @Deprecated
    public Result<PageList<ArticleCommentVO>> listReply(
            @RequestParam(name = "articleId") Long articleId,
            @RequestParam(name = "parentCommentId") Long parentCommentId,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize,
            @UserInfo UserModel userModel) {
        CommentPageQueryParam param = new CommentPageQueryParam();
        param.setPageParam(new PageParam(pageNum, pageSize));
        param.setArticleId(articleId);
        param.setParentCommentId(parentCommentId);
        param.setOrder("asc");
        param.setOrderFieldName("time");
        PageList<ArticleCommentModel> commentModelPageList = commentService.pageQuery(param, userModel.getUserId());

        List<ArticleCommentVO> articleCommentVOList = Optional.ofNullable(commentModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream()
                .map(voConverter::convert).collect(Collectors.toList());
        return Result.ok(new PageList<>(articleCommentVOList, commentModelPageList.getPaginator()));
    }
//    }


    // TODO: 2023/2/12 删除可以把标志位设置为-1，页面上显示该条评论已经删除，所有的模型中都加一个是否有效字段 ，
    //  把所有删除动作改为update，前期数据库资源有限，可以删除

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @NeedLogin
    @GetMapping("/deleteComment")
//    @ApiOperation(value = "删除评论")
    public Result<Void> deleteComment(@RequestParam(name = "commentId") Long commentId,
                                      @UserInfo UserModel userModel) {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        Long userId = userModel.getUserId();
        if (userId < 0) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        ArticleCommentModel articleCommentModel = commentService.queryByPrimaryKey(commentId);
        if (Objects.isNull(articleCommentModel)) {
            return Result.ok();
        }
        if (userModel.getAdmin() || articleCommentModel.getId().equals(commentId)) {
            commentService.deleteComment(commentId, userId);
        } else {
            throw new BusinessException(NO_AUTH, "无权限");
        }


        // TODO: 2023/3/31 积分 - 5 弹窗 ，前提是删除了一天内的评论
        return Result.ok();
    }


    /**
     * 点赞评论
     *
     * @param commentId
     * @param targetUserId 被点赞用户id
     * @return
     */
    @NeedLogin
    @GetMapping("/thumbUpComment")
//    @ApiOperation(value = "点赞评论")
    public Result<Void> thumbUpComment(
            @RequestParam(name = "articleId") Long articleId,
            @RequestParam(name = "commentId") Long commentId,
            @RequestParam(name = "targetUserId") Long targetUserId,
            @RequestParam(name = "thumbUpStatus") Boolean thumbUpStatus,
            @UserInfo UserModel userModel) throws BusinessException {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        Long userId = userModel.getUserId();
        if (userId < 0) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        // TODO: 2023/2/18  commentId就能唯一确定一条评论了，targetUserId可以不传，但是传过来可以少一次查询（通过commentId查询评论再找到该评论对应的userId）
        commentService.thumbUpComment(articleId, commentId, userModel, targetUserId, thumbUpStatus);
        return Result.ok();
    }


    /**
     * 举报
     *
     * @param articleCommentId
     * @return
     */
    @NeedLogin
    @GetMapping("/complaint")
    public Result complaint(
            @RequestParam(name = "articleCommentId") Long articleCommentId,
            @RequestParam(name = "reason") String reason,
            @RequestParam(name = "reportedUserId") Long reportedUserId,
            @UserInfo UserModel userModel) throws IOException, WxErrorException {

        // TODO: 2023/8/30 给我自己发微信模板消息、也发小程序内的消息（留存记录），然后操作把该文章设置为has_delete
        
        //        保存举报记录, 给我发送模板消息，
        //
        //        我收到后判断，如果违规然后对原帖做删除动作，同时给举报人和被举报人发消息通知
        //        如果不违规，则给举报人发消息，告知不违规（应该不需要发消息了）

        //        举报帖子id，举报时间，理由（枚举），举报人id，被举报人id，

        //        report_id, createTime,reason,user_id, reported_user_id

        ReportQueryParam param = new ReportQueryParam();
        param.setUserId(userModel.getUserId());
        param.setReportType(ReportTypeEnum.ARTICLE_COMMENT.getCode());
        param.setReportTypeId(articleCommentId);
        ReportRecordDO exist = reportRecordDOMapper.queryByParam(param);
        if (Objects.nonNull(exist)) {
            return Result.ok();
        }

        ReportRecordDO reportRecordDO = new ReportRecordDO();
        reportRecordDO.setReportType(ReportTypeEnum.ARTICLE_COMMENT.getCode());
        reportRecordDO.setReportTypeId(articleCommentId);
        reportRecordDO.setReason(reason);
        reportRecordDO.setUserId(userModel.getUserId());
        reportRecordDO.setReportedUserId(reportedUserId);
        reportRecordDO.setCreateTime(new Date());
        reportRecordDO.setSchoolId(userModel.getStrollSchoolId());
        reportRecordDOMapper.insertSelective(reportRecordDO);

//        ReportEventModel reportEventModel = new ReportEventModel();
//        reportEventModel.setReportType(ReportTypeEnum.ARTICLE_COMMENT);
//        reportEventModel.setReportId(articleCommentId);
        EventBus.emit(EventBus.Topic.REPORT, reportRecordDO);


        return Result.ok();



    }



}
