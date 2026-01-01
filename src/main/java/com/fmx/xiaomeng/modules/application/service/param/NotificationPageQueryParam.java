package com.fmx.xiaomeng.modules.application.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NotificationPageQueryParam {

    private Long userId;

    private Long targetUserId;

    private List<Integer> noticeTypeList;

    @NotNull
    private PageParam pageParam;

    public void setNoticeTypeList(java.util.List<Integer> noticeTypeList) { this.noticeTypeList = noticeTypeList; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }


        public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }
        public java.util.List<Integer> getNoticeTypeList() { return noticeTypeList; }
    
}
