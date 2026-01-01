package com.fmx.xiaomeng.modules.application.websocket.pubsub;

import lombok.CustomLog;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
@CustomLog
public class MessagePublisher {
    @Autowired
    private RedissonClient redissonClient;

    public void publish(String topic, String msg) {

    }


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessagePublisher.class);
}
