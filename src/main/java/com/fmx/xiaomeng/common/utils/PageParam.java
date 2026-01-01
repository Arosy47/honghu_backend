package com.fmx.xiaomeng.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam implements Serializable {
    private int page = 1;

    private int size = 10;

    public PageParam(Integer pageNum) {
        this.page = pageNum;
    }
    public PageParam(Integer pageNum, Integer pageSize) {
        this.page = pageNum;
        this.size = pageSize;
    }


    public int getOffset() {
        return (page - 1) * size;
    }

    public Integer getPage() { return page; }
    public Integer getSize() { return size; }
}
