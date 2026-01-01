
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理
 *
 */
@Data
//@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
//	@TableId
	private Long menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;
	
	/**
	 * 父菜单名称
	 */
//	@TableField(exist=false)
	private String parentName;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;
	
	/**
	 * ztree属性
	 */
//	@TableField(exist=false)
	private Boolean open;

//	@TableField(exist=false)
	private List<SysMenuEntity> list=new ArrayList<>();


    public String getName() { return name; }
    public Long getParentId() { return parentId; }
    public Integer getType() { return type; }
    public String getUrl() { return url; }


    public void setMenuId(long menuId) { this.menuId = menuId; }
    public void setName(String name) { this.name = name; }
    public void setParentId(long parentId) { this.parentId = parentId; }
    public void setOpen(boolean open) { this.open = open; }

}
