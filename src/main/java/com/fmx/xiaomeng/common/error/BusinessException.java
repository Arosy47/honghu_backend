package com.fmx.xiaomeng.common.error;

/**
 * Created by hzllb on 2018/11/13.
 */
//包装器业务异常类实现
@Deprecated
public class BusinessException extends RuntimeException implements CommonError {

    private CommonError commonError;

//    private String msg;
//    private int code = 500;

    //直接接收EmBusinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super(commonError.getErrMsg());
        this.commonError = commonError;
    }

    //接收自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg){
        super(errMsg);
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    public BusinessException(CommonError commonError, String errMsg, Throwable e) {
        super(errMsg, e);
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
//        this.msg = msg;
//        this.code = code;
    }




    @Override
    public int getCode() {
        return this.commonError.getCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }

    public CommonError getCommonError() {
        return commonError;
    }
}
