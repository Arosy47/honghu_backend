package com.fmx.xiaomeng.modules.application.controller.request;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import java.util.List;

@Data
public class AuthenticateDTO {
    //短信验证码
    String otpCode;

    //手机号
    String phone;

//    使用小程序自己的获取手机号方式，那手机号认证和证书认证就是单独两个接口，这里只传证书链接就行了

    //证书链接
    List<OssFileModel> certification;
}
