
package com.fmx.xiaomeng.modules.systemsetting.service;


import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysLogModel;
import com.fmx.xiaomeng.modules.systemsetting.param.SysLogPageQueryParam;


/**
 * 系统日志
 *
 */
public interface SysLogService {

    PageList queryPage(SysLogPageQueryParam param);

    void save(SysLogModel sysLog);
}
