
package com.fmx.xiaomeng.modules.systemsetting.controller;


import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @Description Controller公共组件
 * @Date 2025-12-20
 * @Author honghu
 **/
@CrossOrigin(origins = "${cors.origins}", allowCredentials = "true")
public abstract class AbstractAdminController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected AdminUserModel getUser() {
        return (AdminUserModel) SecurityUtils.getSubject().getPrincipal();
    }

	protected Long getUserId() {
		return getUser().getUserId();
	}
}
