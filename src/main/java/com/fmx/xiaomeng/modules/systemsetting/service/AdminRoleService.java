
package com.fmx.xiaomeng.modules.systemsetting.service;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminRoleModel;
import com.fmx.xiaomeng.modules.systemsetting.service.param.AdminRolePageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgRolePageQueryParam;

import java.util.List;


/**
 * 角色
 *
 */
public interface AdminRoleService {

	PageList<AdminRoleModel> pageQuery(AdminRolePageQueryParam params);

	void saveRole(AdminRoleModel role);

	void update(AdminRoleModel role);

	void deleteBatch(Long[] roleIds);


	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);

	AdminRoleModel queryRoleInfo(Long roleId);

	List<AdminRoleModel> listByParam(BgRolePageQueryParam param);
}
