package com.fmx.xiaomeng.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Paginator implements Serializable {
    private int page = 1;
    private int size = 5;

    private long total = 0;

    public Paginator(PageParam param, long total) {
        this.page = param.getPage();
        this.size = param.getSize();
        this.total = total;
    }

    ;
}
