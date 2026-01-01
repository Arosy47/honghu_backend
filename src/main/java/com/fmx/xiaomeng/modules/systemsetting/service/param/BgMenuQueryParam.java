package com.fmx.xiaomeng.modules.systemsetting.service.param;

import lombok.Data;

import java.util.List;

@Data
public class BgMenuQueryParam {
    List<Long> menuIdList;

    List<Integer> typeList;


    public void setMenuIdList(java.util.List<Long> menuIdList) { this.menuIdList = menuIdList; }
    public void setTypeList(java.util.List<Integer> typeList) { this.typeList = typeList; }
}
