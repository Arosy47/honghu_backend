
package com.fmx.xiaomeng.modules.systemsetting.controller;


import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminMenuModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.NavModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysMenuEntity;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminMenuService;
import com.fmx.xiaomeng.modules.systemsetting.service.ShiroService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author honghu
 * @date 2025-12-20
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuAdminController extends AbstractAdminController {
	@Autowired
	private AdminMenuService adminMenuService;
	@Autowired
	private ShiroService shiroService;

    /**
     * 导航菜单
     * @return
     */
	@GetMapping("/nav")
	public Result<NavModel> nav(){
		List<AdminMenuModel> menuList = adminMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
        NavModel navModel = new NavModel();
        navModel.setMenuList(menuList);
        navModel.setPermissions(permissions);
		return Result.ok(navModel);
	}


    /**
     * 所有菜单列表
     * @return
     */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<AdminMenuModel> list(){
		List<AdminMenuModel> menuList = adminMenuService.list();
		HashMap<Long, AdminMenuModel> menuMap = new HashMap<>(12);
		for (AdminMenuModel s : menuList) {
			menuMap.put(s.getMenuId(), s);
		}
		for (AdminMenuModel s : menuList) {
			AdminMenuModel parent = menuMap.get(s.getParentId());
			if (Objects.nonNull(parent)) {
				s.setParentName(parent.getName());
			}

		}


		return menuList;
	}


	/**
	 * 选择菜单(添加、修改菜单)
	 * @return
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public Result<List<SysMenuEntity>> select(){
		List<SysMenuEntity> menuList = adminMenuService.queryNotButtonList();

		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		return Result.ok(menuList);
	}

	/**
	 * 菜单信息
	 * @param menuId
	 * @return
	 */
	@GetMapping("/info")
	@RequiresPermissions("sys:menu:info")
	public Result<SysMenuEntity> info(@RequestParam("menuId") Long menuId){
		SysMenuEntity menu = adminMenuService.queryByPrimaryKey(menuId);
		return Result.ok(menu);
	}

	/**
	 * 保存
	 * @param menu
	 * @return
	 */
	@SysLog("保存菜单")
	@PostMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public Result save(@RequestBody SysMenuEntity menu){
		//数据校验
		adminMenuService.verifyForm(menu);

		adminMenuService.save(menu);

		return Result.ok();
	}

	/**
	 * 修改
	 * @param menu
	 * @return
	 */
	@SysLog("修改菜单")
	@PostMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public Result update(@RequestBody SysMenuEntity menu){
		//数据校验
		adminMenuService.verifyForm(menu);

		adminMenuService.updateById(menu);

		return Result.ok();
	}

	/**
	 * 删除
	 * @param menuId
	 * @return
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public Result<Void> delete(@RequestParam("menuId") long menuId){

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = adminMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return Result.error("请先删除子菜单或按钮");
		}

		adminMenuService.delete(menuId);

		return Result.ok();
	}


}
