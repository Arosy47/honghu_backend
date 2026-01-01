package com.fmx.xiaomeng.modules.application.controller;

import com.fmx.xiaomeng.common.converter.ParamConverter;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.exception.BusinessException;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.controller.request.ArticleRequestDTO;
import com.fmx.xiaomeng.modules.application.controller.response.ArticleVO;
import com.fmx.xiaomeng.modules.application.service.ArticleService;
import com.fmx.xiaomeng.modules.application.service.model.ArticleModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/app/article")
@Slf4j
/**
 * @author honghu
 */
public class ArticleController {

    @Autowired
    private ParamConverter converter;

    @Autowired
    private VOConverter voConverter;

    @Autowired
    private ArticleService articleService;

    @NeedLogin
    @PostMapping("/createArticle")
    public Result<Integer> createArticle(@RequestBody ArticleRequestDTO articleRequestDTO,
                                         @UserInfo UserModel userModel) throws BusinessException, WxErrorException, ParseException {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        Integer schoolId = userModel.getStrollSchoolId();
        if (schoolId == null) {
            throw new BusinessException(ErrorCodeEnum.UNKNOWN_ERROR, "请选择学校");
        }

        ArticleModel articleModel = converter.convert(articleRequestDTO);
        articleModel.setUserNickName(userModel.getNickName());
        articleModel.setAnonymousName(userModel.getAnonymousName());
        if (Boolean.TRUE.equals(articleRequestDTO.getAnonymous())) {
            articleModel.setAvatar(userModel.getAnonymousAvatar().getUrl());
        } else {
            articleModel.setAvatar(userModel.getAvatar() != null ? userModel.getAvatar().getUrl() : null);
        }
        articleModel.setUserId(userModel.getUserId());
        articleModel.setSchoolId(schoolId);

        articleModel.setAdmin(Boolean.TRUE.equals(userModel.getAdmin()));

        articleService.createArticle(articleModel);
        return Result.ok(5);
    }

    @GetMapping("/detail")
    public Result<ArticleVO> detail(@RequestParam(name = "id") Long id) throws BusinessException {
        ArticleModel model = articleService.detail(id);
        return Result.ok(voConverter.convert(model));
    }


}
