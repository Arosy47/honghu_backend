package com.fmx.xiaomeng.common.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMsgEvent;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.WxMsgTemplateTypeEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.exception.ExceptionResolver;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.common.utils.redis.RedissonLockUtil;
import com.fmx.xiaomeng.modules.application.repository.dao.*;
import com.fmx.xiaomeng.modules.application.repository.model.MediaCheckRecordDO;
import com.fmx.xiaomeng.modules.application.service.*;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.fmx.xiaomeng.modules.application.websocket.service.NewMsgSendService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.fmx.xiaomeng.common.constant.GlobalConstants.initAvatarList;
import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.UPDATE_SUBSCRIBE_RECORD_ERROR;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/

@Slf4j
@Configuration
public class WxMaMessageConfiguration {

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("收到消息：" + wxMessage.toString());
        return null;
    };
    //        用户给客服发消息
    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("用户给客服发消息"+wxMessage.toString() + context.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("人工客服正在快马加鞭赶来，若着急可添加微信：R8ider，并备注 生态圈客服，未备注不予通过哦")
                .toUser(wxMessage.getFromUser()).build());
        return null;
    };
    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        return null;
    };
    //二维码
    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        return null;
    };
    @Autowired
    private MediaCheckRecordDOMapper mediaCheckRecordDOMapper;
    @Autowired
    private ArticleService articleService;



    @Autowired
    private CommentService articleCommentService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserDOMapper userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Converter converter;
    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private ArticleCommentDOMapper articleCommentDOMapper;


    @Autowired
    private NewMsgSendService newMsgSendService;
    @Autowired
    private RedissonLockUtil redissonLockUtil;
    //    写一个处理敏感图片检测结果handler
    private final WxMaMessageHandler ImgCheckMsgHandler = (wxMessage, context, service, sessionManager) -> {
        log.info("小程序后台给我发消息啦,敏感图片检测结果:" + wxMessage.toString());
        if (!"100".equals(wxMessage.getResult().getLabel())) {
            String traceId = wxMessage.getTraceId();
            MediaCheckRecordDO mediaCheckRecordDO = mediaCheckRecordDOMapper.selectByTraceId(traceId);
            Long userId = mediaCheckRecordDO.getUserId();
            Integer mediaType = mediaCheckRecordDO.getMediaType();
            Integer belongType = mediaCheckRecordDO.getBelongType();
            Long belongId = mediaCheckRecordDO.getBelongId();

            NotificationModel notificationModel;
            if (mediaType == 0) { //0图片,1音频
                switch (belongType) {
                    case 0: // 0帖子、 2评论帖子、4头像
                        ArticleModel detail = articleService.detail(belongId);
                        if(Objects.nonNull(detail)){
                            try {
                                articleService.deleteArticle(belongId);


                            } catch (Exception e) {
                            }

                            notificationModel = new NotificationModel();
                            notificationModel.setNoticeType(NotificationTypeEnum.ARTICLE_VIOLATION.getCode());
                            notificationModel.setContent("您的帖子中有敏感图片，已被删除，多次违规将被禁言或封号");

                            notificationModel.setArticleId(belongId);
                            notificationModel.setTargetUserId(userId);
                            notificationService.createNotification(notificationModel);
                        }




                        break;
                    case 2:
                        articleCommentService.deleteComment(belongId, userId);

                        notificationModel = new NotificationModel();
                        notificationModel.setNoticeType(NotificationTypeEnum.COMMENT_VIOLATION.getCode());
                        notificationModel.setContent("您的评论包含敏感图片，已被删除，多次违规将被禁言或封号");
                        notificationModel.setTargetUserId(userId);
                        notificationModel.setSourceCommentId(belongId);
                        notificationService.createNotification(notificationModel);
                        break;
                    case 4:
                        UserModel userModel = new UserModel();
                        userModel.setUserId(belongId);
                        Random random = new Random();
                        int randomIndex = random.nextInt(initAvatarList.length);
                        userModel.setAvatar(new OssFileModel(null, initAvatarList[randomIndex]));

                        redisUtil.delete("userId:" + belongId);
                        userModel.setModifiedTime(new Date());
                        userDAO.updateByUserId(converter.convert(userModel));

                        articleCommentDOMapper.updateMyCommentsNoAnonymous(belongId, "", null);
                        articleDOMapper.updateMyArticleNoAnonymous(belongId, "", null);


                        notificationModel = new NotificationModel();
                        notificationModel.setNoticeType(NotificationTypeEnum.AVATAR_VIOLATION.getCode());
                        notificationModel.setContent("您的头像为敏感图片，多次违规将被禁言或封号");
                        notificationModel.setTargetUserId(userId);
                        notificationService.createNotification(notificationModel);
                        break;
                    default:
                        break;
                }

                if (newMsgSendService.isOnline(userId)) {
                    Map<String, String> msg = new HashMap<>();
                    msg.put("type", "NEW_SYSTEM_NOTICE");
                    //不用管数量，有新的消息就发，前端自己加1，查看后（或下拉刷新）清零
                    msg.put("num", "1");
                    newMsgSendService.sendTextMessage(userId, msg);
                }
            }

            //        这里要做的是设置帖子为删除状态(或者评论)，给作者发一个Notification消息，告知违规并警告，然后user表加一个用户违规次数字段，
            //    达到违规次数后，禁言几天，再多封号，
            System.out.println("测试");
            userService.addViolationTimes(userId);


//            更用户发在线通知

        }
        return null;
    };

    @Bean
    public WxMaMessageRouter wxMaMessageRouter(WxMaService wxMaService) {
        final WxMaMessageRouter router = new WxMaMessageRouter(wxMaService);
        router
//                https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/customer-message/receive.html#XML-%E6%A0%BC%E5%BC%8F
                .rule().msgType("event").event("user_enter_tempsession").handler(logHandler).next()  //用户进入客服会话
                .rule().async(false).msgType("text").handler(textHandler).end() //用户在客服会话中发送文本信息
                .rule().async(false).msgType("image").handler(picHandler).end()
                .rule().async(false).content("二维码").handler(qrcodeHandler).end()
//                https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/sec-center/sec-check/mediaCheckAsync.html
                .rule().async(false).msgType("event").event("wxa_media_check").handler(ImgCheckMsgHandler).end(); //图片违规审核结果

        return router;
    }


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WxMaMessageConfiguration.class);
}
