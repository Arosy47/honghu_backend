package com.fmx.xiaomeng.modules.application.service.impl;
import com.fmx.xiaomeng.modules.application.service.WxMsgSubscribeService;
import com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel;
import com.fmx.xiaomeng.common.enums.WxMsgTemplateTypeEnum;
import org.springframework.stereotype.Service;
@Service
public class WxMsgSubscribeServiceImpl implements WxMsgSubscribeService {
    @Override
    public WxMsgSubscribeRecordModel query(Long userId, WxMsgTemplateTypeEnum type) { return null; }

    @Override public java.util.List<com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel> query(Long userId) { return java.util.Collections.emptyList(); }
}