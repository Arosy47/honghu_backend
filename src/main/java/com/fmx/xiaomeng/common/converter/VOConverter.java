package com.fmx.xiaomeng.common.converter;

import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.admin.response.ReportRecordVO;
import com.fmx.xiaomeng.modules.application.controller.response.*;
import com.fmx.xiaomeng.modules.application.service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface VOConverter {
    VOConverter INSTANCT = Mappers.getMapper(VOConverter.class);

    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(userModel.getCreateTime().getTime()))")
    @Mapping(target = "authStatus", expression = "java(userModel.getAuthStatus().getDesc())")
    @Mapping(target = "accountStatus", expression = "java(userModel.getAccountStatus() == null ? null : com.fmx.xiaomeng.common.enums.AccountStatusEnum.getByCode(userModel.getAccountStatus()).getDesc())")
    @Mapping(target = "birthday", expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.dateToStr(userModel.getBirthday(),com.fmx.xiaomeng.common.utils.DateUtil.DATE_FORMAT_DATE))")
    @Mapping(target = "hiddenArticle", expression = "java(userModel.getAttributes().get(\"hiddenArticle\") == null ? null : (java.lang.Boolean)userModel.getAttributes().get(\"hiddenArticle\"))")
    @Mapping(target = "hiddenCollect", expression = "java(userModel.getAttributes().get(\"hiddenCollect\") == null ? null : (java.lang.Boolean)userModel.getAttributes().get(\"hiddenCollect\"))")
    @Named("one")
    UserVO convert(UserModel userModel);

    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "nickName", ignore = true)
    @Mapping(target = "anonymousName", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "homeCity", ignore = true)
    @Mapping(target = "homeProvince", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "motto", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(userModel.getCreateTime().getTime()))")
    @Mapping(target = "authStatus", expression = "java(userModel.getAuthStatus().getDesc())")
    @Named("two")
    UserVO sensitiveConvert(UserModel userModel);

    UserLocationVO convert(UserLocationModel userLocationModel);



    @Mapping(target = "categoryName",
            expression = "java(articleModel.getCategory() == null ? null :com.fmx.xiaomeng.common.enums.CategoryEnum.getById(articleModel.getCategory().getId()).getDesc())")
    @Mapping(target = "categoryCode", expression = "java(articleModel.getCategory() == null ? null :articleModel.getCategory().name())")
    @Mapping(target = "displayType", expression = "java(articleModel.getDisplayType() == null ? null : articleModel.getDisplayType().name())")
    @Mapping(target = "functionType", expression = "java(articleModel.getFunctionType() == null ? null :com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum.getByCode(articleModel.getFunctionType()).name())")
    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(articleModel.getCreateTime().getTime()))")
    @Named("one")
    ArticleVO convert(ArticleModel articleModel);

    @Mapping(target = "categoryName",
            expression = "java(articleModel.getCategory() == null ? null :com.fmx.xiaomeng.common.enums.CategoryEnum.getById(articleModel.getCategory().getId()).getDesc())")
    @Mapping(target = "categoryCode", expression = "java(articleModel.getCategory() == null ? null :articleModel.getCategory().name())")
    @Mapping(target = "displayType", expression = "java(articleModel.getDisplayType() == null ? null : articleModel.getDisplayType().name())")
    @Mapping(target = "functionType", expression = "java(articleModel.getFunctionType() == null ? null :com.fmx.xiaomeng.common.enums.ArticleFunctionTypeEnum.getByCode(articleModel.getFunctionType()).name())")
    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(articleModel.getCreateTime().getTime()))")
    @Named("two")
    ArticleVO sensitiveConvert(ArticleModel articleModel);

    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(commentModel.getCreateTime().getTime()))")
    ArticleCommentVO convert(ArticleCommentModel commentModel);

    List<ArticleCommentVO> convertArticleCommentList(List<ArticleCommentModel> commentModels);



    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(notificationModel.getCreateTime().getTime()))")
    NotificationVO convert(NotificationModel notificationModel);

    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.dateToStr(reportRecordModel.getCreateTime(), com.fmx.xiaomeng.common.utils.DateUtil.DATE_FORMAT))")
    ReportRecordVO convert(ReportRecordModel reportRecordModel);



    @Mapping(target = "categoryName",
            expression = "java(articleModel.getCategory() == null ? null :com.fmx.xiaomeng.common.enums.CategoryEnum.getById(articleModel.getCategory().getId()).getDesc())")
    @Mapping(target = "categoryCode", expression = "java(articleModel.getCategory() == null ? null :articleModel.getCategory().name())")
    @Mapping(target = "createTime",
            expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.timeStamp2ReadableTime(articleModel.getCreateTime().getTime()))")
    MockArticleVO convert(MockArticleModel articleModel);



    default String map(com.fmx.xiaomeng.modules.application.service.model.OssFileModel value) {
        if (value == null) return null;
        return value.getUrl();
    }



    default Integer map(com.fmx.xiaomeng.common.enums.CommentTypeEnum value) {
        return value != null ? value.getCode() : null;
    }

}
