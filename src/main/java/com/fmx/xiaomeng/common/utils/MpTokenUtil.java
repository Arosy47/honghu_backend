package com.fmx.xiaomeng.common.utils;

import com.fmx.xiaomeng.common.utils.http.HttpClientUtil;
import com.fmx.xiaomeng.common.utils.redis.RedisKeys;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.application.model.AccessToken;
import com.fmx.xiaomeng.modules.application.model.WXProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description  使用wxMaService的接口来操作，不需要自己管理AccessToken了
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
@Deprecated
public class MpTokenUtil {
    @Autowired
    private RedisUtil redisUtils;

    public void delete(String configKey) {
        String key = RedisKeys.getMpTokenKey();
        redisUtils.delete(key);
    }

    public String get(WXProperties wxProperties) {
        String key = RedisKeys.getMpTokenKey();
        AccessToken accessToken = redisUtils.get(key, AccessToken.class);
        if (accessToken == null) {
            String s = HttpClientUtil.
                    doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxProperties.getWxId() + "&secret=" + wxProperties.getWxSecret());
            accessToken = JsonUtils.jsonToPojo(s, AccessToken.class);
            // 缓存过期时间设置为7200秒-200秒，比token时间短一些
            redisUtils.set(key, accessToken, accessToken.getExpires_in() - 200);
        }
        return accessToken.getAccess_token();
    }
}
