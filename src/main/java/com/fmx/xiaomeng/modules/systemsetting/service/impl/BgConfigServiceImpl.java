package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.repository.dao.BgConfigDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.BgConfigDO;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.BgConfigModel;
import com.fmx.xiaomeng.modules.systemsetting.redis.SysConfigRedis;
import com.fmx.xiaomeng.modules.systemsetting.service.BgConfigService;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgConfigPageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.param.BgConfigQueryParam;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

import static com.fmx.xiaomeng.common.error.ErrorCodeEnum.BG_CONFIG_ALREADY_EXIST;

@Service
/**
 * @author honghu
 * @date 2025-12-20
 */
public class BgConfigServiceImpl implements BgConfigService {
    @Autowired
    private SysConfigRedis sysConfigRedis;

    @Autowired
    private BgConfigDOMapper bgConfigDOMapper;

    @Autowired
    private Converter converter;

    @Override
    public BgConfigModel queryByPrimaryKey(Integer id) {
        return converter.convert(bgConfigDOMapper.selectByPrimaryKey(id));
    }

    @Override
    public PageList<BgConfigModel> queryPage(BgConfigPageQueryParam param) {

        long total = bgConfigDOMapper.countByParam(param);
        List<BgConfigModel> bgConfigModelList = null;
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {
            List<BgConfigDO> bgConfigDOList = bgConfigDOMapper.pageQuery(param);
            bgConfigModelList = converter.convertToBgConfigModelList(bgConfigDOList);
        }
        return new PageList<>(bgConfigModelList, new Paginator(pageParam, total));
    }

    @Override
    public void saveConfig(BgConfigModel config) {
        Integer schoolId = config.getSchoolId();
        @NotBlank(message = "配置名不能为空") String configName = config.getConfigName();

        BgConfigModel bgConfigModel = sysConfigRedis.get(schoolId, configName);
        if(Objects.nonNull(bgConfigModel)){
            throw new BusinessException(BG_CONFIG_ALREADY_EXIST, "configName:{}", configName);
        }else {
            BgConfigQueryParam param = new BgConfigQueryParam();
            param.setSchoolId(schoolId);
            param.setConfigName(configName);
            BgConfigDO bgConfigDO = bgConfigDOMapper.queryByParam(param);
            if(Objects.nonNull(bgConfigDO)){
                throw new BusinessException(BG_CONFIG_ALREADY_EXIST, "configName:{}", configName);
            }else {
                bgConfigDOMapper.insertSelective(converter.convert(config));
                sysConfigRedis.delete(schoolId, configName);
            }
        }


    }

    @Override
    public void update(BgConfigModel config) {
        bgConfigDOMapper.updateByPrimaryKeySelective(converter.convert(config));
        sysConfigRedis.delete(config.getSchoolId(), config.getConfigName());
    }


    @Override
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            BgConfigDO bgConfigDO = bgConfigDOMapper.selectByPrimaryKey(id);
            sysConfigRedis.delete(bgConfigDO.getSchoolId(), bgConfigDO.getConfigName());
        }

        bgConfigDOMapper.removeByIds(ids);
    }


    @Override
    public String getValue(Integer schoolId, String configName) {
        String key = configName+"_"+schoolId;
        BgConfigModel config = sysConfigRedis.get(schoolId, key);
        if (config == null) {
            BgConfigQueryParam param = new BgConfigQueryParam();
            param.setSchoolId(schoolId);
            param.setConfigName(configName);
            BgConfigDO bgConfigDO = bgConfigDOMapper.queryByParam(param);
            config = converter.convert(bgConfigDO);
            sysConfigRedis.saveOrUpdate(config);
        }

        return Objects.nonNull(config) && config.getStatus() ? config.getConfigValue() : null;

//        BgConfigDO bgConfigDO = bgConfigDOMapper.queryByConfigName(key);
//        BgConfigModel config = converter.convert(bgConfigDO);
//        sysConfigRedis.saveOrUpdate(config);
//
//        return config == null ? null : config.getConfigValue();

    }

    /**
     * 获取非指定学校配置
     * @param configName
     * @return
     */
    @Override
    public String getValueNoSchool(String configName) {
        String key = configName;
//        学校id为-1，表示该配置不区分学校
        Integer schoolId = -1;
        BgConfigModel config = sysConfigRedis.get(schoolId, key);
        if (config == null) {
            BgConfigQueryParam param = new BgConfigQueryParam();
            param.setSchoolId(schoolId);
            param.setConfigName(configName);
            BgConfigDO bgConfigDO = bgConfigDOMapper.queryByParam(param);
            config = converter.convert(bgConfigDO);
            sysConfigRedis.saveOrUpdate(config);
        }

        return Objects.nonNull(config) && config.getStatus() ? config.getConfigValue() : null;

//        BgConfigDO bgConfigDO = bgConfigDOMapper.queryByConfigName(key);
//        BgConfigModel config = converter.convert(bgConfigDO);
//        sysConfigRedis.saveOrUpdate(config);
//
//        return config == null ? null : config.getConfigValue();

    }

    @Override
    public <T> T getConfigObject(Integer schoolId,String key, Class<T> clazz) {
        String value = getValue(schoolId, key);
        if (StringUtils.isNotBlank(value)) {
            return new Gson().fromJson(value, clazz);
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.GET_BG_CONFIG_ERROR, "configKey:{}", key);
        }
    }

//	@Override
//	public PageUtils queryPage(Map<String, Object> params) {
//		String paramKey = (String)params.get("paramKey");
//
//		IPage<SysConfigEntity> page = this.page(
//			new Query<SysConfigEntity>().getPage(params),
//			new QueryWrapper<SysConfigEntity>()
//				.like(StringUtils.isNotBlank(paramKey),"param_key", paramKey)
//				.eq("status", 1)
//		);
//
//		return new PageUtils(page);
//	}
//
//	@Override
//	public void saveConfig(SysConfigEntity config) {
//		this.save(config);
//		sysConfigRedis.saveOrUpdate(config);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void update(SysConfigEntity config) {
//		this.updateById(config);
//		sysConfigRedis.saveOrUpdate(config);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void updateValueByKey(String key, String value) {
//		baseMapper.updateValueByKey(key, value);
//		sysConfigRedis.delete(key);
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteBatch(Long[] ids) {
//		for(Long id : ids){
//			SysConfigEntity config = this.getById(id);
//			sysConfigRedis.delete(config.getParamKey());
//		}
//
//		this.removeByIds(Arrays.asList(ids));
//	}
//
//	@Override
//	public String getValue(String key) {
//		SysConfigEntity config = sysConfigRedis.get(key);
//		if(config == null){
//			config = baseMapper.queryByKey(key);
//			sysConfigRedis.saveOrUpdate(config);
//		}
//
//		return config == null ? null : config.getParamValue();
//	}
//
//	@Override
//	public <T> T getConfigObject(String key, Class<T> clazz) {
//		String value = getValue(key);
//		if(StringUtils.isNotBlank(value)){
//			return new Gson().fromJson(value, clazz);
//		}
//
//		try {
//			return clazz.newInstance();
//		} catch (Exception e) {
//			throw new LinfengException("获取参数失败");
//		}
//	}
}
