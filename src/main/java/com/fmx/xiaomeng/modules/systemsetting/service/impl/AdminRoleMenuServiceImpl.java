
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 角色与菜单对应关系
 *
 * @author honghu
 * @date 2025-12-20
 */
@Service
public class AdminRoleMenuServiceImpl implements AdminRoleMenuService {
	@Override
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {

	}

	@Override
	public List<Long> queryMenuIdList(Long roleId) {
		return null;
	}

	@Override
	public int deleteBatch(Long[] roleIds) {
		return 0;
	}

}
