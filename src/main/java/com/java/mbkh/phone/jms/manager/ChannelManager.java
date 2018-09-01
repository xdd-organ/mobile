package com.java.mbkh.phone.jms.manager;

import com.java.common.redis.service.RedisService;
import com.java.mbkh.phone.jms.bean.TcpServerBean;
import com.java.mbkh.phone.jms.constant.ServerConstant;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ChannelManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelManager.class);
    @Autowired
    private RedisService redisService;

    public int registerChannel(String channelId, String uid) {
        redisService.hset(ServerConstant.CHANNEL_UID_MAP, uid, channelId);
        List<byte[]> hmget = redisService.hmget(ServerConstant.CHANNEL_MAP.getBytes(), channelId.getBytes());
        if (!CollectionUtils.isEmpty(hmget)) {
            TcpServerBean tcpServerBean = SerializationUtils.deserialize(hmget.get(0));
            tcpServerBean.setUid(uid);
            redisService.hset(ServerConstant.CHANNEL_MAP.getBytes(), channelId.getBytes(), SerializationUtils.serialize(tcpServerBean));
            return 1;
        }
        return -1;
    }


}
