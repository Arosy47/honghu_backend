package com.fmx.xiaomeng.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description cdn鉴权后url， 要写在后端，因为如果要紧急修改，前端发布时间不受控制
 * @Date 2025-12-20
 * @Author honghu
 **/
public class CdnSignUtil {
    final String CDN_PATH = "cdn.vdvdv.com";

//    e.g. cdn.vdvdv.com/avatar/7777777_1715775434060.webp
    public static String generateSignedUrl(String url) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        //获取当前时间戳
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);

        final String CDN_AUTH_MAIN_KEY = "xKQ5d013pkE2KEqw";

        String[] split = url.split("cdn.vdvdv.com");
        String uri = split[split.length - 1];

        //获取md5字符串
        String sign = CDN_AUTH_MAIN_KEY + uri + nowTimeStamp;
        String paramSign = DigestUtils.md5Hex(sign);

        String retUrl = url + "?sign=" + paramSign + "&t=" + nowTimeStamp;
        return retUrl;
    }
}

