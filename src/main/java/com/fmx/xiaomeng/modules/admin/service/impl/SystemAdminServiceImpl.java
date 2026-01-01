package com.fmx.xiaomeng.modules.admin.service.impl;

import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.modules.admin.service.SystemAdminService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author honghu
 * @date 2025-12-20
 */
@Service("systemAdminService")
public class SystemAdminServiceImpl implements SystemAdminService {


    @Override
    public PageList queryPage(Map<String, Object> params) {
        return new PageList(null, null);
    }


}