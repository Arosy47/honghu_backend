package com.fmx.xiaomeng.modules.application.service.model.configmodel;

import lombok.Data;

import java.util.List;

/**
 * @Description 小程序端展示信息配置，应对小程序审核问题，以及新功能
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class NavListConfig {

   private List<NavItem> navItem;
}
