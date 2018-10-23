package com.java.mobile.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.service.RedisService;
import com.java.mobile.common.sms.AliSmsService;
import com.java.mobile.common.utils.SerialNumber;
import com.java.mobile.common.vo.Result;
import com.java.mobile.common.weixin.AES2;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.apache.catalina.util.HexUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.crypto.Aes128;

import java.util.Arrays;
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
    @Autowired
    private LockInfoService lockInfoService;

    private final String DEFAULT_LOCK_KEY = "32874782547563714880658817994543";

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
    String key = "3A60432A5C01211F291E0F4E0C132825";

    @RequestMapping("encrypt")
    public Result encrypt(@RequestBody Map<String, String> params) {
        LOGGER.info("加密参数:{}", JSONObject.toJSONString(params));
        String src = params.get("encrypt");
        byte[] convert = AES2.toBytes(src);
        String lockKey = lockInfoService.getLockKey(String.valueOf(params.get("qr_code_no")));
        byte[] res = StringUtils.isBlank(lockKey) ? AES2.key : AES2.toBytes2(lockKey);
        byte[] decrypt = AES2.encrypt(convert, res);
        String s = AES2.bytesToHexFun1(decrypt);
        LOGGER.info("加密返回:{}", s);
        return new Result<>(100, s);
    }

    @RequestMapping("decrypt")
    public Result decrypt(@RequestBody Map<String, String> params) {
        LOGGER.info("解密参数:{}", JSONObject.toJSONString(params));
        String src = params.get("decrypt");
        byte[] convert = AES2.toBytes(src);
        String lockKey = lockInfoService.getLockKey(String.valueOf(params.get("qr_code_no")));
        byte[] res = StringUtils.isBlank(lockKey) ? AES2.key : AES2.toBytes2(lockKey);
        byte[] decrypt = AES2.decrypt(convert, res);
        String s = AES2.bytesToHexFun1(decrypt);
        LOGGER.info("解密返回:{}", s);
        return new Result<>(100, s);
    }

}
