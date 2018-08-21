package com.java.mobile.phone.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.utils.FtlTemplateEngine;
import com.java.mobile.common.utils.XmlUtils;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.phone.pay.bean.WxPayInfoBean;
import com.java.mobile.phone.pay.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author xdd
 * @date 2018/8/21
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService{

    private Logger logger = LoggerFactory.getLogger(WeixinPayServiceImpl.class);

    @Autowired
    private FtlTemplateEngine ftlTemplateEngine;

    @Autowired
    private com.java.mobile.common.security.WxRemoteService WxRemoteService;

    @Autowired
    private HttpClientUtil httpClientUtil;

    private final static String prepayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    @Override
    public Map<String, String> prepay(Map<String, String> params) {
        WxPayInfoBean wxPayInfoBean = new WxPayInfoBean();
        try {
            String reqXml = ftlTemplateEngine.genMessage("wx_pay_request.ftl", wxPayInfoBean);
            String sign = getSign(wxPayInfoBean);
            reqXml = XmlUtils.replaceNodeContent("<sign>", "</sign>", sign, reqXml);
            logger.info(reqXml);
            HttpResult httpResult = httpClientUtil.doPost(prepayUrl, reqXml, null);
            logger.info(JSONObject.toJSONString(httpResult));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //1.跳转到用户授权  appid=APPID（公众号唯一标识）
//        https://open.weixin.qq.com/connect/oauth2/authorize
//        String authforOpenIdUrl = authUrl + "?appid=" + orderInfoBean.getWeixinAppId() +
//                "&redirect_uri=" + redirectUriEncode +
//                "&response_type=code&scope=snsapi_base&state=json#wechat_redirect";
        //2.授权后跳转到用户地址地址，获取code与state
//        redirect_uri?code=xx
//        String code = request.getParameter("code");
//        String state = request.getParameter("state");

        //3.根据code获取openid   secret=SECRET（公众号的appsecret）  appid=APPID（公众号唯一标识）
//        https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//        String data = "appid=" + appId + "&secret=" + wxSecret + "&code="
//                + code + "&grant_type=authorization_code";

        //4.使用access_token获取用户信息
//        https://api.weixin.qq.com/sns/userinfo?access_token=access_token&openid=o47Fa0mp9SRTf3eiKmqWm69BjG_8&lang=zh_CN

















        return null;
    }


    private String getSign(WxPayInfoBean wxPayInfoBean) {

        Map<String, String> map = new TreeMap<String, String>();
        map.put("appid", wxPayInfoBean.getAppId());
        map.put("mch_id", wxPayInfoBean.getMchId());
        map.put("nonce_str", wxPayInfoBean.getNonceStr());
        map.put("body", wxPayInfoBean.getBody());
        map.put("attach", wxPayInfoBean.getAttach());
        map.put("out_trade_no", wxPayInfoBean.getOrderNo());
        map.put("total_fee", wxPayInfoBean.getTotalFee());
        map.put("spbill_create_ip", wxPayInfoBean.getSpbillCreateIp());
        map.put("notify_url", wxPayInfoBean.getNotifyUrl());
        map.put("trade_type", wxPayInfoBean.getTradeType());
//        map.put("auth_code", wxPayInfoBean.getAuthCode());
//        map.put("limit_pay", wxPayInfoBean.getLimitPay());
//        map.put("detail", wxPayInfoBean.getDetail());
        if (!StringUtils.isEmpty(wxPayInfoBean.getTradeType())) {
            map.put("product_id", wxPayInfoBean.getPrepayId());
        }
        if (!StringUtils.isEmpty(wxPayInfoBean.getOpenId())) {
            map.put("openid", wxPayInfoBean.getOpenId());
        }

        if(StringUtils.isNotBlank(wxPayInfoBean.getTimeExpire())){
            map.put("time_expire", wxPayInfoBean.getTimeExpire());
        }
//        map.put("scene_info", wxPayInfoBean.getSceneInfo());
        String sign = WxRemoteService.signMd5ByMap(wxPayInfoBean.getMchId(), map);
        return sign;
    }

}
