
package com.fmx.xiaomeng.modules.systemsetting.service.impl;


import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminMenuDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminMenuDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminMenuModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysMenuEntity;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminMenuService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleMenuService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgMenuQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
/**
 * @author honghu
 * @date 2025-12-20
 */
public class AdminMenuServiceImpl implements AdminMenuService {
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminRoleMenuService adminRoleMenuService;

	@Autowired
	private AdminMenuDOMapper adminMenuDAO;

	@Autowired
	private Converter converter;

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		return null;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return null;
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return null;
	}

	@Override
	public List<AdminMenuModel> getUserMenuList(Long userId) {
		if(userId == AdminGlobalConstants.SUPER_ADMIN){
			return getMenuList(null);
		}

		List<Long> menuIdList = adminUserService.queryAllMenuId(userId);
		return getMenuList(menuIdList);
	}

	/**
	 * 获取拥有的菜单列表
	 * @param menuIdList
	 * @return
	 */
	private List<AdminMenuModel> getMenuList(List<Long> menuIdList) {

		BgMenuQueryParam param = new BgMenuQueryParam();
		param.setMenuIdList(menuIdList);
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(0);
		typeList.add(1);
		param.setTypeList(typeList);

		List<AdminMenuDO> adminMenuDOList = adminMenuDAO.queryByParam(param);
		List<AdminMenuModel> adminMenuModelList = converter.convertAdminMenuList(adminMenuDOList);


		HashMap<Long, AdminMenuModel> menuMap = new HashMap<>(12);
		for (AdminMenuModel s : adminMenuModelList) {
			menuMap.put(s.getMenuId(), s);
		}
		Iterator<AdminMenuModel> iterator = adminMenuModelList.iterator();
		while (iterator.hasNext()) {
			AdminMenuModel menu = iterator.next();
			AdminMenuModel parent = menuMap.get(menu.getParentId());
			if (Objects.nonNull(parent)) {
				parent.getList().add(menu);
				iterator.remove();
			}
		}

		return adminMenuModelList;
	}

	@Override
	public void delete(Long menuId) {

	}
	@Override
	public List<AdminMenuModel> list() {
		List<AdminMenuDO> adminMenuDOList = adminMenuDAO.list();
		return converter.convertAdminMenuList(adminMenuDOList);
	}

	@Override
	public SysMenuEntity queryByPrimaryKey(Long menuId) {
		return null;
	}

	@Override
	public void save(SysMenuEntity menu) {

	}

	@Override
	public void updateById(SysMenuEntity menu) {

	}

	/**
	 * 验证参数是否正确
	 * @param menu
	 */
	@Override
	public void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "菜单名称不能为空");
		}

		if(menu.getParentId() == null){
			throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == AdminGlobalConstants.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = AdminGlobalConstants.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = this.queryByPrimaryKey(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == AdminGlobalConstants.MenuType.CATALOG.getValue() ||
				menu.getType() == AdminGlobalConstants.MenuType.MENU.getValue()){
			if(parentType != AdminGlobalConstants.MenuType.CATALOG.getValue()){
				throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮
		if(menu.getType() == AdminGlobalConstants.MenuType.BUTTON.getValue()){
			if(parentType != AdminGlobalConstants.MenuType.MENU.getValue()){
				throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "上级菜单只能为菜单类型");
			}
		}
	}



//
//	@Override
//	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
//		List<SysMenuEntity> menuList = queryListParentId(parentId);
//		if(menuIdList == null){
//			return menuList;
//		}
//
//		List<SysMenuEntity> userMenuList = new ArrayList<>();
//		for(SysMenuEntity menu : menuList){
//			if(menuIdList.contains(menu.getMenuId())){
//				userMenuList.add(menu);
//			}
//		}
//		return userMenuList;
//	}
//
//	@Override
//	public List<SysMenuEntity> queryListParentId(Long parentId) {
//		return baseMapper.queryListParentId(parentId);
//	}
//
//	@Override
//	public List<SysMenuEntity> queryNotButtonList() {
//		return baseMapper.queryNotButtonList();
//	}
//
//	@Override
//	public List<SysMenuEntity> getUserMenuList(Long userId) {
//		//系统管理员，拥有最高权限
//		if(userId == Constant.SUPER_ADMIN){
//			return getMenuList(null);
//		}
//
//		//用户菜单列表
//		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
//		return getMenuList(menuIdList);
//	}
//
//	/**
//	 * 获取拥有的菜单列表
//	 * @param menuIdList
//	 * @return
//	 */
//	private List<SysMenuEntity> getMenuList(List<Long> menuIdList) {
//		// 查询拥有的所有菜单
//		List<SysMenuEntity> menus = this.baseMapper.selectList(new QueryWrapper<SysMenuEntity>()
//				.in(Objects.nonNull(menuIdList), "menu_id", menuIdList).in("type", 0, 1));
//		// 将id和菜单绑定
//		HashMap<Long, SysMenuEntity> menuMap = new HashMap<>(12);
//		for (SysMenuEntity s : menus) {
//			menuMap.put(s.getMenuId(), s);
//		}
//		// 使用迭代器,组装菜单的层级关系
//		Iterator<SysMenuEntity> iterator = menus.iterator();
//		while (iterator.hasNext()) {
//			SysMenuEntity menu = iterator.next();
//			SysMenuEntity parent = menuMap.get(menu.getParentId());
//			if (Objects.nonNull(parent)) {
//				parent.getList().add(menu);
//				// 将这个菜单从当前节点移除
//				iterator.remove();
//			}
//		}
//
//		return menus;
//	}
//
//	@Override
//	public void delete(Long menuId){
//		//删除菜单
//		this.removeById(menuId);
//		//删除菜单与角色关联
//		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
//	}
//
//	/**
//	 * 获取所有菜单列表
//	 */
//	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
//		//查询根菜单列表
//		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
//		//递归获取子菜单
//		getMenuTreeList(menuList, menuIdList);
//
//		return menuList;
//	}
//
//	/**
//	 * 递归
//	 */
//	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
//		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
//
//		for(SysMenuEntity entity : menuList){
//			//目录
//			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
//				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
//			}
//			subMenuList.add(entity);
//		}
//
//		return subMenuList;
//	}
}
