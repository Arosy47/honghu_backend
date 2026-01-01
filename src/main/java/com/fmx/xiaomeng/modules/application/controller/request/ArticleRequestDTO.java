package com.fmx.xiaomeng.modules.application.controller.request;

import com.fmx.xiaomeng.common.enums.ArticleDisplayTypeEnum;
import com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum;
import com.fmx.xiaomeng.modules.application.service.model.*;
import lombok.Data;

import java.util.List;

@Data
public class ArticleRequestDTO {
    /**
     * 如果是创建文章，则不传id
     */
    private Long id;

    /**
     * 题目
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片链接
     */
    private List<OssFileModel> imgUrlList;

    /**
     * 类目
     */
    private String categoryCode;

    /**
     * 类型
     *
     * @see ArticleDisplayTypeEnum  直接传枚举名字
     */
    private ArticleDisplayTypeEnum displayType;

    /**
     * 功能类型
     */
    private ArticleFunctionTypeEnum functionType;

    /**
     * 是否匿名
     */
    private Boolean anonymous;

    private PositionModel positionInfo;



    //------------------------抽奖贴end--------------------------//


    //------------------------二手贴start--------------------------//
    /**
     * 价格
     */
    private Long price;


    //------------------------二手贴end--------------------------//


//    public static void main(String[] args) {
//        System.out.println(JSON.toJSONString(""));
//        System.out.println(JSON.toJSONString(null));
//        System.out.println(JSON.toJSONString(new ArrayList<>()));
//
//        System.out.println(JSON.parseObject("[]", OssFileModel.class));
//        System.out.println(JSON.parseObject("", OssFileModel.class));
//    }

    public Long getUserId() { return userId; }
    public Integer getSchoolId() { return schoolId; }
    public Boolean getAnonymous() { return anonymous; }

    private Long userId; private Integer schoolId;
}
