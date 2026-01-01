package com.fmx.xiaomeng.common.error;

/**
 * Created by hzllb on 2018/11/13.
 */
@Deprecated
public interface CommonError {
    public int getCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);


}
