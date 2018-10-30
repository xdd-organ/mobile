package com.java.mobile.phone.jms.service;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.jms.MqServiceImpl;
import com.java.mobile.common.sms.AliSmsService;
import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("lockBeforeSmsService")
public class LockBeforeSmsService extends MqServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockBeforeSmsService.class);

    @Autowired
    private AliSmsService aliSmsService;
    @Autowired
    private LockOrderMapper lockOrderMapper;

    @Override
    public void doService(String jsonStr) {
        try {
            LOGGER.info("接收到关锁前15分钟发送短息参数：{}", jsonStr);
            Map<String, Object> params = JSONObject.parseObject(jsonStr, Map.class);
            Map<String, Object> lockParams = new HashMap<>();
            lockParams.put("id", params.get("id"));
            Map<String, Object> lockOrder = lockOrderMapper.getByLockOrder(lockParams);
            LOGGER.info("根据id查询到的订单结果：{}", lockOrder);
            if (lockOrder != null && lockOrder.get("end_time") == null) {
                aliSmsService.sendSms(String.valueOf(params.get("telphone")), "SMS_149390463", null);
                LOGGER.info("关锁前15分钟发送短息成功");
            }
        } catch (Exception e) {
            LOGGER.error("关锁前15分钟发送短息异常：" + e.getMessage(), e);
        }
    }
}

