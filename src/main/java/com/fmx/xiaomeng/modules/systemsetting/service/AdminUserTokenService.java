
package com.fmx.xiaomeng.modules.systemsetting.service;

import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserTokenModel;

/**
 * 用户Token
 *
 */
public interface AdminUserTokenService {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	AdminUserTokenModel createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
