package com.fmx.xiaomeng.common.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMediaAsyncCheckResult;
import cn.binarywang.wx.miniapp.bean.security.WxMaMediaSecCheckCheckRequest;
import cn.binarywang.wx.miniapp.bean.security.WxMaMsgSecCheckCheckRequest;
import cn.binarywang.wx.miniapp.bean.security.WxMaMsgSecCheckCheckResponse;
import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.service.WxInterfaceOperateService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.UNKNOWN_ERROR;

/**
 * @author honghu
 * @date 2025-12-19
 */

@RequiredArgsConstructor
@CustomLog
@Service
public class WxInterfaceOperateServiceImpl implements WxInterfaceOperateService {
    private final WxMaService wxService;

    private final UserService userService;

    public Boolean checkMessage(String content, String openId) throws WxErrorException {
        if (StringUtils.isBlank(content)) {
            return true;
        }
        if (StringUtils.isBlank(openId)) {
            return true;
        }

        WxMaMsgSecCheckCheckRequest request = WxMaMsgSecCheckCheckRequest.builder()
                .content(content)
                //scene： 1 资料；2 评论；3 论坛；4 社交日志
                .scene(2)
                .version("2")
                .openid(openId)
                .build();

        try {
            WxMaMsgSecCheckCheckResponse response = this.wxService.getSecCheckService().checkMessage(request);
            if (response.getErrcode() == 0) {
                if (!"100".equals(response.getResult().getLabel())) {
                    log.warn("检测到敏感词: content{}, label:{}, suggest:{}, response:{}",
                            content, response.getResult().getLabel(), response.getResult().getSuggest(), JSON.toJSONString(response));

                    if ("risky".equals(response.getResult().getSuggest())
//                            || "review".equals(response.getResult().getSuggest())
                    ) {
                        return false;
                    }
                }
            } else {
                throw new BusinessException(UNKNOWN_ERROR, "校验敏感词返回错误，错误信息为" + response.getErrmsg());
            }
            return true;

        } catch (Exception e) {
            log.error("内容检测接口报错, error:{}", JSON.toJSONString(e));

//            报错不阻断主流程吧
            return true;
        }

    }

    @Override
    public String checkImage(String url, String openId) throws WxErrorException {

        WxMaMediaSecCheckCheckRequest request = WxMaMediaSecCheckCheckRequest
                .builder()
                .mediaUrl(url)
                .mediaType(2) //1.音频，2.图片
                .version(2)
                .scene(2)
                .openid(openId).build();
//        请求失败会重试，所以一般不需要返回值
        WxMaMediaAsyncCheckResult wxMaMediaAsyncCheckResult = this.wxService.getSecCheckService().mediaCheckAsync(request);
        return wxMaMediaAsyncCheckResult.getTraceId();
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WxInterfaceOperateServiceImpl.class);
}
