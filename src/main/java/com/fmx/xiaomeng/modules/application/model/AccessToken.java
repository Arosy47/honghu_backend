package com.fmx.xiaomeng.modules.application.model;

import lombok.Data;

/**
 * 访问wx接口都需要access_token
 */
@Data
public class AccessToken {
    private String access_token;

    private Integer expires_in;


    public int getExpires_in() { return expires_in; }
    public String getAccess_token() { return access_token; }
}
