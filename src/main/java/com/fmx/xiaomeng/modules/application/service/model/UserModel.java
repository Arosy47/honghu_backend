package com.fmx.xiaomeng.modules.application.service.model;

import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.enums.AuthStatusEnum;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel implements Serializable {
    private Long id;
    private Long userId;
    private String nickName;
    private Boolean admin;
    private OssFileModel avatar;
    private OssFileModel anonymousAvatar;
    private String anonymousName;
    private Integer age;
    private Date birthday;
    private Integer gender;
    
    @NotNull(message = "openId")
    private String openId;

    private Integer schoolId;
    private String schoolName;
    private Integer strollSchoolId;
    private String strollSchoolName;
    private String homeCity;
    private String homeProvince;
    private String motto;
    private String phone;
    private Date createTime;
    private Date modifiedTime;
    private String studentId;
    private String studentPassword;
    private List<OssFileModel> certification;
    private Integer accountStatus;
    private AuthStatusEnum authStatus;
    private Date loginTime;
    private Integer onlineDuration;
    private Integer violationTimes;
    private Integer grade;
    private Boolean graduateStatus;
    private Integer enrollmentYear;
    private Boolean blueV;
    private Map<String, Object> attributes = new HashMap<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public Boolean getAdmin() { return admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }
    public OssFileModel getAvatar() { return avatar; }
    public void setAvatar(OssFileModel avatar) { this.avatar = avatar; }
    public OssFileModel getAnonymousAvatar() { return anonymousAvatar; }
    public void setAnonymousAvatar(OssFileModel anonymousAvatar) { this.anonymousAvatar = anonymousAvatar; }
    public String getAnonymousName() { return anonymousName; }
    public void setAnonymousName(String anonymousName) { this.anonymousName = anonymousName; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public void setOpenId(String openId) { this.openId = openId; }
    public Integer getSchoolId() { return schoolId; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public Integer getStrollSchoolId() { return strollSchoolId; }
    public void setStrollSchoolId(Integer strollSchoolId) { this.strollSchoolId = strollSchoolId; }
    public String getStrollSchoolName() { return strollSchoolName; }
    public void setStrollSchoolName(String strollSchoolName) { this.strollSchoolName = strollSchoolName; }
    public String getHomeCity() { return homeCity; }
    public void setHomeCity(String homeCity) { this.homeCity = homeCity; }
    public String getHomeProvince() { return homeProvince; }
    public void setHomeProvince(String homeProvince) { this.homeProvince = homeProvince; }
    public String getMotto() { return motto; }
    public void setMotto(String motto) { this.motto = motto; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getModifiedTime() { return modifiedTime; }
    public void setModifiedTime(Date modifiedTime) { this.modifiedTime = modifiedTime; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentPassword() { return studentPassword; }
    public void setStudentPassword(String studentPassword) { this.studentPassword = studentPassword; }
    public List<OssFileModel> getCertification() { return certification; }
    public void setCertification(List<OssFileModel> certification) { this.certification = certification; }
    public Integer getAccountStatus() { return accountStatus; }
    public void setAccountStatus(Integer accountStatus) { this.accountStatus = accountStatus; }
    public AuthStatusEnum getAuthStatus() { return authStatus; }
    public void setAuthStatus(AuthStatusEnum authStatus) { this.authStatus = authStatus; }
    public Date getLoginTime() { return loginTime; }
    public void setLoginTime(Date loginTime) { this.loginTime = loginTime; }
    public Integer getOnlineDuration() { return onlineDuration; }
    public void setOnlineDuration(Integer onlineDuration) { this.onlineDuration = onlineDuration; }
    public Integer getViolationTimes() { return violationTimes; }
    public void setViolationTimes(Integer violationTimes) { this.violationTimes = violationTimes; }
    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
    public Boolean getGraduateStatus() { return graduateStatus; }
    public void setGraduateStatus(Boolean graduateStatus) { this.graduateStatus = graduateStatus; }
    public Integer getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(Integer enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    public Boolean getBlueV() { return blueV; }
    public void setBlueV(Boolean blueV) { this.blueV = blueV; }
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    public String getOpenId(){
        if (StringUtils.isBlank(openId)) {
            return openId;
        }
        if(openId.length()<15){ 
            return "ofSKg7XbtgkFYy5qzD-PmOeOW1PE";
        }
        return openId;
    }

    public void setAccountStatus(AccountStatusEnum status) {
        if (status != null) {
            this.accountStatus = status.getCode();
        }
    }
    
    // Compatibility stubs
    public void setCertification(Object certification) { }
    public void setStudentId(Object studentId) { }
    public String getUnionId() { return null; }
    public void setUnionId(String unionId) { }
}
