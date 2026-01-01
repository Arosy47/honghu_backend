package com.fmx.xiaomeng.modules.application.controller;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.controller.request.UserRequestDTO;
import com.fmx.xiaomeng.modules.application.model.WXSessionModel;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import lombok.CustomLog;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/user")

/**
 * @author honghu
 */
public class UserController {

    @Autowired
    private ParamConverter converter;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Transactional
    public Result<WXSessionModel> login(@RequestBody UserRequestDTO userRequestDTO) throws BusinessException {
        UserModel userModel = converter.convert(userRequestDTO);
        WXSessionModel wxSessionModel = userService.login(userRequestDTO.getCode(), userModel);
        return Result.ok(wxSessionModel);
    }



    @NeedLogin
    @GetMapping("/certificatePhone")
    @Transactional
    public Result<String> certificatePhone(@RequestParam(name = "code") String code,
                                           @UserInfo UserModel userModel) {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }

        try {
            String phone = userService.certificatePhone(code, userModel);
            return Result.ok(phone);
        } catch (WxErrorException e) {
            log.error("手机号授权失败，error:{}", e.getError());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", e.getError().getErrorCode());
            responseData.put("errMsg", e.getError().getErrorMsg());
            return Result.error(500, JSON.toJSONString(responseData));
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
}
