package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class AdminMenuModel {
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL（这个是指前端代码文件中的位置，比如admin/article就是指在admin文件夹中的article.VUE文件）
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * ztree属性
     */
    private Boolean open;

    private List<AdminMenuModel> list=new ArrayList<>();

    public Long getMenuId() { return menuId; }
    public Long getParentId() { return parentId; }
    public java.util.List<AdminMenuModel> getList() { return list; }
    public String getPerms() { return perms; }

    public String getName() { return name; }

    public void setParentName(String parentName) { this.parentName = parentName; }
}
