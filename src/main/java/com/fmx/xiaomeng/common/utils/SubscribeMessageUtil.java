package com.fmx.xiaomeng.common.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.CustomLog;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@CustomLog
@Component
public class SubscribeMessageUtil {
    @Autowired
    private WxMaService wxService;

    public void sendSubscribeMessage(Long userId, WxMaSubscribeMessage message) {
        if (message.getToUser() == null || message.getToUser().isEmpty()) {
            return;
        }
        try {
            wxService.getMsgService().sendSubscribeMsg(message);
        } catch (WxErrorException e) {
            log.error("sendSubscribeMessage error", e);
        }
    }

    @Deprecated
    public void subscribeMessage(String toUserOpenId, String templateId, String page, Map<String, TemplateData> map) {
    }

    @Data
    public static class TemplateData {
        private String value;

        public TemplateData(String value) {
            this.value = value;
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SubscribeMessageUtil.class);
}
