
package com.fmx.xiaomeng.modules.systemsetting.service;

//import com.baomidou.mybatisplus.extension.service.IService;
//import io.linfeng.common.utils.PageUtils;
//import io.linfeng.modules.sys.entity.SysUserEntity;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgAdminUserPageQueryParam;

import java.util.List;


/**
 * 管理员service
 *
 */
public interface AdminUserService {

//	PageList queryPage(Map<String, Object> params);


	PageList<AdminUserModel> pageQuery(BgAdminUserPageQueryParam params);


	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	AdminUserModel queryByUserName(String username);

	/**
	 * 保存用户
	 */
	void saveUser(AdminUserModel user);

	/**
	 * 修改用户
	 */
	void update(AdminUserModel user);

	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userName       用户名称（唯一）
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(String userName, String password, String newPassword);

	AdminUserModel queryByPrimaryKey(Long userId);
}
