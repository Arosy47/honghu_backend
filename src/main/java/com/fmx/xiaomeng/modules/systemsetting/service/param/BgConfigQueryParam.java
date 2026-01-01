package com.fmx.xiaomeng.modules.systemsetting.service.param;

import lombok.Data;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class BgConfigQueryParam {
    /**
     * 配置名称
     */
    Integer schoolId;
    /**
     * 配置名称
     */
    String configName;

    /**
     * 状态
     */
    Boolean status;


    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
    public void setConfigName(String configName) { this.configName = configName; }
    public Integer getSchoolId() { return schoolId; }
    public String getConfigName() { return configName; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

}
