package com.fmx.xiaomeng.modules.application.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.enums.WebsocketTopicEnum;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.websocket.pubsub.MessagePublisher;
import com.fmx.xiaomeng.modules.application.websocket.service.NewMsgSendService;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.fmx.xiaomeng.modules.application.websocket.NewMessageRemindWebSocket.NEW_MSG_ONLINE_KEY_PREFIX;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Service
@CustomLog
public class NewMsgSendServiceImpl implements NewMsgSendService {

    @Autowired
    private MessagePublisher messagePublisher;
    @Autowired
    private RedisUtil redisUtil;


    public void sendTextMessage(Long toUserId, Map<String, String> msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("toUserId", toUserId);
        map.put("message", msg);
        messagePublisher.publish(WebsocketTopicEnum.NEW_MSG.name(), JSON.toJSONString(map));
    }

    public Boolean isOnline(Long userId){
        String newMsgOnlineKey = NEW_MSG_ONLINE_KEY_PREFIX + userId;
        String isOnline = redisUtil.get(newMsgOnlineKey);
        log.info("用户{}是否在线:{}",userId, isOnline);
        return StringUtils.isNotBlank(isOnline);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewMsgSendServiceImpl.class);
}
