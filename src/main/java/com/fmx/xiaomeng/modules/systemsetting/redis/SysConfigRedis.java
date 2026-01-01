
package com.fmx.xiaomeng.modules.systemsetting.redis;

import com.fmx.xiaomeng.common.utils.redis.RedisKeys;
import com.fmx.xiaomeng.common.utils.redis.RedisUtil;
import com.fmx.xiaomeng.modules.systemsetting.entity.BgConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统配置Redis
 *
 */
@Component
public class SysConfigRedis {
    @Autowired
    private RedisUtil redisUtils;

    public void saveOrUpdate(BgConfigModel config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getSchoolId(), config.getConfigName());
        redisUtils.set(key, config);
    }

    public void delete(Integer schoolId,String configName) {
        String key = RedisKeys.getSysConfigKey(schoolId, configName);
        redisUtils.delete(key);
    }

    public BgConfigModel get(Integer schoolId,String configName){
        String key = RedisKeys.getSysConfigKey(schoolId, configName);
        return redisUtils.get(key, BgConfigModel.class);
    }

}
