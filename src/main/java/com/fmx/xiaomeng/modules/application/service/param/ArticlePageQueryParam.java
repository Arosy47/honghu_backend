package com.fmx.xiaomeng.modules.application.service.param;

import com.fmx.xiaomeng.common.utils.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 */
public class ArticlePageQueryParam {

    private Long userId;

    private Integer category;

    private Integer functionType;

    private Integer schoolId;

    /**
     * 搜索关键字
     */
    private String keyword;

//    时间，热度
    private String order;

    /**
     * 是否匿名
     */
    private Boolean anonymous;

    /**
     * 是否查询置顶
     */
    private Boolean top;


    @NotNull
    private PageParam pageParam;


    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }

    public void setPageParam(com.fmx.xiaomeng.common.utils.PageParam pageParam) { this.pageParam = pageParam; }
    public void setCategory(Integer category) { this.category = category; }
    public void setFunctionType(Integer functionType) { this.functionType = functionType; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setOrder(String order) { this.order = order; }

    public com.fmx.xiaomeng.common.utils.PageParam getPageParam() { return pageParam; }


    public Integer getCategory() { return category; }
    public void setTop(boolean top) { this.top = top; }
    public Integer getFunctionType() { return functionType; }
    public Integer getSchoolId() { return schoolId; }

}
