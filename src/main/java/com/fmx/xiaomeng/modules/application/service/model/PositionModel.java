package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class PositionModel {
    /**
     * 地址名称
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
}
