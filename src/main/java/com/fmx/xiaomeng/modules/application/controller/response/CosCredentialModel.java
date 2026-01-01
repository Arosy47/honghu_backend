package com.fmx.xiaomeng.modules.application.controller.response;

import lombok.Data;

/**
 * @Description
 * @Date 2023/3/8 0:25
 * @Author honghu
 **/
@Data
public class CosCredentialModel {
    public String tmpSecretId;
    public String tmpSecretKey;
    public String sessionToken;
    public long startTime;
    public long expiredTime;
//    public String token;



    public void setTmpSecretId(String tmpSecretId) { this.tmpSecretId = tmpSecretId; }
    public void setTmpSecretKey(String tmpSecretKey) { this.tmpSecretKey = tmpSecretKey; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public void setExpiredTime(long expiredTime) { this.expiredTime = expiredTime; }

}
