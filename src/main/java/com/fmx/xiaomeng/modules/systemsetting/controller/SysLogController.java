
package com.fmx.xiaomeng.modules.systemsetting.controller;

import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.systemsetting.param.SysLogPageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Description 后台管理员操作日志
 * @Date 2025-12-20
 * @Author honghu
 **/
@Deprecated
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 列表
	 * @param key
	 * @param pageNum
	 * @return
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam("key") String key,
					   @RequestParam("pageNum") Integer pageNum,
					   @RequestParam(name = "pageSize") Integer pageSize){

		SysLogPageQueryParam param = new SysLogPageQueryParam();
		param.setPageParam(new PageParam(pageNum, pageSize));
		PageList page = sysLogService.queryPage(param);

		return Result.ok(page);
	}

}
