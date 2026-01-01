
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 */
@Data
//@TableName("sys_role")
public class AdminRoleModel {
	
	/**
	 * 角色ID
	 */
//	@TableId
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建者ID
	 */
	private Long createUserId;

//	@TableField(exist=false)
	private List<Long> menuIdList;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	

    public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
    public Long getRoleId() { return roleId; }
    public java.util.List<Long> getMenuIdList() { return menuIdList; }

    public Long getCreateUserId() { return createUserId; }

    public void setMenuIdList(java.util.List<Long> menuIdList) { this.menuIdList = menuIdList; }
    public void setCreateUserId(Long createUserId) { this.createUserId = createUserId; }
}
