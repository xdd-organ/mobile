package com.java.mobile.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/23
 */
@Service
public class AliSmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliSmsService.class);

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    private static final String OK = "OK";
    private static final String templateCode = "SMS_142590006";//短信模板ID

    @Value("${accessKeyId:}")
    private String accessKeyId;
    @Value("${accessKeySecret:}")
    private String accessKeySecret;
    @Value("${signName:梦宝康护}")//短信签名值
    private String signName;

    private IAcsClient acsClient;

    public void sendVerifyCode(String phoneNumbers, String code) {
        HashMap<String, String> templateParam = new HashMap<>();
        templateParam.put("code", code);
        this.sendSms(phoneNumbers, templateCode, templateParam);
    }

    public void sendSms(String phoneNumbers, String templateCode, Map<String, String> templateParam) {
        this.sendSms(phoneNumbers, templateCode, signName, templateParam);
    }

    public void sendSms(String phoneNumbers, String templateCode, String signName, Map<String, String> templateParam) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//必填:待发送手机号，多个可逗号分隔
        request.setSignName(signName);//必填:短信签名
        request.setTemplateCode(templateCode);//必填:短信模板
        if (!CollectionUtils.isEmpty(templateParam))request.setTemplateParam(JSONObject.toJSONString(templateParam));
        this.sendSms(request);
    }

    private void sendSms(final SendSmsRequest request) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOGGER.info("发送短信请求内容：{}", JSONObject.toJSONString(request));
                        SendSmsResponse response = acsClient.getAcsResponse(request);
                        LOGGER.info("接收短信响应：{}", JSONObject.toJSONString(response));
                        if (OK.equalsIgnoreCase(response.getCode())) {
                            LOGGER.error("短信发送成功");
                        } else {
                            LOGGER.warn("短信发送失败");
                        }
                    } catch (Exception e) {
                        LOGGER.error("发送短信异常：" + e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            LOGGER.error("发送短信异常：" + e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init() {
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
        } catch (Exception e) {
            LOGGER.error("初始化短信发送类失败：" + e.getMessage(), e);
        }
    }
}
