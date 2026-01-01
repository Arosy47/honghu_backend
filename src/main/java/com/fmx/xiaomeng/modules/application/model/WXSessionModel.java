package com.fmx.xiaomeng.modules.application.model;

import lombok.Data;

/**
 * WXSessionModel是token的一部分，key是user，登录的时候返回给前端了，看一下前端怎么处理的
 */
@Data
public class WXSessionModel {
//    private String session_key;
    private Long expire;
    private String token;
//    private String phoneNumber;
    private Long userId;
//    private Integer schoolId;

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setExpire(Long expire) { this.expire = expire; }
    public void setToken(String token) { this.token = token; }
}
