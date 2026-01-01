package com.fmx.xiaomeng.modules.admin.controller;


import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/statistic")
public class StatisticAdminController extends AbstractAdminController {

    @Autowired
    private AdminUserRoleService adminUserRoleService;
    /**
     * 首页
     * @return
     */
    @GetMapping("/home")
    public Result index(@RequestParam(name = "schoolId") Integer schoolId) {
        AdminUserModel user = getUser();
        List<Long> roleIdList = adminUserRoleService.queryRoleIdList(user.getUserId());
        if(Objects.nonNull(roleIdList) && (roleIdList.contains(1L) || roleIdList.contains(2L))){
            if(schoolId==-1){
                schoolId=null;
            }
            return Result.ok(userService.indexDate(schoolId));
        }else {
            schoolId = user.getSchoolId();
            return Result.ok(userService.indexDate(schoolId));
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.fmx.xiaomeng.modules.application.service.UserService userService;
}
