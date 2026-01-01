package com.fmx.xiaomeng.modules.admin.controller;


import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.AuthStatusEnum;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.admin.request.UserPageQueryParam;
import com.fmx.xiaomeng.modules.admin.service.AppUserAdminService;
import com.fmx.xiaomeng.modules.application.controller.response.UserVO;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.systemsetting.annotation.SysLog;
import com.fmx.xiaomeng.modules.systemsetting.controller.AbstractAdminController;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @Description 后台用户管理模块
 * @Date 2023/3/4 23:28
 * @Author honghu
 **/
@RestController
@RequestMapping("admin/certification")
public class AppUserCertificationAdminAdminController extends AbstractAdminController {
    @Autowired
    private AppUserAdminService appUserAdminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;

    @GetMapping("/list")
    @RequiresPermissions("admin:user:list")
    public Result<PageList<UserVO>> list(@RequestParam(name = "authStatus") String authStatus,
                                         @RequestParam(name = "schoolId") Integer schoolId,
                                         @RequestParam(name = "pageNum") Integer pageNum,
                                         @RequestParam(name = "pageSize") Integer pageSize){
        AdminUserModel adminUser = getUser();

        PageList<UserModel> userModelPageList;

        UserPageQueryParam param = new UserPageQueryParam();
        if (StringUtils.isNotBlank(authStatus)) {
            param.setAuthStatus(AuthStatusEnum.valueOf(authStatus).getCode());
        }
        if (Objects.nonNull(adminUser.getSchoolId())) {
            param.setSchoolId(adminUser.getSchoolId());
        } else if (Objects.nonNull(schoolId)) {
            param.setSchoolId(schoolId);
        }


        param.setPageParam(new PageParam(pageNum, pageSize));
        userModelPageList = appUserAdminService.pageQuery(param);
        List<UserVO> userVOList = Optional.ofNullable(userModelPageList.getDataList()).orElse(Collections.emptyList())
                .stream().map(model -> voConverter.sensitiveConvert(model)).collect(Collectors.toList());
        return Result.ok(new PageList<>(userVOList, userModelPageList.getPaginator()));
    }


    @SysLog("认证审核通过")
    @GetMapping("/pass")
    @RequiresPermissions("admin:certification:pass")
    public Result<Void> pass(@RequestParam("userId") Long targetUserId){
        appUserAdminService.certificationPass(targetUserId);
        return Result.ok();
    }


    @SysLog("认证审核拒绝")
    @GetMapping("/unpass")
    @RequiresPermissions("admin:certification:pass")
    public Result<Void> unpass(@RequestParam("userId") Long targetUserId){
        appUserAdminService.certificationUnpass(targetUserId);
        return Result.ok();
    }
}
