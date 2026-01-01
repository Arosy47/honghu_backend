
package com.fmx.xiaomeng.common.utils.redis;

/**
 * Redis所有Keys
 *
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class RedisKeys {

    public static String getSysConfigKey(Integer schoolId,String configName){
        return "sys:config:" + configName + "_"+schoolId;
    }

    /**
     * 获取小程序token
     * @return
     */
    public static String getMpTokenKey(){
        return "mp_token";
    }
}
