package com.fmx.xiaomeng.modules.application.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "audience")
@Component
public class Audience {
    private String clientId;
    private String base64Secret;
    private String name;
    private int expiresSecond;

    public String getBase64Secret() { return base64Secret; }
    public String getName() { return name; }
    public String getClientId() { return clientId; }
}
