package com.java.mobile.phone.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.pay.constant.PayConstant;
import com.java.mobile.phone.pay.service.WeixinPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
@RestController
@RequestMapping("pay/wx")
public class WeixinPayController {

    private static final Logger logger = LoggerFactory.getLogger(WeixinPayController.class);

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private HttpClientUtil httpClientUtil;

    @Value("${appid}")
    private String appid;
    @Value("${appSecret}")
    private String appSecret;




    @RequestMapping("prepay")
    public String prepay(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> prepay = weixinPayService.prepay(params);








        return null;
    }
    @RequestMapping("payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        String requestParams = this.getRequestParams(request);




        return null;
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
    @RequestMapping("getWeixinUserInfo/{code}")
    public Result getWeixinUserInfo(@PathVariable("code") String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            HttpResult httpResult = httpClientUtil.doGet(url, null, null);
            String body = httpResult.getBody();
            logger.info("调用接口jscode2session，返回：", body);
            if (body != null && body.contains(PayConstant.OPENID)) {
                Map<String, Object> params = JSONObject.parseObject(body, Map.class);
                Map<String, Object> ret = weixinPayService.getWeixinUserInfo(params);
                return new Result(500, ret);
            } else {
                return new Result(500);
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return new Result(500);
    }



}
