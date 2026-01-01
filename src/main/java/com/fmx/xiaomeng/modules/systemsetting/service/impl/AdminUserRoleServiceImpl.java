
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserRoleDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserRoleDO;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysUserRoleEntity;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author honghu
 * @date 2025-12-20
 */
@Service("sysUserRoleService")
public class AdminUserRoleServiceImpl implements AdminUserRoleService {

	@Autowired
	AdminUserRoleDOMapper adminUserRoleDAO;
	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		adminUserRoleDAO.deleteByUserId(userId);

		if(roleIdList == null || roleIdList.size() == 0){
			return ;
		}

		//保存用户与角色关系
		for(Long roleId : roleIdList){
			SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
			sysUserRoleEntity.setUserId(userId);
			sysUserRoleEntity.setRoleId(roleId);

			AdminUserRoleDO adminUserRoleDO = new AdminUserRoleDO();
			BeanUtils.copyProperties(sysUserRoleEntity, adminUserRoleDO);
			adminUserRoleDAO.insertSelective(adminUserRoleDO);
		}
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return adminUserRoleDAO.queryRoleByUserId(userId);
	}

	/**
	 * 根据角色id删除用户和角色的关联
	 * @param roleIds
	 * @return
	 */
	@Override
	public int deleteBatch(Long[] roleIds){
		return adminUserRoleDAO.batchDeleteByRoleIdList(roleIds);
	}
}
