
package com.fmx.xiaomeng.modules.systemsetting.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 后台配置信息
 *
 */
@Data
//@TableName("sys_config")
public class BgConfigModel {
//	@TableId
	private Long id;


	private Integer schoolId;

	@NotBlank(message="配置名不能为空")
	private String configName;
	@NotBlank(message="配置值不能为空")
	private String configValue;

	/**
	 * 1:启用，0废弃
	 */
	private Boolean status;
	/**
	 * 备注
	 */
	private String remark;


    public Integer getSchoolId() { return schoolId; }
    public String getConfigName() { return configName; }


    public Boolean getStatus() { return status; }
    public String getConfigValue() { return configValue; }

}
