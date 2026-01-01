package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class NavModel {
    List<AdminMenuModel> menuList;

    Set<String> permissions;


    public void setMenuList(java.util.List<AdminMenuModel> menuList) { this.menuList = menuList; }
    public void setPermissions(java.util.Set<String> permissions) { this.permissions = permissions; }

}
