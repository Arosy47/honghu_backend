package com.fmx.xiaomeng.modules.admin.service;

import com.fmx.xiaomeng.common.utils.PageList;

import java.util.Map;

public interface SystemAdminService {

    PageList queryPage(Map<String, Object> params);
}
