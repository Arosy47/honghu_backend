
package com.fmx.xiaomeng.modules.systemsetting.form;

import lombok.Data;

/**
 * 登录表单
 *
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;



    public String getUuid() { return uuid; }
    public String getCaptcha() { return captcha; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
