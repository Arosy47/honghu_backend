
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgAdminUserPageQueryParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.ORIGIN_PASSWORD_WRONG;


/**
 * @author honghu
 * @date 2025-12-20
 */
@Service("sysUserService")
public class AdminUserServiceImpl implements AdminUserService {
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	@Autowired
	private AdminRoleService adminRoleService;

	@Autowired
	private AdminUserDOMapper adminUserDOMapper;

	@Autowired
	private Converter converter;

	@Override
	public PageList<AdminUserModel> pageQuery(BgAdminUserPageQueryParam param) {

		long total = adminUserDOMapper.countByParam(param);
		List<AdminUserModel> adminUserModelList = null;
		PageParam pageParam = param.getPageParam();
		if (total > pageParam.getOffset()) {
			List<AdminUserDO> adminUserDOList = adminUserDOMapper.pageQuery(param);
			adminUserModelList = converter.convertToAdminUserList(adminUserDOList);
		}
		return new PageList<>(adminUserModelList, new Paginator(pageParam, total));
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return adminUserDOMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return adminUserDOMapper.queryAllMenuId(userId);
	}

	@Override
	public AdminUserModel queryByUserName(String username) {
		return converter.convert(adminUserDOMapper.queryByUserName(username));
	}

	@Override
	@Transactional
	public void saveUser(AdminUserModel user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);

		AdminUserDO adminUserDO = new AdminUserDO();
		BeanUtils.copyProperties(user, adminUserDO);
		adminUserDOMapper.insertSelective(adminUserDO);
//		this.save(user);

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		adminUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(AdminUserModel user) {
		//检查角色是否越权
		checkRole(user);

		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		AdminUserDO adminUserDO = new AdminUserDO();
		BeanUtils.copyProperties(user, adminUserDO);
		adminUserDOMapper.updateByPrimaryKeySelective(adminUserDO);


		//保存用户与角色关系
		adminUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
//		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	@Deprecated
	public boolean updatePassword(String userName, String password, String newPassword) {
		AdminUserDO existModel = adminUserDOMapper.queryByUserName(userName);
		if(password.equals(existModel.getPassword())){
			AdminUserDO updateModel = new AdminUserDO();
			updateModel.setUserId(existModel.getUserId());
			updateModel.setUserName(existModel.getUserName());
			updateModel.setPassword(newPassword);
//			updateModel.setSalt(existModel.getSalt());
			adminUserDOMapper.updateByPrimaryKeySelective(updateModel);
			return true;
		}else {
			throw new BusinessException(ORIGIN_PASSWORD_WRONG, "原密码错误, 请重新输入");
		}

	}

	@Override
	public AdminUserModel queryByPrimaryKey(Long userId) {
		AdminUserDO adminUserDO = adminUserDOMapper.selectByPrimaryKey(userId);
		List<Long> roleIdList = adminUserRoleService.queryRoleIdList(userId);
		AdminUserModel adminUserModel = new AdminUserModel();
		BeanUtils.copyProperties(adminUserDO, adminUserModel);
		adminUserModel.setRoleIdList(roleIdList);
		return adminUserModel;
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(AdminUserModel user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == AdminGlobalConstants.SUPER_ADMIN){
			return ;
		}

		//查询用户创建的角色列表
		List<Long> roleIdList = adminRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "新增用户所选角色，不是本人创建");
		}
	}
}
