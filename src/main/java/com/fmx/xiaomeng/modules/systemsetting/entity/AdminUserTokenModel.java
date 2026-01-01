package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

@Data
public class AdminUserTokenModel {
    String token;

    int expire;


    public void setToken(String token) { this.token = token; }
    public void setExpire(int expire) { this.expire = expire; }

}
