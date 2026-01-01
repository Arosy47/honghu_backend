
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户Token
 *
 */
@Data
//@TableName("sys_user_token")
public class SysUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private Long userId;
	//token
	private String token;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;



    public void setUserId(long userId) { this.userId = userId; }
    public void setToken(String token) { this.token = token; }

}
