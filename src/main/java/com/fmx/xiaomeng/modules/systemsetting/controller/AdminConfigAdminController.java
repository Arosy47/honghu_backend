
package com.fmx.xiaomeng.modules.systemsetting.controller;


import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.BgConfigModel;
import com.fmx.xiaomeng.modules.systemsetting.service.BgConfigService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgConfigPageQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 配置管理模块
 * @Date 2025-12-20
 * @Author honghu
 **/
//@Api(tags = "系统配置信息")
@RestController
@RequestMapping("/sys/config")
public class AdminConfigAdminController extends AbstractAdminController {
	@Autowired
	private BgConfigService bgConfigService;

	/**
	 * 所有配置列表
	 */
//	@ApiOperation("所有配置列表")
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	public Result<PageList<BgConfigModel>> list(
			@RequestParam(name = "schoolId") Integer schoolId,
			@RequestParam(name = "status") String status,
			@RequestParam(name = "configName") String configName,
			@RequestParam(name = "pageNum") Integer pageNum){
		BgConfigPageQueryParam param = new BgConfigPageQueryParam();
		if(StringUtils.isNotBlank(configName)){
			param.setConfigName(configName);
		}
		if(StringUtils.isNotBlank(status)){
			param.setStatus(Boolean.valueOf(status));
		}

		AdminUserModel user = getUser();
		Integer searchSchoolId = schoolId;
		if (!user.getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
			//            非超级管理员只能看自己学校的帖子
			searchSchoolId = user.getSchoolId();
		}
		param.setSchoolId(searchSchoolId);

		param.setPageParam(new PageParam(pageNum));
		PageList<BgConfigModel> page = bgConfigService.queryPage(param);
		return Result.ok(page);
	}


	/**
	 * 配置信息
	 */
//	@ApiOperation("根据ID配置信息")
	@GetMapping("/info")
	@RequiresPermissions("sys:config:info")
	public Result<BgConfigModel> info(@RequestParam("id") Integer id){
		BgConfigModel config = bgConfigService.queryByPrimaryKey(id);

		return Result.ok(config);
	}

	/**
	 * 保存配置
	 */
//	@ApiOperation("保存配置")
	@SysLog("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:save")
	public Result<Void> save(@RequestBody BgConfigModel config){
//		ValidatorUtils.validateEntity(config);

		bgConfigService.saveConfig(config);

		return Result.ok();
	}

	/**
	 * 修改配置
	 */
//	@ApiOperation("修改配置")
	@SysLog("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	public Result update(@RequestBody BgConfigModel config){
//		ValidatorUtils.validateEntity(config);

		bgConfigService.update(config);

		return Result.ok();
	}

	/**
	 * 删除配置
	 */
//	@ApiOperation("删除配置")
	@SysLog("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public Result delete(@RequestBody List<Integer> ids){
		bgConfigService.deleteBatch(ids);

		return Result.ok();
	}

}
