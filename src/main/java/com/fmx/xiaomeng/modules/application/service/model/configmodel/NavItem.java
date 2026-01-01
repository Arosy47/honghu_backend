package com.fmx.xiaomeng.modules.application.service.model.configmodel;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class NavItem {

    /**
     * 只允许本圈人使用
     */
    private Boolean circleMemberOnly;

    /**
     * 描述
     */
    private String description;

    private String title;

    private String iconUrl;

//    是小程序页面:page(普通页面和webview都属于小程序页面)，还是小程序:miniprogram
    private String targetType;

//    可能是页面，也可能是其他小程序（如果是页面，可以加参数，比如url）
// 例："path": "/page-webview/webview?mode=ad&adId=***",
//     "path": "/page-webview/realwebview?url=https....",
    private String path;

    private String bgColor;
    private String borderColor;

}
