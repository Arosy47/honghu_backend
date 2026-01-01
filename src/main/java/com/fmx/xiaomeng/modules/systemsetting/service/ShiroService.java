
package com.fmx.xiaomeng.modules.systemsetting.service;

import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserTokenDO;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;

import java.util.Set;

/**
 * shiro相关接口
 *
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    AdminUserTokenDO queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    AdminUserModel queryUser(Long userId);
}
