package com.fmx.xiaomeng.modules.admin.service.impl;

import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.ReportHandleStatusEnum;
import com.fmx.xiaomeng.common.enums.ReportTypeEnum;
// import com.fmx.xiaomeng.common.enums.ScoreActionEnum;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.common.utils.Paginator;
import com.fmx.xiaomeng.modules.admin.service.ReportAdminService;
import com.fmx.xiaomeng.modules.admin.service.param.ReportPageQueryParam;
import com.fmx.xiaomeng.modules.application.repository.dao.ArticleDOMapper;
import com.fmx.xiaomeng.modules.application.repository.dao.ReportRecordDOMapper;
// import com.fmx.xiaomeng.modules.application.repository.dao.TaskDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.ReportRecordDO;
import com.fmx.xiaomeng.modules.application.service.*;
import com.fmx.xiaomeng.modules.application.service.convert.Converter;
import com.fmx.xiaomeng.modules.application.service.model.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author honghu
 * @date 2025-12-19
 */
@Service
public class ReportAdminServiceImpl implements ReportAdminService {

    @Autowired
    private ReportRecordDOMapper reportRecordDOMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private Converter converter;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public PageList<ReportRecordModel> listReport(ReportPageQueryParam param) {

        long total = reportRecordDOMapper.countByParam(param);
        List<ReportRecordModel> reportRecordModelList = new ArrayList<>();
        PageParam pageParam = param.getPageParam();
        if (total > pageParam.getOffset()) {

            List<ReportRecordDO> reportRecordDOS = reportRecordDOMapper.pageQuery(param);
            reportRecordModelList = Optional.ofNullable(reportRecordDOS).orElse(Collections.emptyList())
                    .stream().map(recordDO -> {
                        Integer reportType = recordDO.getReportType();
                        Long reportTypeId = recordDO.getReportTypeId();
                        ReportRecordModel model = converter.convert(recordDO);

                        if (ReportTypeEnum.ARTICLE.getCode().equals(reportType)) {
                            ArticleModel articleModel = articleService.queryByPrimaryKey(reportTypeId);
                            if (Objects.nonNull(articleModel)) {
                                model.setContent(articleModel.getContent());
                                model.setImgUrlList(articleModel.getImgUrlList());
                            }


                        } else if (ReportTypeEnum.ARTICLE_COMMENT.getCode().equals(reportType)) {
                            ArticleCommentModel articleCommentModel = commentService.queryByPrimaryKey(reportTypeId);
                            if (Objects.nonNull(articleCommentModel)) {
                                model.setContent(articleCommentModel.getContent());

                                OssFileModel ossFileModel = articleCommentModel.getImgUrl();
                                if (Objects.nonNull(ossFileModel)) {
                                    model.setImgUrlList(Lists.newArrayList(ossFileModel));
                                }
                            }
                        }
                        return model;
                    }).collect(Collectors.toList());
        }
        return new PageList<>(reportRecordModelList, new Paginator(param.getPageParam(), total));

    }

    @Override
    @Transactional
    public void reportValid(Integer reportId, Integer reportType, Long reportTypeId) {


        ReportRecordDO update = new ReportRecordDO();
        update.setId(reportId);
        update.setHandleStatus(ReportHandleStatusEnum.COMPLETED.getCode());
        update.setReportValid(true);
        reportRecordDOMapper.updateByPrimaryKeySelective(update);
        ReportRecordDO recordDO = reportRecordDOMapper.selectByPrimaryKey(reportId);

        if (ReportTypeEnum.ARTICLE.getCode().equals(reportType)) {
            articleService.reportSuccess(reportTypeId, recordDO);

        } else if (ReportTypeEnum.ARTICLE_COMMENT.getCode().equals(reportType)) {
            commentService.reportSuccess(reportTypeId, recordDO);


            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setNoticeType(NotificationTypeEnum.REPORT_SUCCESS.getCode());
            notificationModel.setContent("举报成功，奖励积分10");
            notificationModel.setTargetUserId(recordDO.getUserId());
            notificationModel.setCreateTime(new Date());
            notificationService.createNotification(notificationModel);
            // userService.addScore(recordDO.getUserId(), ScoreActionEnum.REPORT_SUCCESS.getScore(), ScoreActionEnum.REPORT_SUCCESS);
        }

    }
    @Override
    public void reportInvalid (Integer reportId){
        reportRecordDOMapper.selectByPrimaryKey(reportId);


    }
}
