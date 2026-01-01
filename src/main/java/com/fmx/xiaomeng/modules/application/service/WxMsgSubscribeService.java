package com.fmx.xiaomeng.modules.application.service;
import com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel;
import com.fmx.xiaomeng.common.enums.WxMsgTemplateTypeEnum;
public interface WxMsgSubscribeService {
    WxMsgSubscribeRecordModel query(Long userId, WxMsgTemplateTypeEnum type);

    java.util.List<com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel> query(Long userId);
}