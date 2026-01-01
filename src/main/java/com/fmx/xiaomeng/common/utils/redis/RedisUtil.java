package com.fmx.xiaomeng.common.utils.redis;

import com.google.gson.Gson;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@CustomLog
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
public class RedisUtil {
    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;


    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public <T> T get(String key, Type type) {
        String value = valueOperations.get(key);
        return value == null ? null : gson.fromJson(value, type);
    }


    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    public void enqueue(String key, Long value) {
        if(Objects.isNull(value) || value==-1L){
            return;
        }
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        operations.leftPush(key, String.valueOf(value));
    }

    public Long dequeue(String key) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        String res = (String) operations.rightPop(key);
        if(StringUtils.isNotBlank(res)){
            return Long.valueOf(res);
        }
        return null;
    }

    public void queueRemove(String key, Long value) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        operations.remove(key, 0, String.valueOf(value));
    }


    public void zsetAdd(String key, Object value) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        setOps.add(key, value);
    }

    public void zsetDel(String key, Object... element) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Long removedCount = setOps.remove(key, element);
    }

    public Set<Object> zsetGet(String key) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Set<Object> members = setOps.members(key);
        return members;
    }

    public Long zsetSize(String key) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Long size = setOps.size(key);
        return size;
    }

    public Boolean zsetIsMember(String key, String member) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Boolean isMember = setOps.isMember(key, member);
        return isMember;
    }



    public void queueBack(String key, Long value) {}
}
