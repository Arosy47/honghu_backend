package com.fmx.xiaomeng.modules.application.listener;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import com.fmx.xiaomeng.common.enums.CommentNotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.CommentTypeEnum;
import com.fmx.xiaomeng.common.enums.WxMsgTemplateTypeEnum;
import com.fmx.xiaomeng.common.service.WxInterfaceOperateService;
import com.fmx.xiaomeng.common.utils.EnvUtil;
import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.common.utils.SubscribeMessageUtil;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleCommentDOMapper;
import com.fmx.xiaomeng.modules.application.repository.dao.MediaCheckRecordDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ArticleCommentDO;
import com.fmx.xiaomeng.modules.application.repository.model.MediaCheckRecordDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.NotificationService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.WxMsgSubscribeService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.ArticleCommentModel;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel;
import com.fmx.xiaomeng.modules.application.websocket.service.NewMsgSendService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Component
@Slf4j
public class ArticleCommentCreateListener extends EventBus.EventHandler<ArticleCommentModel> {

    @Resource
    private NotificationService notificationService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleCommentDOMapper articleCommentDOMapper;

    @Resource
    private Converter converter;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private MediaCheckRecordDOMapper mediaCheckRecordDOMapper;


    @Autowired
    private WxInterfaceOperateService wxInterfaceOperateService;
    @Autowired
    private SubscribeMessageUtil subscribeMessageUtil;

    @Autowired
    private NewMsgSendService newMsgSendService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private WxMsgSubscribeService wxMsgSubscribeService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_COMMENT_CREATE;
    }

    @Override
    public void onMessage(ArticleCommentModel articleCommentModel) throws IOException {

        if (envUtil.isOnline() && Objects.nonNull(articleCommentModel.getImgUrl())) {
            try {

                UserModel user = userService.getUserInfo(articleCommentModel.getUserId());
                if (StringUtils.isNotBlank(user.getOpenId())) { //兼容新版本小程序切换openId为空的情况
                    String traceId = wxInterfaceOperateService.
                            checkImage(articleCommentModel.getImgUrl().getUrl(), user.getOpenId());

                    MediaCheckRecordDO mediaCheckRecordDO = new MediaCheckRecordDO();
                    mediaCheckRecordDO.setTraceId(traceId);
                    mediaCheckRecordDO.setUserId(articleCommentModel.getUserId());
                    mediaCheckRecordDO.setMediaType(0); //0图片,1音频
                    mediaCheckRecordDO.setBelongType(2); //0帖子、1组局帖、 2评论帖子、3评论组局、4头像 5私信消息、6匿名聊天消息 等
                    mediaCheckRecordDO.setBelongId(articleCommentModel.getId());
                    mediaCheckRecordDOMapper.insertSelective(mediaCheckRecordDO);
                }
            } catch (WxErrorException e) {
                log.error("敏感图片校验接口异常！！！");
            }
        }


        if (articleCommentModel.getUserId().equals(articleCommentModel.getToUserId())) {
//            评论的人和被评论的人不是一个人则返回
            return;
        }

        notificationService.commentNotice(articleCommentModel, CommentNotificationTypeEnum.COMMENT_ARTICLE);

        Long toUserId = articleCommentModel.getToUserId();

        if (newMsgSendService.isOnline(toUserId)) {
            Map<String, String> msg = new HashMap<>();
            msg.put("type", "NEW_COMMENT");
            //不用管数量，有新的消息就发，前端自己加1，查看后（或下拉刷新）清零
            msg.put("num", "1");
            newMsgSendService.sendTextMessage(toUserId, msg);
        } else {
//            String content = articleCommentModel.getContent();

            UserModel userInfo = userService.getUserInfo(toUserId);

            Long articleId = articleCommentModel.getArticleId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            String time = format.format(new Date());
//            String page = "/pages/topic/index?topicId=" + articleId + "&shareUserId=7777777"; //这里的shareUserId只是为了兼容分享的情况，通过消息通知点开后也是先到主页再到明细
            String page = "/pages/topic/index?topicId=" + articleId; ////不需要shareUserId，有topicId就会先到主页再到明细


            String content = articleCommentModel.getContent();
            CommentTypeEnum commentType = articleCommentModel.getCommentType();
            if(CommentTypeEnum.PICTURE.equals(commentType) && StringUtils.isBlank(content)){
                content="[图片]";
            }else {
                content=content.length() <= 15 ? content : StringUtils.substring(content, 0, 13) + "..";
            }

            if (articleCommentModel.getParentCommentId() == 0L) {
                ArticleModel articleModel = articleService.queryByPrimaryKey(articleId);

                if (Objects.isNull(articleModel) || Boolean.TRUE.equals(articleModel.getHasDelete())) {
//                    帖子都删了就不发了
                    return;
                }


                WxMsgSubscribeRecordModel record = wxMsgSubscribeService.query(userInfo.getUserId(), WxMsgTemplateTypeEnum.NEW_COMMENT);
                if (Objects.nonNull(record) && record.getSubscribe() && record.getLeftTimes() > 0) {
                    // 说明是评论
//                    ArticleModel articleModel = articleService.queryByPrimaryKey(articleId);
                    this.subscribeMessageUtil.sendSubscribeMessage(userInfo.getUserId(), WxMaSubscribeMessage.builder()
                            .templateId(record.getTemplateId())
                            .lang(WxMaConstants.MiniProgramLang.ZH_CN)
                            .miniprogramState(WxMaConstants.MiniProgramState.FORMAL)
                            .data(Lists.newArrayList(
                                    new WxMaSubscribeMessage.MsgData("thing8", articleModel.getContent().length() <= 15 ?
                                            articleModel.getContent() :
                                            StringUtils.substring(articleModel.getContent(), 0, 13) + ".."),
                                    new WxMaSubscribeMessage.MsgData("thing2", content),
                                    new WxMaSubscribeMessage.MsgData("thing12", articleCommentModel.getUserNickName()),
                                    new WxMaSubscribeMessage.MsgData("date3", time)
                                    )
                            )
                            .toUser(userInfo.getOpenId())
                            .page(page)
                            .build());
                }
            } else {
                WxMsgSubscribeRecordModel record = wxMsgSubscribeService.query(userInfo.getUserId(), WxMsgTemplateTypeEnum.NEW_COMMENT);
                if (Objects.nonNull(record) && record.getSubscribe() && record.getLeftTimes() > 0) {
                    //                说明是回复
                    ArticleCommentDO beComment = articleCommentDOMapper.selectByPrimaryKey(articleCommentModel.getReplyCommentId());

                    String beCommentContent = "";
                    if (Objects.isNull(beComment) || Boolean.TRUE.equals(beComment.getHasDelete())) {
                        beCommentContent = "原评论已删除";
                    } else {
                        beCommentContent = beComment.getContent();
                        if(CommentTypeEnum.PICTURE.equals(beComment.getCommentType()) && StringUtils.isBlank(beCommentContent)){
                            beCommentContent="[图片]";
                        }else {
                            beCommentContent = beCommentContent.length() <= 15 ?
                                    beCommentContent : StringUtils.substring(beCommentContent, 0, 13) + "..";
                        }
                    }
                    this.subscribeMessageUtil.sendSubscribeMessage(userInfo.getUserId(), WxMaSubscribeMessage.builder()
                            .templateId(record.getTemplateId())
                            .lang(WxMaConstants.MiniProgramLang.ZH_CN)
                            .miniprogramState(WxMaConstants.MiniProgramState.FORMAL)
                            .data(
                                    Lists.newArrayList(
//                                    new WxMaSubscribeMessage.MsgData("thing1","原评论，这个删掉"),
                                            new WxMaSubscribeMessage.MsgData("thing8", beCommentContent),
                                            new WxMaSubscribeMessage.MsgData("thing2", content),
                                            new WxMaSubscribeMessage.MsgData("thing12", articleCommentModel.getUserNickName()),
                                            new WxMaSubscribeMessage.MsgData("date3", time)
                                    )
                            )
//                                    new WxMaSubscribeMessage.MsgData("time4",time)))
                            .toUser(userInfo.getOpenId())
                            .page(page)
                            .build());
                }
            }

        }

    }



    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ArticleCommentCreateListener.class);
}
