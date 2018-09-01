package com.java.mbkh.phone.jms.service;

import com.alibaba.fastjson.JSONObject;
import com.java.common.jms.MqServiceImpl;
import com.java.mbkh.phone.jms.work.LockWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("messageService")
public class MessageService extends MqServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Resource(name = "lockWorkMap")
    private Map<String, LockWork> lockWorkMap;

    @Override
    public void doService(String jsonStr) {
        try {
            LOGGER.info("接收到tcp服务消息：{}", jsonStr);
            Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
            Map<String, String> msg = (Map<String, String>) map.get("msg");
            LockWork work = lockWorkMap.get(msg.get("TYPE"));
            work.doService(map.get("channelId").toString(), msg);
        } catch (Exception e) {
            LOGGER.error("处理tcp服务器消息异常：" + e.getMessage(), e);
        }
    }
}
