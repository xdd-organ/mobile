package com.java.mbkh.phone.jsm.service;

import com.java.common.jms.MqServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("messageService")
public class MessageService extends MqServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Override
    public void doService(String jsonStr) {
        LOGGER.info("接收到tcp服务消息：{}", jsonStr);
    }
}
