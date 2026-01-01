
package com.fmx.xiaomeng.modules.systemsetting.service;


import com.fmx.xiaomeng.modules.systemsetting.entity.AdminMenuModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysMenuEntity;

import java.util.List;


/**
 * 菜单管理
 *
 */
public interface AdminMenuService {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();

	/**
	 * 获取用户菜单列表
	 */
	List<AdminMenuModel> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);

	List<AdminMenuModel> list();



	SysMenuEntity queryByPrimaryKey(Long menuId);

	void save(SysMenuEntity menu);

	void updateById(SysMenuEntity menu);

	void verifyForm(SysMenuEntity menu);
}
