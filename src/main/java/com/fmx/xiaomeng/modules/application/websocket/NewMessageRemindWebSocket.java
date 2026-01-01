package com.fmx.xiaomeng.modules.application.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmx.xiaomeng.common.enums.MsgTypeEnum;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.utils.SpringContextUtil;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
@CustomLog
@ServerEndpoint("/newMessageRemindWebSocket/{userId}")
public class NewMessageRemindWebSocket {
    public static final String NEW_MSG_ONLINE_USER_ID_SET_KEY = "NEW_MSG_ONLINE_USER_ID_SET_";

    public static final String NEW_MSG_ONLINE_KEY_PREFIX = "NEW_MSG_ONLINE_";
    //所有session
    public static Map<Long, Session> sessionPool = new ConcurrentHashMap<>();
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //    自己的id标识
    private Long userId = -1L;

    private Integer strollSchoolId = -1;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) throws IOException {

    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

    }



    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewMessageRemindWebSocket.class);
}
