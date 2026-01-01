package com.fmx.xiaomeng.modules.application.controller.response;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class SchoolVO {
    private Integer id;

    /**
     * 还不知道有啥用
     */
    private Integer groupId;

    @NotBlank
    private String name;


    private String abbreviation;

    private Date createTime;

    private String city;

    private String province;

    /**
     * 访问类型，默认只能本校访问，还有市内，全国
     */
    private Integer accessType;

    private Long studentNum;

    private String openStatus;

    //    众筹
    private Integer crowdfunding;


    private Integer crowdfundingTotal;

    private String badgeUrl;

    //    排序，可以手动修改，控制按这个顺序在前端展示
    private Integer sort;

    /**
     * 学期开始日期
     */
    @Deprecated
    String termStartDate;

    /**
     * 当前学期
     */
    String currentTerm;

}
