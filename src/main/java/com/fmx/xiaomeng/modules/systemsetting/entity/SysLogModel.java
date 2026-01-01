
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 *
 */
@Data
//@TableName("sys_log")
public class SysLogModel implements Serializable {
	private static final long serialVersionUID = 1L;
//	@TableId
	private Long id;
	//用户名
	private Long userId;
	//用户名
	private String userName;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long duration;
	//IP地址
	private String ip;
	//创建时间
	private Date createTime;



    public void setOperation(String operation) { this.operation = operation; }
    public void setMethod(String method) { this.method = method; }
    public void setParams(String params) { this.params = params; }
    public void setIp(String ip) { this.ip = ip; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setDuration(Long duration) { this.duration = duration; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
