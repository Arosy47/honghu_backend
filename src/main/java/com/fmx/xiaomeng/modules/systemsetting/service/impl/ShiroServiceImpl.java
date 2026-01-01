
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminMenuDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserRoleDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserTokenDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminMenuDO;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserDO;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserRoleDO;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserTokenDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminMenuModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.ShiroService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgMenuQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
/**
 * @author honghu
 * @date 2025-12-20
 */
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private AdminMenuDOMapper adminMenuDAO;
    @Autowired
    private AdminUserDOMapper adminUserDAO;

    @Autowired
    private AdminUserTokenDOMapper adminUserTokenDAO;
    @Autowired
    private AdminUserRoleService adminUserRoleService;
    @Autowired
    private Converter converter;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        if(userId == AdminGlobalConstants.SUPER_ADMIN){
            List<AdminMenuDO> menuList = adminMenuDAO.queryByParam(new BgMenuQueryParam());

            List<AdminMenuModel> adminMenuModelList = converter.convertAdminMenuList(menuList);

            permsList = new ArrayList<>(menuList.size());
            for(AdminMenuModel menu : adminMenuModelList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = adminUserDAO.queryAllPerms(userId);
        }
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public AdminUserTokenDO queryByToken(String token) {
        return adminUserTokenDAO.queryByToken(token);
    }

    @Override
    public AdminUserModel queryUser(Long userId) {
        AdminUserDO adminUserDO = adminUserDAO.selectByPrimaryKey(userId);
        List<Long> roleIdList = adminUserRoleService.queryRoleIdList(userId);
        AdminUserModel adminUserModel = new AdminUserModel();
        BeanUtils.copyProperties(adminUserDO, adminUserModel);
        adminUserModel.setRoleIdList(roleIdList);
        return adminUserModel;
    }
}
