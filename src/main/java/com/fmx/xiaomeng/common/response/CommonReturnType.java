package com.fmx.xiaomeng.common.response;

/**
 * Created by hzllb on 2018/11/13.
 */
public class CommonReturnType<T> {
//    //表明对应请求的返回处理结果 "ok" 或 "fail"
//    private String status;
//
//    //若status=ok,则data内返回前端需要的json数据
//    //若status=fail，则data内使用通用的错误码格式
//    private T data;
//
//    //定义一个通用的创建方法
//    public static CommonReturnType create(T result){
//        return CommonReturnType.create(result,"ok");
//    }
//
//    public static CommonReturnType create(T result,String status){
//        CommonReturnType type = new CommonReturnType();
//        type.setStatus(status);
//        type.setData(result);
//        return type;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
}
