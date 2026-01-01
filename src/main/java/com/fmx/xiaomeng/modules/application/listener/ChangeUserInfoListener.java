package com.fmx.xiaomeng.modules.application.listener;

import com.fmx.xiaomeng.common.service.WxInterfaceOperateService;
import com.fmx.xiaomeng.common.utils.EnvUtil;
import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.modules.application.repository.dao.*;
import com.fmx.xiaomeng.modules.application.repository.model.MediaCheckRecordDO;
import com.fmx.xiaomeng.modules.application.service.NotificationService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Slf4j
@Component
public class ChangeUserInfoListener extends EventBus.EventHandler<UserModel> {

    @Resource
    private NotificationService notificationService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleDOMapper articleDOMapper;

    @Resource
    private ArticleCommentDOMapper articleCommentDOMapper;

    // @Resource
    // private PickUpPackageDOMapper pickUpPackageDOMapper;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private MediaCheckRecordDOMapper mediaCheckRecordDOMapper;


    @Autowired
    private WxInterfaceOperateService wxInterfaceOperateService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.CHANGE_USER_INFO;
    }

    @Override
    public void onMessage(UserModel userModel) throws IOException {

        Long userId = userModel.getUserId();
        String nickName = StringUtils.isNotBlank(userModel.getNickName())?userModel.getNickName():null;
//        String anonymousName = userModel.getAnonymousName();
        String avatarUrl = Objects.nonNull(userModel.getAvatar()) ? userModel.getAvatar().getUrl() : null;

        if (envUtil.isOnline() && !StringUtils.isEmpty(avatarUrl) && StringUtils.isNotBlank(userModel.getOpenId())) {
//          头像敏感校验
            try {
                UserModel userInfo = userService.getUserInfo(userId);
                String traceId = wxInterfaceOperateService.checkImage(avatarUrl, userInfo.getOpenId());

                MediaCheckRecordDO mediaCheckRecordDO = new MediaCheckRecordDO();
                mediaCheckRecordDO.setTraceId(traceId);
                mediaCheckRecordDO.setUserId(userId);
                mediaCheckRecordDO.setMediaType(0); //0图片,1音频
                mediaCheckRecordDO.setBelongType(4); ////0帖子、1组局帖、 2评论帖子、3评论组局、4头像 5私信消息、6匿名聊天消息 等
                mediaCheckRecordDO.setBelongId(userId);
                mediaCheckRecordDOMapper.insertSelective(mediaCheckRecordDO);
            } catch (WxErrorException e) {
                log.error("敏感图片校验接口异常！！！");
            }
        }


        if (!StringUtils.isEmpty(nickName) || !StringUtils.isEmpty(avatarUrl)) {
//            组局支持匿名，组局评论暂时不支持匿名，帖子和帖子评论都支持匿名
            articleDOMapper.updateMyArticleNoAnonymous(userId, avatarUrl, nickName);
        }

    }


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangeUserInfoListener.class);
}

