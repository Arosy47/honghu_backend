
package com.fmx.xiaomeng.modules.systemsetting.service;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.systemsetting.entity.BgConfigModel;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgConfigPageQueryParam;

import java.util.List;

/**
 * 管理配置信息服务
 *
 */
public interface BgConfigService {

	BgConfigModel queryByPrimaryKey(Integer id);

	PageList<BgConfigModel> queryPage(BgConfigPageQueryParam param);

	/**
	 * 保存配置信息
	 */
	void saveConfig(BgConfigModel config);

	/**
	 * 更新配置信息
	 */
	void update(BgConfigModel config);

	/**
	 * 删除配置信息
	 */
	void deleteBatch(List<Integer> ids);

	/**
	 * 根据key，获取配置的value值
	 *
	 * @param configName           configName
	 */
	String getValue(Integer schoolId, String configName);


	/**
	 * 根据key，获取配置的value值
	 *
	 * @param configName           configName
	 */
	String getValueNoSchool(String configName);

	/**
	 * 根据key，获取value的Object对象
	 * @param key    key
	 * @param clazz  Object对象
	 */
	<T> T getConfigObject(Integer schoolId, String key, Class<T> clazz);

}
