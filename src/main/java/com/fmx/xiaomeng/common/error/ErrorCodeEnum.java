package com.fmx.xiaomeng.common.error;

import com.fmx.xiaomeng.common.log.LogCode;

/**
 * Created by hzllb on 2018/11/13.
 */
public enum ErrorCodeEnum implements LogCode, ErrorCode {
    //1 开头是业务异常
    PARAMETER_VALIDATION_ERROR("10001","参数不合法"),

    UNKNOWN_ERROR("10002","未知错误"),

    CONTENT_VIOLATION("10003","内容违规"),

    USER_NOT_EXIST("10004","用户不存在"),

    GET_BG_CONFIG_ERROR("10006","获取后台配置信息错误"),
    BG_CONFIG_ALREADY_EXIST("10007","该配置名已存在"),



    EDU_SYS_NOT_LOGIN_IN("10008","教务系统未登录"),

    USER_NAME_OR_PASSWORD_ERROR("10009","教务系统用户名或密码错误"),




    CREATE_ARTICLE_ERROR("10010","创建文章异常"),
    THUMB_UP_ARTICLE_ERROR("10011","点赞文章异常"),
    COLLECT_ARTICLE_ERROR("10012","收藏文章异常"),
    DING_ARTICLE_ERROR("10013","收藏文章异常"),



    CREATE_COMMENT_ERROR("10014","创建评论异常"),
    THUMB_UP_COMMENT_ERROR("10015","点赞评论异常"),


    ADD_SCORE_ERROR("10030","加积分异常"),
    UPDATE_ARTICLE_ERROR("10031","更新帖子异常"),



    //    这个不算异常不用抛出吧
//    FULL_ORGANIZATION("10032","组局人数已满"),
//    BOY_FULL_ORGANIZATION("10033","男生人数已满"),
//    GIRL_FULL_ORGANIZATION("10034","女生人数已满"),



//    ORGANIZATION_DISSOLUTION("10041","组局已解散"),
    //    这个不算异常不用抛出吧
//    IN_ORGANIZATION("10042","已经在组局中"),


    QUERY_TERM_START_TIME_ERROR("10050","查询学期开始时间异常"),
    ES_DELETE_ARTICLE_ERROR("10051","es删除帖子失败"),
//    ES_DELETE_ORGANIZATION_ERROR("10052","es删除组局失败"),


    ORIGIN_PASSWORD_WRONG("10060","原密码错误"),
    COURSE_CONFLICT("10061","与其他课程冲突"),

    UPDATE_SUBSCRIBE_RECORD_ERROR("10071","更新订阅记录异常"),
    MERCHANT_PHONE_IS_NULL("10072","手机号为空"),
    MERCHANT_IS_EXIST("10073","商家已存在"),




    JWXT_LOGIN_ERROR("10078","教务系统登录失败"),

    NEW_TERM_WEEK_NOT_UPDATE("10079","新学期还没开始"),


    QUERY_IDLE_CLASS_NOT_IN_TERM("10080","日期不在教学周期,无空教室信息"),

    UPDATE_COMMENT_ERROR("10088","更新评论异常"),









    //2 开头是系统异常
    HTTP_REQUEST_ERROR("20001","http请求报错"),

    OK_BUT_MORE("20002", "已经登陆多台设备,可能存在被盗风险,请及时修改密码"),

    NO_AUTH("20003", "无权限"),


    DISTRIBUTE_LOCK_FAILED("20004","分布式锁加锁失败"),


    BODY_NOT_MATCH("20005", "请求的数据格式不正确"),

    SIGNATURE_NOT_MATCH("20006", "请求的数字签名不匹配"),

    SIGNATURE_NOT_ALLOW("20007", "没有权限"),

    NOT_LOGIN("20008", "未登录"),

    GENERATE_TOKEN_FAIL("20009", "生成Token失败"),

    UNAUTHED("20010", "未认证"),


    EDIT_NOT_ALLOW("20011", "没有编辑权限"),
    VIEW_NOT_ALLOW("20012", "没有浏览权限"),



    OPS_TOO_MUCH("20020","操作太频繁"),

    USER_BLACKED("20021","您已被对方拉黑"),

    ;

    ErrorCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }


    private String code;
    private String message;


    @Override
    public String getErrorCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.message;
    }


    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
