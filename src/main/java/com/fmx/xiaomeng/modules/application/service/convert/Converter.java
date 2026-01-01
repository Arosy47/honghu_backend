package com.fmx.xiaomeng.modules.application.service.convert;

import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminMenuDO;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminRoleDO;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserDO;
import com.fmx.xiaomeng.modules.admin.repository.model.BgConfigDO;
import com.fmx.xiaomeng.modules.application.repository.model.*;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("converter")
public interface Converter {

    Converter INSTANCT = Mappers.getMapper(Converter.class);

    //    @Mapping(target = "studentPassword", ignore = true)
    @Mapping(target = "certification", expression =
            "java(userDO.getCertification()==null?new java.util.ArrayList():com.alibaba.fastjson.JSON.parseObject(userDO.getCertification(), " +
                    "new com.alibaba.fastjson.TypeReference<java.util.List<com.fmx.xiaomeng.modules.application.service.model.OssFileModel>>(){}))")
    @Mapping(target = "authStatus", expression = "java(com.fmx.xiaomeng.common.enums.AuthStatusEnum.getByCode(userDO.getAuthStatus()))")
    @Mapping(target = "accountStatus", expression = "java(com.fmx.xiaomeng.common.enums.AccountStatusEnum.getByCode(userDO.getAccountStatus()))")
    @Mapping(target = "attributes", expression =
            "java(userDO.getAttributes() == null ? new java.util.HashMap<>() : com.alibaba.fastjson.JSON.parseObject(userDO.getAttributes(), new com.alibaba.fastjson.TypeReference<java.util.Map<String, Object>>() {}))")
    @Mapping(target = "avatar", expression = "java(com.alibaba.fastjson.JSON.parseObject(userDO.getAvatar(), " +
            "com.fmx.xiaomeng.modules.application.service.model.OssFileModel.class))")
    @Mapping(target = "anonymousAvatar", expression = "java(com.alibaba.fastjson.JSON.parseObject(userDO.getAnonymousAvatar(), " +
            "com.fmx.xiaomeng.modules.application.service.model.OssFileModel.class))")
    UserModel convert(UserDO userDO);

    @Mapping(target = "certification", expression = "java(userModel.getCertification()==null?null:com.alibaba.fastjson.JSON.toJSONString(userModel.getCertification()))")
    @Mapping(target = "authStatus", expression = "java(userModel.getAuthStatus()==null?null:userModel.getAuthStatus().getCode())")
    @Mapping(target = "accountStatus", expression = "java(userModel.getAccountStatus()==null?null:userModel.getAccountStatus())")
    @Mapping(target = "attributes", expression = "java(org.springframework.util.CollectionUtils.isEmpty(userModel.getAttributes()) ? null : com.alibaba.fastjson.JSON.toJSONString(userModel.getAttributes()))")
    @Mapping(target = "avatar", expression = "java(userModel.getAvatar()==null?null: com.alibaba.fastjson.JSON.toJSONString(userModel.getAvatar()))")
    @Mapping(target = "anonymousAvatar", expression = "java(userModel.getAnonymousAvatar()==null?null: com.alibaba.fastjson.JSON.toJSONString(userModel.getAnonymousAvatar()))")
    UserDO convert(UserModel userModel);

    List<UserModel> convertToUserList(List<UserDO> userDO);

    @Mapping(target = "imgUrlList", expression =
            "java(com.alibaba.fastjson.JSON.parseObject(articleDO.getImgUrlList(), " +
                    "new com.alibaba.fastjson.TypeReference<java.util.List<com.fmx.xiaomeng.modules.application.service.model.OssFileModel>>(){}))")
    @Mapping(target = "displayType",
            expression = "java(com.fmx.xiaomeng.common.enums.ArticleDisplayTypeEnum.getByCode(articleDO.getDisplayType()))")
    @Mapping(target = "functionType",
            expression = "java(com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum.getByCode(articleDO.getFunctionType()))")
    @Mapping(target = "category",
            expression = "java(com.fmx.xiaomeng.common.enums.CategoryEnum.getById(articleDO.getCategory()))")
    @Mapping(target = "positionInfo", expression =
            "java(com.alibaba.fastjson.JSON.parseObject(articleDO.getPositionInfo(), " +
                    "com.fmx.xiaomeng.modules.application.service.model.PositionModel.class))")
    ArticleModel convert(ArticleDO articleDO);


    @Mapping(target = "imgUrlList", expression = "java(articleModel.getImgUrlList()==null?null:com.alibaba.fastjson.JSON.toJSONString(articleModel.getImgUrlList()))")
    @Mapping(target = "displayType", expression = "java(articleModel.getDisplayType()==null?null:articleModel.getDisplayType().getCode())")
    @Mapping(target = "functionType", expression = "java(articleModel.getFunctionType())")
    @Mapping(target = "category", expression = "java(articleModel.getCategory()==null?null:articleModel.getCategory().getId())")
    @Mapping(target = "positionInfo", expression = "java(articleModel.getPositionInfo()==null?null:com.alibaba.fastjson.JSON.toJSONString(articleModel.getPositionInfo()))")
    ArticleDO convert(ArticleModel articleModel);

    @Mapping(target = "imgUrl", expression =
            "java(com.alibaba.fastjson.JSON.parseObject(articleCommentDO.getImgUrl(), " +
                    "com.fmx.xiaomeng.modules.application.service.model.OssFileModel.class))")
    @Mapping(target = "commentType",
            expression = "java(com.fmx.xiaomeng.common.enums.CommentTypeEnum.getByCode(articleCommentDO.getCommentType()))")
    ArticleCommentModel convert(ArticleCommentDO articleCommentDO);

    @Mapping(target = "imgUrl", expression = "java(articleCommentModel.getImgUrl()==null?null:com.alibaba.fastjson.JSON.toJSONString(articleCommentModel.getImgUrl()))")
    @Mapping(target = "commentType", expression = "java(articleCommentModel.getCommentType()==null?null:articleCommentModel.getCommentType().getCode())")
    ArticleCommentDO convert(ArticleCommentModel articleCommentModel);

    List<ArticleCommentModel> convertToCommentModelList(List<ArticleCommentDO> articleCommentDOList);


    List<ArticleModel> convertArticleList(List<ArticleDO> articleDOList);

    NotificationModel convert(NotificationDO notificationDO);

    NotificationDO convert(NotificationModel notificationModel);

    List<NotificationModel> convertNotificationList(List<NotificationDO> notificationDOList);




    ThumbUpModel convert(ThumbUpDO thumbUpDO);

    ThumbUpDO convert(ThumbUpModel thumbUpModel);


    AdminUserModel convert(AdminUserDO adminUserDO);

    AdminUserDO convert(AdminUserModel adminUserModel);

    List<AdminUserModel> convertToAdminUserList(List<AdminUserDO> adminUserDOList);

    @Mapping(target = "menuId", source = "adminMenuDO.id")
    AdminMenuModel convert(AdminMenuDO adminMenuDO);

    List<AdminMenuModel> convertAdminMenuList(List<AdminMenuDO> adminMenuDOList);

    CollectModel convert(CollectDO collectDO);

    List<CollectModel> convertCollectList(List<CollectDO> collects);


    BgConfigModel convert(BgConfigDO bgConfigDO);

    List<BgConfigModel> convertToBgConfigModelList(List<BgConfigDO> bgConfigDOList);

    BgConfigDO convert(BgConfigModel bgConfigModel);

    List<AdminRoleModel> convertAdminRoleModelList(List<AdminRoleDO> adminRoleDOList);



    CategoryModel convert(ArticleCategoryDO categoryDO);


    @Mapping(target = "reportType", expression = "java(com.fmx.xiaomeng.common.enums.ReportTypeEnum.getByCode(recordDO.getReportType()))")
    ReportRecordModel convert(ReportRecordDO recordDO);




    SysLogModel convert(SysLogDO sysLogModel);

    SysLogDO convert(SysLogModel sysLogModel);
    
    List<WxMsgSubscribeRecordModel> convertToWxMsgSubscribeRecordList(List<WxMsgSubscribeRecordDO> recordDOList);

    @Mapping(target = "imgUrlList", expression =
            "java(com.alibaba.fastjson.JSON.parseObject(articleDO.getImgUrlList(), " +
                    "new com.alibaba.fastjson.TypeReference<java.util.List<com.fmx.xiaomeng.modules.application.service.model.OssFileModel>>(){}))")
    @Mapping(target = "category",
            expression = "java(com.fmx.xiaomeng.common.enums.CategoryEnum.getById(articleDO.getCategory()))")
    MockArticleModel convert(MockArticleDO articleDO);

    @Mapping(target = "imgUrlList", expression = "java(articleModel.getImgUrlList()==null?null:com.alibaba.fastjson.JSON.toJSONString(articleModel.getImgUrlList()))")
    @Mapping(target = "category", expression = "java(articleModel.getCategory()==null?null:articleModel.getCategory().getId())")
    MockArticleDO convert(MockArticleModel articleModel);

    List<MockArticleModel> convertMockArticleList(List<MockArticleDO> articleDOList);



    default com.fmx.xiaomeng.modules.application.service.model.OssFileModel map(String value) {
        if (value == null) return null;
        return new com.fmx.xiaomeng.modules.application.service.model.OssFileModel(null, value);
    }
    default String map(com.fmx.xiaomeng.modules.application.service.model.OssFileModel value) {
        if (value == null) return null;
        return value.getUrl();
    }

}
