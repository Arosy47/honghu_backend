package com.fmx.xiaomeng.modules.application.websocket.pubsub;

import lombok.CustomLog;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
@CustomLog
public class MessageSubscriber {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MessagePublisher messagePublisher;
    @PostConstruct
    public void subscribeToTopic() {

    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessageSubscriber.class);
}
