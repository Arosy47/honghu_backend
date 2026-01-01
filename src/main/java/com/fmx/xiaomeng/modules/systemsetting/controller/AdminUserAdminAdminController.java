
package com.fmx.xiaomeng.modules.systemsetting.controller;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.form.PasswordForm;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgAdminUserPageQueryParam;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 后台管理员 管理
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/sys/user")
public class AdminUserAdminAdminController extends AbstractAdminController {
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserRoleService adminUserRoleService;

	@Autowired
	private ValidatorImpl validator;


	/**
	 * 所有管理员列表
	 * @param userName
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Result<PageList<AdminUserModel>> list(
			@RequestParam("userName") String userName,
			@RequestParam("pageNum") Integer pageNum,
			@RequestParam("pageSize") Integer pageSize){
		BgAdminUserPageQueryParam pageQueryParam = new BgAdminUserPageQueryParam();
		if(StringUtils.isNotBlank(userName)){
			pageQueryParam.setUserName(userName);
		}
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != AdminGlobalConstants.SUPER_ADMIN){
			pageQueryParam.setCreateUserId(getUserId());
		}
		pageQueryParam.setPageParam(new PageParam(pageNum, pageSize));
		PageList<AdminUserModel> listPageList = adminUserService.pageQuery(pageQueryParam);

		return Result.ok(listPageList);
	}

	/**
	 * 管理员获取个人信息
	 * @return
	 */
	@GetMapping("/privateInfo")
	public Result info(){
		AdminUserModel user = getUser();
		List<Long> roleIdList = adminUserRoleService.queryRoleIdList(user.getUserId());
		user.setRoleIdList(roleIdList);
		return Result.ok(user);
	}

	/**
	 * 修改管理员登录密码
	 * @param form
	 * @return
	 */
	@PostMapping("/password")
	@RequiresPermissions("sys:user:update")
	public Result password(@RequestBody PasswordForm form){
		if(StringUtils.isBlank(form.getNewPassword())){
			throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, "新密码不能为空");
		}
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		//更新密码
		boolean flag = adminUserService.updatePassword(getUser().getUserName(), password, newPassword);

		return Result.ok();
	}

	/**
	 * 管理员查看自己设置的其他管理员信息
	 * @param adminId
	 * @return
	 */
	@GetMapping("/info")
	@RequiresPermissions("sys:user:info")
	public Result info(@RequestParam("adminId") Long adminId){
		AdminUserModel user = adminUserService.queryByPrimaryKey(adminId);

//		//获取用户所属的角色列表
//		List<Long> roleIdList = adminUserRoleService.queryRoleIdList(adminId);
//		user.setRoleIdList(roleIdList);

		return Result.ok(user);
	}

	/**
	 * 添加管理员
	 * @param user
	 * @return
	 */
	@SysLog("添加管理员")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody AdminUserModel user){
//		ValidatorUtils.validateEntity(user, AddGroup.class);

		ValidationResult result = validator.validate(user);
		if (result.isHasErrors()) {
			throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
		}

		user.setCreateUserId(getUserId());
		adminUserService.saveUser(user);

		return Result.ok();
	}

	/**
	 * 修改管理员信息
	 * @param user
	 * @return
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody AdminUserModel user){
//		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		ValidationResult result = validator.validate(user);
		if (result.isHasErrors()) {
			throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
		}

		user.setCreateUserId(getUserId());
		adminUserService.update(user);

		return Result.ok();
	}

	/**
	 * 删除管理员
	 * @param userIds
	 * @return
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return Result.error("系统管理员不能删除");
		}

		if(ArrayUtils.contains(userIds, getUserId())){
			return Result.error("当前用户不能删除");
		}

		adminUserService.deleteBatch(userIds);

		return Result.ok();
	}
}
