package com.fmx.xiaomeng.modules.admin.controller;


import com.fmx.xiaomeng.common.constant.AdminGlobalConstants;
import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.admin.service.AppUserAdminService;
import com.fmx.xiaomeng.modules.application.controller.request.UserRequestDTO;
import com.fmx.xiaomeng.modules.application.controller.response.UserVO;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/user")
public class AppUserAdminAdminController extends AbstractAdminController {
    @Autowired
    private AppUserAdminService appUserAdminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("admin:user:list")
    public Result<PageList<UserVO>> list(@RequestParam UserPageQueryParam params) {
        AdminUserModel adminUser = getUser();

        PageList<UserModel> userModelPageList;
        if (!adminUser.getUserId().equals(AdminGlobalConstants.SUPER_ADMIN)) {
            params.setSchoolId(adminUser.getSchoolId());
            userModelPageList = appUserAdminService.pageQuery(params);
            List<UserVO> userVOList = Optional.ofNullable(userModelPageList.getDataList()).orElse(Collections.emptyList())
                    .stream().map(model -> {
                        model.setPhone(null);
                        model.setOpenId(null);
                        model.setCertification(null);
                        model.setStudentId(null);
                        return voConverter.convert(model);
                    }).collect(Collectors.toList());
            return Result.ok(new PageList<>(userVOList, userModelPageList.getPaginator()));
        } else {
            userModelPageList = appUserAdminService.pageQuery(params);
            List<UserVO> userVOList = Optional.ofNullable(userModelPageList.getDataList()).orElse(Collections.emptyList())
                    .stream().map(model -> voConverter.convert(model)).collect(Collectors.toList());
            return Result.ok(new PageList<>(userVOList, userModelPageList.getPaginator()));
        }

    }


    /**
     * 用户信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/info")
    @RequiresPermissions("admin:user:info")
    public Result<UserVO> info(@RequestParam("uid") Long uid) {
        Long userId = getUser().getUserId();
        UserModel targetUser = userService.getUserInfo(uid);
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            targetUser.setPhone(null);
            targetUser.setCertification(null);
        }
        return Result.ok(voConverter.convert(targetUser));
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @return
     */
    @SysLog("修改用户信息")
    @PostMapping("/update")
    @RequiresPermissions("admin:user:update")
    public Result<Void> update(@RequestBody UserRequestDTO request) {
        Long userId = getUser().getUserId();
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("禁止修改用户信息");
        }
        return Result.ok();
    }

    @SysLog("删除用户")
    @GetMapping("/delete")
    @RequiresPermissions("admin:user:delete")
    public Result<Void> delete(@RequestBody Long[] uids) {
        Long userId = getUser().getUserId();
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("禁止删除用户信息");
        }
        //不给管理员开放
        appUserAdminService.removeByIds(Arrays.asList(uids));
        return Result.ok();
    }

    /**
     * 禁言用户
     *
     * @param id
     * @return
     */
    @SysLog("禁言用户")
    @GetMapping("/ban")
    @RequiresPermissions("admin:user:ban")
    public Result ban(@RequestParam("id") Long id) {
        Long userId = getUser().getUserId();
        if (!userId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("禁止禁言用户");
        }
        appUserAdminService.ban(id);

        return Result.ok();
    }

    @SysLog("用户解除禁用")
    @GetMapping("/openBan")
    @RequiresPermissions("admin:user:openBan")
    public Result openBan(@RequestParam("id") Long id) {
        appUserAdminService.openBan(id);

        return Result.ok();
    }

    @GetMapping("/listMockUser")
    @RequiresPermissions("admin:user:listMockUser")
    public Result<List<UserModel>> listMockUser() {
        List<UserModel> mockUserList = appUserAdminService.listMockUser();
        return Result.ok(mockUserList);
    }



    @SysLog("设置用户蓝V状态")
    @PostMapping("/setBlueV")
    @RequiresPermissions("admin:user:update")
    public Result<Void> setBlueV(@RequestParam("userId") Long userId,
                                 @RequestParam("blueV") Boolean blueV) {
        Long adminUserId = getUser().getUserId();
        if (!adminUserId.equals(AdminGlobalConstants.SUPER_ADMIN)) {
            return Result.error("禁止修改用户蓝V状态");
        }
        userService.setBlueV(userId, blueV);
        return Result.ok();
    }

}
