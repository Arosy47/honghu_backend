package com.fmx.xiaomeng.modules.application.controller.request;

import com.fmx.xiaomeng.modules.application.service.model.OssFileModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRequestDTO {

    /**
     * 微信授权code
     */
    private String code;

    /**
     * 用户id，修改用户资料时必填
     */
    @NotNull
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 匿名名称
     */
    private String anonymousName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 性别 0女1男  2保密（默认）
     */
    private Integer gender;

    /**
     * 头像链接
     */
    private OssFileModel avatar;

    /**
     * 匿名头像链接
     */
    private OssFileModel anonymousAvatar;

    /**
     * 学校id
     */
    private String schoolId;


    /**
     * 学校Id（闲逛学校id）
     */
    private Integer strollSchoolId;


    /**
     * 家乡（城市）
     */
    private String homeCity;

    /**
     * 家乡（省份）
     */
    private String homeProvince;

    /**
     * 座右铭
     */
    private String motto;

    /**
     * 手机号
     */
    private String phone;


    private Integer enrollmentYear;

    private Boolean graduateStatus;

//
//    /**
//     * 积分
//     */
//    private Long score;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 隐藏帖子
     */
    private Boolean hiddenArticle;

    /**
     * 隐藏收藏
     */
    private Boolean hiddenCollect;



    public String getCode() { return code; }
}
