
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 */
@Data
//@TableName("sys_user_role")
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
//	@TableId
	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 角色ID
	 */
	private Long roleId;

	

    public void setUserId(Long userId) { this.userId = userId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
