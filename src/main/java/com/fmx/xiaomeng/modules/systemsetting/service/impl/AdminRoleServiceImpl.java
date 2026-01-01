package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminRoleDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminRoleDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminRoleModel;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleMenuService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.AdminRolePageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgRolePageQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * @author honghu
 * @date 2025-12-20
 */
@Service("sysRoleService")
public class AdminRoleServiceImpl implements AdminRoleService {
    @Autowired
    private AdminRoleMenuService adminRoleMenuService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminUserRoleService adminUserRoleService;

    @Autowired
    private AdminRoleDOMapper adminRoleDAO;

    @Autowired
    private Converter converter;

    @Override
    public PageList<AdminRoleModel> pageQuery(AdminRolePageQueryParam param) {
        long total = adminRoleDAO.countByParam(param);
        List<AdminRoleModel> bgConfigModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<AdminRoleDO> bgConfigDOList = adminRoleDAO.pageQuery(param);
            bgConfigModelList = converter.convertAdminRoleModelList(bgConfigDOList);
        }
        return new PageList<>(bgConfigModelList, new Paginator(pageParam, total));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(AdminRoleModel role) {
        role.setCreateTime(new Date());

        AdminRoleDO adminRoleDO = new AdminRoleDO();
        BeanUtils.copyProperties(role, adminRoleDO);
        adminRoleDAO.insertSelective(adminRoleDO);

        checkPrems(role);

        adminRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdminRoleModel role) {
        AdminRoleDO adminRoleDO = new AdminRoleDO();
        BeanUtils.copyProperties(role, adminRoleDO);
        adminRoleDAO.updateByPrimaryKeySelective(adminRoleDO);

        checkPrems(role);

        adminRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        adminRoleDAO.deleteByRoleIds(Arrays.asList(roleIds));

        adminRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        adminUserRoleService.deleteBatch(roleIds);
    }


    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return adminRoleDAO.queryRoleIdList(createUserId);
    }

    @Override
    public AdminRoleModel queryRoleInfo(Long roleId) {
        return null;
    }

    @Override
    public List<AdminRoleModel> listByParam(BgRolePageQueryParam param) {
        return null;
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(AdminRoleModel role) {
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if (role.getCreateUserId() == AdminGlobalConstants.SUPER_ADMIN) {
            return;
        }

        //查询用户所拥有的菜单列表
        List<Long> menuIdList = adminUserService.queryAllMenuId(role.getCreateUserId());

        //判断是否越权
        if (!menuIdList.containsAll(role.getMenuIdList())) {
            throw new BusinessException(ErrorCodeEnum.SIGNATURE_NOT_ALLOW, "新增角色的权限，已超出你的权限范围");
        }
    }
}
