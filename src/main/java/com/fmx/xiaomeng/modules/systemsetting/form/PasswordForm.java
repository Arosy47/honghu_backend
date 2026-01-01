
package com.fmx.xiaomeng.modules.systemsetting.form;

import lombok.Data;

/**
 * 密码表单
 *
 */
//@ApiModel(value="PasswordForm对象", description="密码表单请求参数对象")
@Data
public class PasswordForm {
    /**
     * 原密码
     */
//    @ApiModelProperty(value = "原密码",required = true)
    private String password;
    /**
     * 新密码
     */
//    @ApiModelProperty(value = "新密码",required = true)
    private String newPassword;



    public String getNewPassword() { return newPassword; }
    public String getPassword() { return password; }
}
