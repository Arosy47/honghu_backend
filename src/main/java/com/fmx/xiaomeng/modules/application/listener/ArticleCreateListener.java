package com.fmx.xiaomeng.modules.application.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.common.service.WxInterfaceOperateService;
import com.fmx.xiaomeng.common.utils.EnvUtil;
import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleDOMapper;
import com.fmx.xiaomeng.modules.application.repository.dao.MediaCheckRecordDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.MediaCheckRecordDO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.websocket.service.NewMsgSendService;
import lombok.CustomLog;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fmx.xiaomeng.modules.application.websocket.NewMessageRemindWebSocket.NEW_MSG_ONLINE_USER_ID_SET_KEY;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/4
 * @desc
 **/
@Component
//@Slf4j
@CustomLog
public class ArticleCreateListener extends EventBus.EventHandler<ArticleModel> {


    @Resource
    private ArticleDOMapper dao;

    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;

    @Autowired
    private Converter converter;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private MediaCheckRecordDOMapper mediaCheckRecordDOMapper;

    @Autowired
    private WxInterfaceOperateService wxInterfaceOperateService;
    @Autowired
    private NewMsgSendService newMsgSendService;
    @Autowired
    private RedisUtil redisUtil;

    public static void main(String[] args) {
        Map<String, String> msg = new HashMap<>();
        msg.put("type", "NEW_ARTICLE");
        //不用管数量，有新的消息就发，前端自己加1，查看后（或下拉刷新）清零
        msg.put("num", "1");

        Map<String, Object> map = new HashMap<>();
        map.put("toUserId", 123);
        map.put("message", JSON.toJSONString(msg));

        System.out.println(JSON.toJSONString(map));


//        JSON.toJSONString(map);
//
//
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map));

        System.out.println(jsonObject);


        Long toUserId = jsonObject.getLong("toUserId");
        String message = jsonObject.getString("message");
        System.out.println(toUserId);
        System.out.println(message);

        System.out.println("{\"num\":\"1\",\"type\":\"NEW_ARTICLE\"}");
        System.out.println((JSON.toJSONString("{\"num\":\"1\",\"type\":\"NEW_ARTICLE\"}")));

    }

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_CREATE;
    }

    @Override
    public void onMessage(ArticleModel article) {

        Long userId = article.getUserId();
        UserModel userModel = userService.getUserInfo(userId);
        if (envUtil.isOnline()) {
            List<OssFileModel> imgUrlList = article.getImgUrlList();
            if (!CollectionUtils.isEmpty(imgUrlList) && StringUtils.isNotBlank(userModel.getOpenId())) {

//                图片敏感校验
                imgUrlList.forEach(imgUrl -> {
                    try {
                        String traceId = wxInterfaceOperateService.checkImage(imgUrl.getUrl(), userModel.getOpenId());
                        MediaCheckRecordDO mediaCheckRecordDO = new MediaCheckRecordDO();
                        mediaCheckRecordDO.setTraceId(traceId);
                        mediaCheckRecordDO.setUserId(userId);
                        mediaCheckRecordDO.setMediaType(0); //0图片,1音频
                        mediaCheckRecordDO.setBelongType(0); //0帖子、1组局帖、 2评论帖子、3评论组局、4头像 5私信消息、6匿名聊天消息 等
                        mediaCheckRecordDO.setBelongId(article.getId());
                        mediaCheckRecordDOMapper.insertSelective(mediaCheckRecordDO);
                    } catch (WxErrorException e) {
                        log.error("敏感图片校验接口异常！！！");
                    }
                });
            }
        }


            //        给在线的每个人发消息
            String newMsgOnlineUserIdKey = NEW_MSG_ONLINE_USER_ID_SET_KEY + article.getSchoolId();
            Set<Object> userIdList = redisUtil.zsetGet(newMsgOnlineUserIdKey);

//        for (Map.Entry<Long, Session> entry : NewMessageRemindWebSocket.sessionPool.entrySet()){
            for (Object onlineUserId : userIdList) {
                Map<String, String> msg = new HashMap<>();
                msg.put("type", "NEW_ARTICLE");
                //不用管数量，有新的消息就发，前端自己加1，查看后（或下拉刷新）清零
                msg.put("num", "1");

                newMsgSendService.sendTextMessage(Long.valueOf(onlineUserId.toString()), msg);

            }

    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ArticleCreateListener.class);
}
