
package com.fmx.xiaomeng.modules.systemsetting.controller;

import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.validator.ValidationResult;
import com.fmx.xiaomeng.common.validator.ValidatorImpl;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminRoleModel;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleMenuService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminRoleService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.AdminRolePageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgRolePageQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description 后台管理员角色管理模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/sys/role")
public class AdminRoleAdminController extends AbstractAdminController {
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private AdminRoleMenuService adminRoleMenuService;

    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private Converter converter;

	/**
	 * 角色列表
	 * @param roleName
	 * @param pageNum
	 * @return
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Result<PageList<AdminRoleModel>> list(@RequestParam("roleName") String roleName,
												 @RequestParam("pageNum") Integer pageNum,
												 @RequestParam("pageSize") Integer pageSize){
		AdminRolePageQueryParam param = new AdminRolePageQueryParam();
		if(StringUtils.isNotBlank(roleName)){
			param.setRoleName(roleName);
		}
		param.setPageParam(new PageParam(pageNum, pageSize));

		//如果不是超级管理员，则只查询自己创建的角色列表
		if(getUserId() != AdminGlobalConstants.SUPER_ADMIN){
			param.setCreateUserId(getUserId());
		}
		PageList<AdminRoleModel> adminRoleModelList = adminRoleService.pageQuery(param);

		return Result.ok(adminRoleModelList);
	}


	/**
	 * 角色查询
	 * @return
	 */
//	@ApiOperation("角色查询")
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Result select(){
		BgRolePageQueryParam param = new BgRolePageQueryParam();

		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(getUserId() != AdminGlobalConstants.SUPER_ADMIN){
            param.setCreateUserId(getUserId());
		}
		List<AdminRoleModel> list = adminRoleService.listByParam(param);

		return Result.ok(list);
	}

	/**
	 * 角色信息
	 * @param roleId
	 * @return
	 */
	@GetMapping("/info")
	@RequiresPermissions("sys:role:info")
	public Result info(@RequestParam("roleId") Long roleId){
		AdminRoleModel role = adminRoleService.queryRoleInfo(roleId);

		//查询角色对应的菜单
		List<Long> menuIdList = adminRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		return Result.ok(role);
	}

	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody AdminRoleModel role){
//		ValidatorUtils.validateEntity(role);

        ValidationResult result = validator.validate(role);
        if (result.isHasErrors()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        role.setCreateUserId(getUserId());
		adminRoleService.saveRole(role);

		return Result.ok();
	}

	/**
	 * 修改角色
	 * @param role
	 * @return
	 */
	@SysLog("修改角色")
	@PostMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody AdminRoleModel role){
//		ValidatorUtils.validateEntity(role);
        ValidationResult result = validator.validate(role);
        if (result.isHasErrors()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
		role.setCreateUserId(getUserId());
		adminRoleService.update(role);

		return Result.ok();
	}

	/**
	 * 删除角色
	 * @param roleIds
	 * @return
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody Long[] roleIds){
		adminRoleService.deleteBatch(roleIds);

		return Result.ok();
	}
}
