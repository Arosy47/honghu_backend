package com.fmx.xiaomeng.modules.application.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "wx") //接收application.wx
public class WXProperties {
    private String wxId;


    private String wxSecret;


    public String getWxId() { return wxId; }
    public String getWxSecret() { return wxSecret; }
}
