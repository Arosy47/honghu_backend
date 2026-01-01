package com.fmx.xiaomeng.common.utils;

import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.http.HttpClientUtil;
import com.fmx.xiaomeng.modules.application.model.WXProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
@Slf4j
@Component
public class CheckMessageUtil {

    @Autowired
    private MpTokenUtil mpTokenUtil;


    @Deprecated
    public Boolean checkMessage(String content, WXProperties wxProperties) throws BusinessException {
        String token = mpTokenUtil.get(wxProperties);
        String result = this.checkMsg(token, content);
        return true;
    }

    /**
     * 这个接口已经调通，目前来看是应该把整个请求体都转成utf-8编码，
     * 而不是单纯content，请求头的ContentType也用utf-8，参考了WxJava，me.chanjar.weixin.common.util.http.apache.ApacheSimplePostRequestExecutor
     * @param token
     * @param content
     * @return
     */
    private String checkMsg(String token, String content){

        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + token;

        Map<String, String> param = new HashMap<String, String>();
        param.put("content", content);
//       scene： 1 资料；2 评论；3 论坛；4 社交日志
        param.put("scene", "3");
        param.put("version","2");
        param.put("openid","of6h15DajolR9aYy1Un2kfoYXdyQ");
        String result= HttpClientUtil.doPostNew(url, param);

        return result;
    }
}
