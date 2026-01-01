
package com.fmx.xiaomeng.modules.systemsetting.oauth2;

import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserTokenDO;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * 认证
 *
 * @author honghu
 * @date 2025-12-20
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AdminUserModel user = (AdminUserModel)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);
//        Set<String> roleNameSet = shiroService.getRoleNamesByUserId(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        设置了roles有啥用
//        info.setRoles(roleNameSet);
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        AdminUserTokenDO adminUserTokenDO = shiroService.queryByToken(accessToken);
        //token失效
        if(adminUserTokenDO == null || adminUserTokenDO.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        AdminUserModel user = shiroService.queryUser(adminUserTokenDO.getUserId());
        //账号锁定
        if(Objects.equals(user.getStatus(), 0)){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }
}
