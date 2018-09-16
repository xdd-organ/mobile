package com.java.mobile.phone.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.security.WxRemoteService;
import com.java.mobile.common.utils.KeyValueUtil;
import com.java.mobile.common.utils.XmlUtils;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.pay.constant.PayConstant;
import com.java.mobile.phone.pay.service.WeixinPayService;
import com.java.mobile.phone.pay.service.impl.WeixinPayServiceImpl;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author xdd
 * @date 2018/8/21
 */
@RestController
@RequestMapping("pay/wx")
@CrossOrigin
public class WeixinPayController {

    private static final Logger logger = LoggerFactory.getLogger(WeixinPayController.class);

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Autowired
    private WxRemoteService wxRemoteService;

    private final static String SUCCESS = "SUCCESS";

    @Value("${appid:}")
    private String appid;
    @Value("${appSecret:}")
    private String appSecret;

    @RequestMapping("prepay")
    public Result prepay(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("微信预支付参数：{}", JSONObject.toJSONString(params));
        Map<String, String> prepay = weixinPayService.prepay(params);
        logger.info("微信预支付返回：{}", JSONObject.toJSONString(prepay));
        return new Result<>(100, prepay);
    }

    @RequestMapping("generatePayParams")
    public Result generatePayParams(@RequestBody Map<String, String> params) {
        logger.info("微信生成支付参数：{}", JSONObject.toJSONString(params));
        Map<String, String> prepay = weixinPayService.generatePayParams(params);
        logger.info("微信生成支付返回：{}", JSONObject.toJSONString(prepay));
        return new Result<>(100, prepay);
    }

    @RequestMapping("generatePayAndUnLockParams")
    public Result generatePayAndUnLockParams(@RequestBody Map<String, String> params) {
        logger.info("微信生成支付参数：{}", JSONObject.toJSONString(params));
        params.put("notify_url", WeixinPayServiceImpl.payAndUnLockNotifyUrl);
        Map<String, String> prepay = weixinPayService.generatePayParams(params);
        logger.info("微信生成支付返回：{}", JSONObject.toJSONString(prepay));
        return new Result<>(100, prepay);
    }

    /**
     * 结果通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        String return_code = "FAIL";
        String return_msg = "error";
        String xmlStr = this.getRequestParams(request);
        logger.info("收到微信异步通知：[{}]", xmlStr);
        String returnCode = XmlUtils.getNodeValueFromXml("return_code", xmlStr);
        if (SUCCESS.equals(returnCode.toUpperCase())) {
            if (this.verify(xmlStr)) {
                try {
                    Map<String, String> params = XmlUtils.xmlStrToMap(xmlStr);
                    weixinPayService.payNotify(params);
                    return_code = SUCCESS;
                    return_msg = "OK";
                } catch (Exception e) {
                    logger.error("异常：" + e.getMessage(), e);
                }
            }
        }
        String rtnMsg = "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA["
                + return_msg + "]]></return_msg></xml>";
        logger.info("返回微信通知结果：[{}]", rtnMsg);
        return rtnMsg;
    }

    /**
     * 结果通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payAndUnLockNotifyUrl")
    public String payAndUnLockNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        String return_code = "FAIL";
        String return_msg = "error";
        String xmlStr = this.getRequestParams(request);
        logger.info("收到微信异步通知：[{}]", xmlStr);
        String returnCode = XmlUtils.getNodeValueFromXml("return_code", xmlStr);
        if (SUCCESS.equals(returnCode.toUpperCase())) {
            if (this.verify(xmlStr)) {
                try {
                    Map<String, String> params = XmlUtils.xmlStrToMap(xmlStr);
                    weixinPayService.payAndUnLockNotifyUrl(params);
                    return_code = SUCCESS;
                    return_msg = "OK";
                } catch (Exception e) {
                    logger.error("异常：" + e.getMessage(), e);
                }
            }
        }
        String rtnMsg = "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA["
                + return_msg + "]]></return_msg></xml>";
        logger.info("返回微信通知结果：[{}]", rtnMsg);
        return rtnMsg;
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
            logger.error("异常：" + e.getMessage(), e);
            return false;
        }

        return flag;
    }

    /**
     * 从request中获取请求字符串(xml字符串)
     *
     * @param request
     * @return
     */
    private String getRequestParams(HttpServletRequest request) {
        String str = "";
        BufferedReader br = null;
        try {
            request.setCharacterEncoding("utf-8");
            InputStream inStream = request.getInputStream();
            br = new BufferedReader(new InputStreamReader(inStream));
            String s = null;
            while ((s = br.readLine()) != null) {
                str += s;
            }
        } catch (Exception e) {
            logger.error("解析报文过程失败" + e.getMessage(), e);
        } finally {

            if (br != null){

                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return str;
    }


    /**
     * 1.wx.login()获取code (有效期5分钟)
     * 2.登录凭证校验获取session_key  通过js_code(登录获取的code),appid,secret(小程序的 app secret)，grant_type=authorization_code
     *      https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
     *
     * 3.wx.getUserInfo()获取用户信息
     * 4.解密encryptedData字段数据需要用上第二步获取的session_key，获取用户openId与unionId
     *
     */
    @RequestMapping("getWeixinUserInfo")
    public Result getWeixinUserInfo(@RequestBody Map<String, Object> params2) {
        logger.info("获取微信用户信息参数：{}", JSONObject.toJSONString(params2));
        String code = params2.get("code").toString();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            logger.info("发送到微信获取openid参数：{}", url);
            HttpResult httpResult = httpClientUtil.doGet(url, null, null);
            String body = httpResult.getBody();
            logger.info("调用接口jscode2session，返回code：{}：{}", httpResult.getCode(),body);
            if (body != null && body.contains(PayConstant.OPENID)) {
                Map<String, Object> params = JSONObject.parseObject(body, Map.class);
                Map<String, Object> ret = weixinPayService.getWeixinUserInfo(params);
                logger.info("获取微信用户信息返回：{}", JSONObject.toJSONString(ret));
                return new Result(100, ret);
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return new Result(500);
    }



}
