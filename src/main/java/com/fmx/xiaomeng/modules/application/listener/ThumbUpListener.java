package com.fmx.xiaomeng.modules.application.listener;

import com.fmx.xiaomeng.common.utils.EventBus;
import com.fmx.xiaomeng.modules.application.service.model.ThumbUpModel;
import com.fmx.xiaomeng.modules.application.websocket.service.NewMsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Component
public class ThumbUpListener extends EventBus.EventHandler<ThumbUpModel> {

    @Autowired
    private NewMsgSendService newMsgSendService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.THUMB_UP;
    }

    @Override
    public void onMessage(ThumbUpModel thumbUpModel) throws IOException {
        Long toUserId = thumbUpModel.getToUserId();
//        Session session = NewMessageRemindWebSocket.sessionPool.get(toUserId);
        //如果在线就发送
//        if(Objects.nonNull(session)){
        if (newMsgSendService.isOnline(toUserId)) {
            Map<String, String> msg = new HashMap<>();
            msg.put("type", "NEW_THUMB_UP");
            //不用管数量，有新的消息就发，前端自己加1，查看后（或下拉刷新）清零
            msg.put("num", "1");
            newMsgSendService.sendTextMessage(toUserId, msg);
//            session.getBasicRemote().sendText(JSON.toJSONString(msg));
        }

    }


}
