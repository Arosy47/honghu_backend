
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.application.repository.dao.SysLogDOMapper;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysLogModel;
import com.fmx.xiaomeng.modules.systemsetting.param.SysLogPageQueryParam;
import com.fmx.xiaomeng.modules.systemsetting.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author honghu
 * @date 2025-12-20
 */
@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDOMapper sysLogDOMapper;
    @Autowired
    private Converter converter;

    @Override
    public PageList queryPage(SysLogPageQueryParam params) {
        return null;
    }

    @Override
    public void save(SysLogModel sysLog) {
        sysLogDOMapper.insertSelective(converter.convert(sysLog));
    }
}
