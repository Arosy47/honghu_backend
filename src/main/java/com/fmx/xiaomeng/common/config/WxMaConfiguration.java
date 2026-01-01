package com.fmx.xiaomeng.common.config;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaQrcodeServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfiguration {

    private final WxMaProperties properties;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    public WxMaConfiguration(WxMaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxMaService wxMaService() {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }
        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(
                configs.stream()
                        .map(a -> {
//                    WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                            WxMaDefaultConfigImpl config = new WxMaRedissonConfigImpl(redissonClient);
                            // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
                            config.setAppid(a.getAppid());
                            config.setSecret(a.getSecret());
                            config.setToken(a.getToken());
                            config.setAesKey(a.getAesKey());
                            config.setMsgDataFormat(a.getMsgDataFormat());
                            return config;
                        }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        return maService;
    }

    @Bean
    public WxMaQrcodeService wxMaQrcodeService(WxMaService wxMaService) {
        WxMaQrcodeService qrcodeService = new WxMaQrcodeServiceImpl(wxMaService);
        return qrcodeService;
    }


//    公众号h5开发时，要打开下面的  代码：https://github.com/binarywang/weixin-java-mp-demo
//    @Bean
//    public WxMpService wxMpService() {
//        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
//        final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
//        if (configs == null) {
//            throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
//        }
//
//        WxMpService service = new WxMpServiceImpl();
//        service.setMultiConfigStorages(configs
//                .stream().map(a -> {
////                    WxMpDefaultConfigImpl configStorage;
////                    if (this.properties.isUseRedis()) {
////                        final WxMpProperties.RedisConfig redisConfig = this.properties.getRedisConfig();
////                        JedisPoolConfig poolConfig = new JedisPoolConfig();
////                        JedisPool jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
////                                redisConfig.getTimeout(), redisConfig.getPassword());
////                        configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(jedisPool), a.getAppId());
////                    } else {
////                        configStorage = new WxMpDefaultConfigImpl();
////                    }
//
//                    WxMpDefaultConfigImpl config = new WxMpRedissonConfigImpl(redissonClient);
//
//                    configStorage.setAppId(a.getAppId());
//                    configStorage.setSecret(a.getSecret());
//                    configStorage.setToken(a.getToken());
//                    configStorage.setAesKey(a.getAesKey());
//                    return configStorage;
//                }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
//        return service;
//    }

}
