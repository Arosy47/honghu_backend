package com.fmx.xiaomeng.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Component
public class EnvUtil {
    private static final String DEV = "dev";

    /**
     * 预发（远程debug需要）
     */
    private static final String STAGE = "stage";

    private static final String PRODUCTION = "production";

    @Value("${spring.profiles.active}")
    private String env;

    public boolean isOnline(){
        return PRODUCTION.equalsIgnoreCase(env.trim()) || STAGE.equalsIgnoreCase(env.trim());
    }

    public boolean isDev(){
        return DEV.equalsIgnoreCase(env.trim());
    }
}
