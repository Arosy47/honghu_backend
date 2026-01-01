package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class UserBlackModel {
    private Integer id;
    private Long userId;
    private Long blackedUserId;
    private Date createTime;
    private Date modifiedTime;
    private Boolean valid;
}
