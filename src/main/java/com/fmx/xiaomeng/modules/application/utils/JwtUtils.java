package com.fmx.xiaomeng.modules.application.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * jwt工具类
 */
@ConfigurationProperties(prefix = "jwt.audience")
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private String secret;
    private long expire;
    private String header;

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }

    /**
     * 生成令牌。
     * 原理说明：使用对称密钥进行签名，载荷仅包含用户标识与过期时间。
     */
    public String generateToken(long userId) {
        Date issuedAt = Date.from(Instant.now());
        Date expiresAt = new Date(this.getExpireTimeStamp());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(userId))
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.debug("token parse failed", e);
            return null;
        }
    }

    public Long getExpireTimeStamp() {
        long now = System.currentTimeMillis();
        return now + expire * 1000;
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration == null || expiration.before(new Date());
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    // 根据配置的密钥生成签名素材，保持与签名算法一致
    private byte[] getSignatureMaterial() {
        return secret.getBytes(StandardCharsets.UTF_8);
    }
}
