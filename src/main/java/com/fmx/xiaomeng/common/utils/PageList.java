package com.fmx.xiaomeng.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Data
public class PageList<E> implements Serializable {
    private List<E> dataList;
    private Paginator paginator;

    public PageList(List<E> dataList, Paginator paginator) {
        this.dataList = dataList;
        this.paginator = paginator;
    }

    public java.util.List<E> getDataList() { return dataList; }
    public com.fmx.xiaomeng.common.utils.Paginator getPaginator() { return paginator; }
}
