package com.fmx.xiaomeng.common.service;

import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/


public interface WxInterfaceOperateService {

    Boolean checkMessage(String content, String openId) throws WxErrorException;


   String checkImage(String url, String openId) throws WxErrorException;
}
