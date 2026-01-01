package com.fmx.xiaomeng.modules.application.controller.response;

import lombok.Data;

/**
 * @Description
 * @Date 2023/3/8 0:25
 * @Author honghu
 **/
@Data
public class CosPolicyModel {
    public String cosHost;
    public String cosKey;
    public String qSignAlgorithm;
    public String qAk;
    public String qKeyTime;
    public String qSignature;
    public String policy;
    @Deprecated
    public String securityToken;

}
