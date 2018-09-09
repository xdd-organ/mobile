package com.java.mobile.phone.pay.service;

import java.util.Map; /**
 * @author xdd
 * @date 2018/8/21
 */
public interface WeixinPayService {

    Map<String, String> prepay(Map<String, String> params);

    Map<String,Object> getWeixinUserInfo(Map<String, Object> params);

    int payNotify(Map<String, String> params);

    Map<String,Object> query(Map<String, String> params);

    Map<String,String> generatePayParams(Map<String, String> params);

    int payAndUnLockNotifyUrl(Map<String,String> params);
}
