package com.fmx.xiaomeng.common.converter;

import com.fmx.xiaomeng.modules.admin.model.MockArticleModel;
import com.fmx.xiaomeng.modules.admin.request.ArticleAdminRequestDTO;
import com.fmx.xiaomeng.modules.admin.request.MockArticleAdminRequestDTO;
import com.fmx.xiaomeng.modules.application.controller.request.*;
// import com.fmx.xiaomeng.modules.application.controller.response.RankingParticipantVO;
// import com.fmx.xiaomeng.modules.application.service.eduAdminSystem.model.CourseModel;
import com.fmx.xiaomeng.modules.application.service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ParamConverter {
    ParamConverter INSTANCT = Mappers.getMapper(ParamConverter.class);

    @Mapping(target = "birthday", expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.strToDate(userRequestDTO.getBirthday(),com.fmx.xiaomeng.common.utils.DateUtil.DATE_FORMAT_DATE))")
    UserModel convert(UserRequestDTO userRequestDTO);

    

    @Mapping(target = "category",source = "categoryCode")
//    @Mapping(target = "voteOptionList",ignore = true)
    ArticleModel convert(ArticleRequestDTO articleRequestDTO);

    @Mapping(target = "category",source = "categoryCode")
    ArticleModel convert(ArticleAdminRequestDTO articleRequestDTO);


    ArticleCommentModel convert(CommentRequestDTO commentRequestDTO);

    // SchoolModel convert(SchoolRequestDTO schoolRequestDTO);

    // SlideMatchCardModel convert(SlideMatchCardRequestDTO articleRequestDTO);

    // CourseModel convert(CourseAddOrUpdateRequestDTO articleRequestDTO);


    // TaskModel convert(TaskDTO taskDTO);

    // @Mapping(target = "startTime", expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.strToDate(requestDTO.getStartTime(),com.fmx.xiaomeng.common.utils.DateUtil.DATE_FORMAT))")
    // @Mapping(target = "endTime", expression = "java(com.fmx.xiaomeng.common.utils.DateUtil.strToDate(requestDTO.getEndTime(),com.fmx.xiaomeng.common.utils.DateUtil.DATE_FORMAT))")
    // RankingActivityModel convert(RankingActivityAdminRequestDTO requestDTO);

    @Mapping(target = "category",source = "categoryCode")
    MockArticleModel convert(MockArticleAdminRequestDTO mockArticleAdminRequestDTO);
}
