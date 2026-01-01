package com.fmx.xiaomeng.modules.application.websocket.service;

import java.util.Map;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public interface NewMsgSendService {

    void sendTextMessage(Long toUserId, Map<String, String> msg);

    Boolean isOnline(Long userId);
}
