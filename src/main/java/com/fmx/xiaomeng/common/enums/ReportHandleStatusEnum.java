package com.fmx.xiaomeng.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Getter
// @AllArgsConstructor
public enum ReportHandleStatusEnum {
    /**
     * 待处理
     */
    WAITING(1),

    /**
     * 处理完成
     */
    COMPLETED(2),

    ;

    private Integer code;

//
//    private static Map<Integer, ReportHandleStatusEnum> zyMap = new HashMap<>();
//
//    static {
//        for (ReportHandleStatusEnum value : ReportHandleStatusEnum.values()) {
//            zyMap.put(value.getCode(), value);
//        }
//    }
//
//
//    public static ReportHandleStatusEnum getByCode(Integer code) {
//        return zyMap.get(code);
//    }

    ReportHandleStatusEnum(Integer code) { this.code = code; }
    public Integer getCode() { return code; }
}
