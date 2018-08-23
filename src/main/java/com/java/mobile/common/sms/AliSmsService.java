package com.java.mobile.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.xml.internal.fastinfoset.util.ValueArrayResourceException;
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
    //短信签名值
    private static final String signName = "dysmsapi.aliyuncs.com";
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String templateCode = "templateCode";//短信模板ID

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${accessKeyId:}")
    private String accessKeyId;
    @Value("${accessKeySecret:}")
    private String accessKeySecret;

    private IAcsClient acsClient;

    public String sendVerifyCode(String phoneNumbers, String code) {
        HashMap<String, String> templateParam = new HashMap<>();
        templateParam.put("code", code);
        return sendMsg(phoneNumbers, templateCode, signName, templateParam);
    }

    public String sendMsg(String phoneNumbers, String templateCode, String signName, Map<String, String> templateParam) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);//必填:待发送手机号，多个可逗号分隔
        request.setSignName(signName);//必填:短信签名
        request.setTemplateCode(templateCode);//必填:短信模板
        if (!CollectionUtils.isEmpty(templateParam))request.setTemplateParam(JSONObject.toJSONString(templateParam));
        return sendMsg(request);
    }

    public String sendMsg(final SendSmsRequest request) {
        final String[] ret = new String[] {ERROR};
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOGGER.info("发送短信请求内容：{}", JSONObject.toJSONString(request));
                        SendSmsResponse response = acsClient.getAcsResponse(request);
                        LOGGER.info("接收短信响应：{}", JSONObject.toJSONString(response));
                        if (OK.equalsIgnoreCase(response.getCode())) {
                            ret[0] = OK;
                        }
                    } catch (Exception e) {
                        LOGGER.error("发送短信异常：" + e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("发送短信异常：" + e.getMessage(), e);
        }
        return ret[0];
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
            LOGGER.error("发送短信异常：" + e.getMessage(), e);
        }
    }
}
