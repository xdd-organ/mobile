package com.java.mobile.phone.pay.service;

import java.util.Map; /**
 * @author xdd
 * @date 2018/8/21
 */
public interface WeixinPayService {

    Map<String, String> prepay(Map<String, String> params);
}
