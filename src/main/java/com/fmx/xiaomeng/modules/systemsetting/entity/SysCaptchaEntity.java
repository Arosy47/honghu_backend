
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.util.Date;

/**
 * 系统验证码
 *
 */
@Data
//@TableName("sys_captcha")
public class SysCaptchaEntity {
//    @TableId(type = IdType.INPUT)
    private String uuid;
    /**
     * 验证码
     */
    private String code;
    /**
     * 过期时间
     */
    private Date expireTime;


    public void setUuid(String uuid) { this.uuid = uuid; }
    public void setCode(String code) { this.code = code; }
    public void setExpireTime(java.util.Date expireTime) { this.expireTime = expireTime; }


    public String getCode() { return code; }
    public Date getExpireTime() { return expireTime; }

}
