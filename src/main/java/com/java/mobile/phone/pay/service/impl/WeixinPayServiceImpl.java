package com.java.mobile.phone.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.security.WxRemoteService;
import com.java.mobile.common.utils.*;
import com.java.mobile.common.utils.httpclient.CustomHttpRequestRetryHandler;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.pay.bean.WxPayInfoBean;
import com.java.mobile.phone.pay.constant.PayConstant;
import com.java.mobile.phone.pay.mapper.WxPayInfoMapper;
import com.java.mobile.phone.pay.service.WeixinPayService;
import com.java.mobile.phone.user.mapper.TransFlowInfoMapper;
import com.java.mobile.phone.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpMessage;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.*;

/**
 * @author xdd
 * @date 2018/8/21
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService{

    private static final Logger logger = LoggerFactory.getLogger(WeixinPayServiceImpl.class);

    @Autowired
    private FtlTemplateEngine ftlTemplateEngine;

    @Autowired
    private WxRemoteService wxRemoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private WxPayInfoMapper wxPayInfoMapper;
    @Autowired
    private TransFlowInfoMapper transFlowInfoMapper;
    @Autowired
    private LockOrderService lockOrderService;
    private String filePath;
    private String pwd;

    @Autowired
    private HttpClientUtil httpClientUtil;

    private final static String prepayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private final static String queryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";
    private final static String payNotifyUrl = "https://www.chmbkh.com/mobile/pay/wx/payNotify";
    public final static String payAndUnLockNotifyUrl = "https://www.chmbkh.com/mobile/pay/wx/payAndUnLockNotifyUrl";
    private final static String SUCCESS = "SUCCESS";

    @Value("${appid:}")
    private String appid;
    @Value("${mchId:}")
    private String mchId;
    private String spbillCreateIp = IPUtil.getIp();

    @Transactional
    @Override
    public Map<String, String> prepay(Map<String, String> params) {
        Map<String, String> resp = new HashMap<>();
        try {
            WxPayInfoBean wxPayInfoBean = this.combineWxPayInfoBean(params);
            String reqXml = ftlTemplateEngine.genMessage("wx_pay_request.ftl", wxPayInfoBean);
            String sign = this.getSign(wxPayInfoBean);
            reqXml = XmlUtils.replaceNodeContent("<sign>", "</sign>", sign, reqXml);
            logger.info("微信预支付参数：{}", reqXml);
            HttpResult httpResult = httpClientUtil.doPost(prepayUrl, reqXml, null);
            logger.info("微信预支付返回：{}", JSONObject.toJSONString(httpResult));
            String rspXml = httpResult.getBody();
            String returnCode = XmlUtils.getNodeValueFromXml("<return_code>", "</return_code>", rspXml);
            if (SUCCESS.equalsIgnoreCase(returnCode)) {
                if (this.verify(rspXml)) {
                    Map<String, String> weixinRsp = XmlUtils.xmlStrToMap(rspXml);
                    resp.put("prepay_id", weixinRsp.get("prepay_id"));
                }
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
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

        //appid=wxb3bfac02eb55371e
        //3.根据code获取openid   secret=SECRET（公众号的appsecret）  appid=APPID（公众号唯一标识）
//        https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//        String data = "appid=" + appId + "&secret=" + wxSecret + "&code="
//                + code + "&grant_type=authorization_code";

        //4.使用access_token获取用户信息
//        https://api.weixin.qq.com/sns/userinfo?access_token=access_token&openid=o47Fa0mp9SRTf3eiKmqWm69BjG_8&lang=zh_CN
        logger.info("发送微信预支付返回：{}", resp);
        return resp;
    }

    private WxPayInfoBean combineWxPayInfoBean(Map<String, String> params) {
        String randomNum = SerialNumber.getRandomNum(32);
        WxPayInfoBean wxPayInfoBean = new WxPayInfoBean();
        wxPayInfoBean.setAppId(appid);//微信分配的小程序ID
        wxPayInfoBean.setMchId(mchId);//微信支付分配的商户号
        wxPayInfoBean.setNonceStr(randomNum);//随机字符串
        wxPayInfoBean.setBody(params.get("body"));//商品简单描述
        wxPayInfoBean.setAttach(params.get("attach"));//支付通知中原样返回
        wxPayInfoBean.setOrderNo(randomNum); //out_trade_no 商户系统内部订单号
        wxPayInfoBean.setTotalFee(params.get("total_fee"));//订单总金额
        wxPayInfoBean.setSpbillCreateIp(spbillCreateIp);//微信支付API的机器IP
        wxPayInfoBean.setNotifyUrl(StringUtils.isNotBlank(params.get("notify_url")) ? params.get("notify_url") : payNotifyUrl);//支付结果通知
        wxPayInfoBean.setTradeType("JSAPI");
        wxPayInfoBean.setOpenId(params.get("openid"));//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识


        Map<String, Object> payParams = new HashMap<>();
        payParams.putAll(params);
        payParams.put("appid", appid);
        payParams.put("mch_id", mchId);
        payParams.put("nonce_str", randomNum);
        payParams.put("out_trade_no", randomNum);
        payParams.put("spbill_create_ip", spbillCreateIp);
        payParams.put("notify_url", payNotifyUrl);
        payParams.put("trade_type", "JSAPI");
        logger.info("微信支付参数入库：{}", JSONObject.toJSONString(payParams));
        wxPayInfoMapper.insert(payParams);
        logger.info("组装wxPayInfoBean：{}", JSONObject.toJSONString(wxPayInfoBean));
        return wxPayInfoBean;
    }

    private boolean verify(String xml){
        boolean flag = false;
        try {
            SortedMap<String, String> map = XmlUtils.xmlStrToMap(xml);
            String sign = map.get("sign");
            map.remove("sign");
            String keyValue = KeyValueUtil.mapToString(map);
            flag = wxRemoteService.verifyMd5(map.get("mch_id"), keyValue, sign);
        } catch (DocumentException e) {
            return false;
        }

        return flag;
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
        if (!StringUtils.isEmpty(wxPayInfoBean.getProductId())) {
            map.put("product_id", wxPayInfoBean.getProductId());
        }
        if (!StringUtils.isEmpty(wxPayInfoBean.getOpenId())) {
            map.put("openid", wxPayInfoBean.getOpenId());
        }

        if(StringUtils.isNotBlank(wxPayInfoBean.getTimeExpire())){
            map.put("time_expire", wxPayInfoBean.getTimeExpire());
        }
//        map.put("scene_info", wxPayInfoBean.getSceneInfo());
        String sign = wxRemoteService.signMd5ByMap(wxPayInfoBean.getMchId(), map);
        return sign;
    }

    @Override
    @Transactional
    public Map<String, Object> getWeixinUserInfo(Map<String, Object> params) {
        Map<String, Object> user = userService.getByPrimaryKey(params);
        if (user == null) {
            params.put("nickname", params.get(PayConstant.OPENID).toString().substring(10));
            params.put("is_disable", "0");
            return userService.insert(params);
        } else  {
            userService.updateByOpenid(params);
        }
        return user;
    }

    @Override
    @Transactional
    public int payNotify(Map<String, String> params) {
        String orderNo = params.get("out_trade_no");
        Map<String, Object> order = wxPayInfoMapper.getByOrderNo(orderNo);
        logger.info("查询订单结果：{}", JSONObject.toJSONString(order));
        if (!CollectionUtils.isEmpty(order)) {
            String result = String.valueOf(order.get("result"));
            if (!"SUCCESS".equalsIgnoreCase(result)) {
                Map<String, Object> params2 = new HashMap<>();
                params2.putAll(params);

                //更新订单状态
                logger.info("更新订单参数：{}", JSONObject.toJSONString(params));
                int i = wxPayInfoMapper.updateByOrderNo(params2);
                logger.info("更新订单结果：{}", i);

                //更新流水与用户余额
                this.saveInfo(params, order);
            }
        } else {
            logger.error("订单不存在");
        }
        return 1;
    }

    private void saveInfo(Map<String, String> params, Map<String, Object> order) {
        try {
            String attach = params.get("attach");
            SortedMap<String, String> attachMap = KeyValueUtil.keyValueStringToMap(attach);
            String type = attachMap.get("type");
            String userId = order.get("user_id").toString();
            String totalFee = params.get("total_fee");

            //插入流水
            Map<String, Object> flowPrams = new HashMap<>();
            flowPrams.put("type", type);
            flowPrams.put("fee", totalFee);
            flowPrams.put("user_id", userId);
            flowPrams.put("status", "0");
            flowPrams.put("insert_author", userId);
            flowPrams.put("update_time", userId);
            logger.info("插入充值流水参数：{}", JSONObject.toJSONString(flowPrams));
            transFlowInfoMapper.insert(flowPrams);

            //更新未支付的订单金额
            this.updateDiffFee(totalFee, userId);

            //更新用户余额/押金
            logger.info("更新余额/押金参数：userId:{}，totalFee:{},type:{}", userId, totalFee,type);
            if ("4".equals(type)) {
                userService.updateMoney(userId, Integer.valueOf(totalFee));
            } else if ("2".equals(type)) {
                userService.updateDeposit(userId, Integer.valueOf(totalFee));
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
    }

    private void updateDiffFee(String totalFee, String userId) {
        List<Map<String, Object>> list = lockOrderService.unPayLockOrder(userId);
        if (!CollectionUtils.isEmpty(list)) {
            int diffFee = Integer.valueOf(list.get(0).get("diff_fee").toString()).intValue();
            int diffFeeInt = Integer.valueOf(totalFee).intValue();
            int fee = diffFee + diffFeeInt;

            Map<String, Object> feeParams = new HashMap<>();
            if (fee >= 0) {
                feeParams.put("diff_fee", "0");
            } else {
                feeParams.put("diff_fee", fee);
            }
            feeParams.put("id", list.get(0).get("id"));
            lockOrderService.update(feeParams);
        }
    }

    @Transactional
    @Override
    public Map<String, Object> query(Map<String, String> params) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Map<String, String> wxPayInfoBean = this.combineWxQueryParams(params);
            String reqXml = ftlTemplateEngine.genMessage("wx_query_message_request.ftl", wxPayInfoBean);
            logger.info("微信订单查询参数：{}", reqXml);
            HttpResult httpResult = httpClientUtil.doPost(queryUrl, reqXml, null);
            logger.info("微信订单查询返回：{}", JSONObject.toJSONString(httpResult));
            String rspXml = httpResult.getBody();
            String returnCode = XmlUtils.getNodeValueFromXml("<return_code>", "</return_code>", rspXml);
            if (SUCCESS.equalsIgnoreCase(returnCode)) {
                if (this.verify(rspXml)) {
                    Map<String, String> weixinRsp = XmlUtils.xmlStrToMap(rspXml);
                    resp.put("trade_state", weixinRsp.get("trade_state"));
                }
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        logger.info("发送微信订单查询返回：{}", resp);
        return resp;
    }

    private Map<String,String> combineWxQueryParams(Map<String, String> params) {
        Map<String, String> reqData = new TreeMap<String, String>();
        reqData.put("appid", appid);
        reqData.put("mch_id", mchId);
        reqData.put("transaction_id", params.get("transaction_id"));
        reqData.put("out_trade_no", params.get("out_trade_no"));
        reqData.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        Iterator<String> it = reqData.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (StringUtils.isBlank(reqData.get(key))) {
                it.remove();
            }
        }
        String sign = this.wxRemoteService.signMd5ByMap(mchId, reqData);
        reqData.put("sign", sign);
        return reqData;
    }

    @Override
    public Map<String, String> generatePayParams(Map<String, String> params) {
        Map<String, String> prepayRsp = this.prepay(params);
        if (!CollectionUtils.isEmpty(prepayRsp)) {
            return this.combineWxGeneratePayParams(prepayRsp);
        }
        return null;
    }

    private Map<String,String> combineWxGeneratePayParams(Map<String, String> params) {
        Map<String, String> reqData = new TreeMap<String, String>();
        reqData.put("appId", appid);
        reqData.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000 + (8 * 60 * 60)));
        reqData.put("package", "prepay_id=" + params.get("prepay_id"));
        reqData.put("signType", "MD5");
        reqData.put("nonceStr", UUID.randomUUID().toString().replace("-", ""));
        Iterator<String> it = reqData.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (StringUtils.isBlank(reqData.get(key))) {
                it.remove();
            }
        }
        String sign = this.wxRemoteService.signMd5ByMap(mchId, reqData);
        reqData.put("paySign", sign);
        reqData.remove("appId");
        return reqData;
    }

    @Override
    public int payAndUnLockNotifyUrl(Map<String, String> params) {
        String orderNo = params.get("out_trade_no");
        Map<String, Object> order = wxPayInfoMapper.getByOrderNo(orderNo);
        logger.info("查询订单结果：{}", JSONObject.toJSONString(order));
        if (!CollectionUtils.isEmpty(order)) {
            String result = String.valueOf(order.get("result"));
            if (!"SUCCESS".equalsIgnoreCase(result)) {
                Map<String, Object> params2 = new HashMap<>();
                params2.putAll(params);

                //更新订单状态
                logger.info("更新订单参数：{}", JSONObject.toJSONString(params));
                int i = wxPayInfoMapper.updateByOrderNo(params2);
                logger.info("更新订单结果：{}", i);

                //更新流水与用户余额
                this.saveInfo(params, order);

                //开锁
                this.unLock(params);
            }
        } else {
            logger.error("订单不存在");
        }
        return 1;
    }

    private int unLock(Map<String,String> params) {
        String attach = params.get("attach");
        SortedMap<String, String> attachMap = KeyValueUtil.keyValueStringToMap(attach);
        String type = attachMap.get("type");


        return 1;
    }



    @PostConstruct
    public void init () {
        HttpClientBuilder httpClientBuilder = this.initBuilder();
        this.httpClient = httpClientBuilder.build();
    }

    @Value("${maxTotal}")
    private int maxTotal;
    @Value("${defaultMaxPerRoute}")
    private int defaultMaxPerRoute;
    @Value("${validateAfterInactivity}")
    private int validateAfterInactivity;
    @Autowired
    private CustomHttpRequestRetryHandler retryHandler;
    @Autowired(required = false)
    private RequestConfig requestConfig;
    private CloseableHttpClient httpClient;

    public SSLContext createIgnoreVerifySSL(){
        try {
            //String path = this.getClass().getClassLoader().getResource("certificate").getPath().toString();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream inputStream = new FileInputStream(filePath);
            try {
                keyStore.load(inputStream, pwd.toCharArray());
            } finally {
                inputStream.close();
            }
            SSLContext sc = SSLContexts.custom().loadKeyMaterial(keyStore, pwd.toCharArray()).build();
            return sc;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public Registry<ConnectionSocketFactory> buildHttps(){
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(createIgnoreVerifySSL(), NoopHostnameVerifier.INSTANCE)).build();
        return socketFactoryRegistry;
    }

    public PoolingHttpClientConnectionManager initManager() {
        Registry<ConnectionSocketFactory> registry = this.buildHttps();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(maxTotal);
        manager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        manager.setValidateAfterInactivity(validateAfterInactivity);
        return manager;
    }

    public HttpClientBuilder initBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(this.initManager());
        httpClientBuilder.setRetryHandler(retryHandler);
        return httpClientBuilder;
    }

    /**
     * post请求(表单提交)
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPost(String url, String params, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        this.setRequestHeaders(headers, httpPost);
        httpPost.setConfig(this.requestConfig);
        if (StringUtils.isNotBlank(params)) {
            // 构造一个body实体
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(entity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    private void setRequestHeaders(Map<String, String> headers, HttpMessage httpMessage){
        if (null == httpMessage) return;
        if (null != headers && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpMessage.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


}
