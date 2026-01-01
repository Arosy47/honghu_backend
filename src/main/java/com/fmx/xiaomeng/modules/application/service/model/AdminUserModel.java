package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class AdminUserModel {
    private Integer id;
    private String username;
    private String password;
    private Date inTime;

    private Integer roleId;
}
