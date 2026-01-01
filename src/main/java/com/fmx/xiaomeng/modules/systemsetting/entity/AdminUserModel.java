package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author honghu
 * @date 2025-12-20
 */
@Data
public class AdminUserModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 学校ID
	 */
	private Integer schoolId;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;


    public Long getUserId() { return userId; }
    public Integer getStatus() { return status; }

    public Integer getSchoolId() { return schoolId; }

    public String getPassword() { return password; }
    public String getSalt() { return salt; }

    public void setRoleIdList(java.util.List<Long> roleIdList) { this.roleIdList = roleIdList; }


    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return userName; }
    public java.util.List<Long> getRoleIdList() { return roleIdList; }
    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }

    public void setPassword(String password) { this.password = password; }
    public void setSalt(String salt) { this.salt = salt; }
    public Long getCreateUserId() { return createUserId; }
}
