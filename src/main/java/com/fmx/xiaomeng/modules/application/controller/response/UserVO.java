package com.fmx.xiaomeng.modules.application.controller.response;
import lombok.Data;
import java.util.List;
import java.util.Map;
@Data
public class UserVO {
    private Long userId;
    private String nickName;
    private String avatar; // String URL
    private String anonymousName;
    private String anonymousAvatar;
    private String authStatus;
    private String accountStatus;
    private String createTime;
    private String birthday;
    private String homeCity;
    private String homeProvince;
    private String motto;
    private String phone;
    private String studentId;
    private Integer score; // Added
    private Integer age;
    private Integer gender;
    private Integer schoolId;
    private String schoolName;
    private Integer strollSchoolId;
    private String strollSchoolName;
    private Integer grade;
    private Integer enrollmentYear;
    private Boolean graduateStatus;
    private Boolean blueV;
    private Boolean admin;
    private Boolean hiddenArticle;
    private Boolean hiddenCollect;
    private List<String> certification; // URLs
}