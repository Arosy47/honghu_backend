package com.fmx.xiaomeng.modules.application.controller;

import com.alibaba.fastjson.JSON;
import com.fmx.xiaomeng.common.converter.VOConverter;
import com.fmx.xiaomeng.common.enums.AccountStatusEnum;
import com.fmx.xiaomeng.common.enums.NotificationTypeEnum;
import com.fmx.xiaomeng.common.enums.WxMsgTemplateTypeEnum;
import com.fmx.xiaomeng.common.error.ErrorCodeEnum;
import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.common.utils.PageList;
import com.fmx.xiaomeng.common.utils.PageParam;
import com.fmx.xiaomeng.modules.application.annotation.NeedLogin;
import com.fmx.xiaomeng.modules.application.annotation.UserInfo;
import com.fmx.xiaomeng.modules.application.controller.response.NotificationVO;
import com.fmx.xiaomeng.modules.application.service.NotificationService;
import com.fmx.xiaomeng.modules.application.service.UserService;
import com.fmx.xiaomeng.modules.application.service.WxMsgSubscribeService;
import com.fmx.xiaomeng.modules.application.service.model.NotificationModel;
import com.fmx.xiaomeng.modules.application.service.model.UserModel;
import com.fmx.xiaomeng.modules.application.service.model.WxMsgSubscribeRecordModel;
import com.fmx.xiaomeng.modules.application.service.param.NotificationPageQueryParam;
import com.fmx.xiaomeng.modules.application.service.param.NotificationParam;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 客户端消息通知模块
 * @Date 2023/3/4 23:28
 * @Author honghu
 **/
@CustomLog
@RestController
@RequestMapping("/app/notification")
public class NotificationController {
    @Autowired
    VOConverter voConverter;
    @Resource
    private NotificationService notificationService;
    @Resource
    private UserService userService;
    @Resource
    private WxMsgSubscribeService wxMsgSubscribeService;

    /**
     * 查看所有通知
     *
     * @param noticeTypeList 消息类型
     * @param pageNum
     * @return
     * @link com.fmx.xiaomeng.common.enums.NotificationTypeEnum
     */
    @NeedLogin
    @GetMapping("/listNoticeMessage")
    public Result<PageList<NotificationVO>> listNoticeMessage(@RequestParam(name = "noticeTypeList") String noticeTypeList,
                                                              @RequestParam(name = "pageNum") Integer pageNum,
                                                              @RequestParam(name = "pageSize") Integer pageSize,
                                                              @UserInfo UserModel userModel) {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }
        Long userId = userModel.getUserId();
        NotificationPageQueryParam param = new NotificationPageQueryParam();
        param.setNoticeTypeList(JSON.parseArray(noticeTypeList, Integer.class));
        param.setTargetUserId(userId);
        param.setPageParam(new PageParam(pageNum, pageSize));
        PageList<NotificationModel> notificationModelPageList = notificationService.listNoticeMessage(param, userModel);
        List<NotificationVO> notificationVOList = Optional.ofNullable(notificationModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> {
                    NotificationVO notificationVO = voConverter.convert(model);

                    return notificationVO;
                }).collect(Collectors.toList());
        return Result.ok(new PageList<>(notificationVOList, notificationModelPageList.getPaginator()));
    }


    /**
     * 查看违规通知
     *
     * @param pageNum
     * @return
     * @link com.fmx.xiaomeng.common.enums.NotificationTypeEnum
     */
    @NeedLogin
    @GetMapping("/getSysNotifications")
    public Result<PageList<NotificationVO>> getSysNotifications(@RequestParam(name = "pageNum") Integer pageNum,
                                                                @RequestParam(name = "pageSize") Integer pageSize,
                                                                @UserInfo UserModel userModel) {
//        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
//            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
//        }
        Long userId = userModel.getUserId();
        NotificationPageQueryParam param = new NotificationPageQueryParam();

//        查询系统通知，目前只有违规这一通知，后面可能有升级通知，积分兑换通知啥的
        param.setNoticeTypeList(Arrays.asList(
                NotificationTypeEnum.ARTICLE_VIOLATION.getCode(),
                NotificationTypeEnum.COMMENT_VIOLATION.getCode(),
                NotificationTypeEnum.AVATAR_VIOLATION.getCode(),
                NotificationTypeEnum.CERTIFICATION_PASS.getCode(),
                NotificationTypeEnum.CERTIFICATION_UNPASS.getCode(),
                NotificationTypeEnum.REPORT_SUCCESS.getCode()
        ));
        param.setTargetUserId(userId);
        param.setPageParam(new PageParam(pageNum,pageSize));
        PageList<NotificationModel> notificationModelPageList = notificationService.getSysNotifications(param, userModel);

        List<NotificationVO> notificationVOList = Optional.ofNullable(notificationModelPageList.getDataList())
                .orElse(Collections.emptyList()).stream().map(model -> voConverter.convert(model)).collect(Collectors.toList());

        return Result.ok(new PageList<>(notificationVOList, notificationModelPageList.getPaginator()));
    }

    /**
     * 查看最新系统通知内容
     *
     * @return
     * @link com.fmx.xiaomeng.common.enums.NotificationTypeEnum
     */
    @NeedLogin
    @GetMapping("/getNewestSystemNotice")
    public Result<String> getNewestSystemNotice(@UserInfo UserModel userModel) {
        Long userId = userModel.getUserId();
        NotificationParam param = new NotificationParam();

//        查询系统通知，目前只有违规这一通知，后面可能有升级通知，积分兑换通知啥的
        param.setNoticeTypeList(Arrays.asList(
                NotificationTypeEnum.ARTICLE_VIOLATION.getCode(),
                NotificationTypeEnum.COMMENT_VIOLATION.getCode(),
                NotificationTypeEnum.AVATAR_VIOLATION.getCode(),
                NotificationTypeEnum.CERTIFICATION_PASS.getCode(),
                NotificationTypeEnum.CERTIFICATION_UNPASS.getCode(),
                NotificationTypeEnum.REPORT_SUCCESS.getCode()
        ));
        param.setTargetUserId(userId);
        NotificationModel notificationModel = notificationService.getNewestSystemNotice(param);
//        log.info("系统通知信息11是:{}", notificationModel);
        if (Objects.isNull(notificationModel)) {
            return Result.ok("终于等到你，在友尼邦记录自己");
        }
        return Result.ok(notificationModel.getContent());
    }

    /**
     * 更新为已读
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/updateAlreadyReadViolationNotification")
    public Result<Void> updateAlreadyReadViolationNotification(@UserInfo UserModel userModel) {
        if (AccountStatusEnum.NO_EDIT.equals(userModel.getAccountStatus())) {
            return Result.error(ErrorCodeEnum.EDIT_NOT_ALLOW.getErrorMsg());
        }

        notificationService.updateAlreadyReadViolationNotification(userModel.getUserId());
        return Result.ok();
    }


    /**
     * 删除通知消息
     *
     * @param notificationId 通知id
     * @return
     */
    @NeedLogin
    @GetMapping("/deleteNotice")
//    @ApiOperation("删除通知消息")
    public Result<Void> deleteNotice(@RequestParam(name = "notificationId") Long notificationId,
                                     @UserInfo UserModel userModel) {
        //删除消息的界面肯定只有自己知道，所以不需要userId这个筛选条件
        notificationService.deleteById(notificationId);
        return Result.ok();
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    @NeedLogin
    @GetMapping("/getUnreadCount")
//    @ApiOperation("删除通知消息")
    public Result<List<Integer>> getUnreadCount(@UserInfo UserModel userModel) {
        //删除消息的界面肯定只有自己知道，所以不需要userId这个筛选条件
        List<Integer> unreadCountList = notificationService.getUnreadCount(userModel.getUserId());
        return Result.ok(unreadCountList);
    }

    @NeedLogin
    @GetMapping("/getSubscribeTimes")
//    @ApiOperation("删除通知消息")
    public Result<Map<String, Integer>> getSubscribeTimes(@UserInfo UserModel userModel) {
//        return Result.ok(null);

        List<WxMsgSubscribeRecordModel> recordList = wxMsgSubscribeService.query(userModel.getUserId());
        Map<String, Integer> map = new HashMap<>();
        Optional.ofNullable(recordList).orElse(Collections.emptyList())
                .forEach(recordModel -> {
                    WxMsgTemplateTypeEnum type = WxMsgTemplateTypeEnum.getByTemplateId(recordModel.getTemplateId());
                    map.put(type.name(), recordModel.getLeftTimes());
                });
        return Result.ok(map);
    }
}
