package com.fmx.xiaomeng.modules.application.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一管理session、websocket、curUser
 */
public class CurPool {

    // list 里面第一个存sessionId，第二个存session    只存储本服务器连接的用户
    public static Map<Long, List<Object>> sessionPool = new ConcurrentHashMap<>();


}
