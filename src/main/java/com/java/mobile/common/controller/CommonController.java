package com.java.mobile.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.service.RedisService;
import com.java.mobile.common.sms.AliSmsService;
import com.java.mobile.common.utils.SerialNumber;
import com.java.mobile.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/24
 */
@RestController
@CrossOrigin
@RequestMapping("anon/common")
public class CommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Value("${verifyCodeExpireTime:300}")
    private int verifyCodeExpireTime;

    @Autowired
    private AliSmsService aliSmsService;
    @Autowired(required = false)
    private RedisService redisService;

    @RequestMapping("sendVerifyCode")
    public Result sendVerifyCode(@RequestBody Map<String, String> params) {
        LOGGER.info("发送短信参数:{}", JSONObject.toJSONString(params));
        String templateCode = params.remove("templateCode");
        String randomKey = params.remove("randomKey");
        String phoneNumbers = params.remove("phoneNumbers");
        String randomValue = SerialNumber.generateRandomSerial(6);
        redisService.set(randomKey, randomValue, verifyCodeExpireTime);
        LOGGER.info("发送短信数据存入redis中，randomKey:{},randomValue:{}",randomKey, randomValue);
        params.put("code", randomValue);
        aliSmsService.sendSms(phoneNumbers, templateCode, params);
        return new Result<>(100);
    }

}