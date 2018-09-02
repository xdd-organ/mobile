package com.java.mbkh.phone.jms.work.lock;

import com.java.common.jms.AdvancedGroupQueueSender;
import com.java.mbkh.phone.jms.constant.ServerConstant;
import com.java.mbkh.phone.jms.manager.ChannelManager;
import com.java.mbkh.phone.jms.vo.LockReturn;
import com.java.mbkh.phone.jms.work.LockWork;
import com.java.mbkh.phone.lock.constant.TcpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("registerLockWork")
public class RegisterLockWork implements LockWork {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterLockWork.class);

    @Resource(name = "messageSender")
    private AdvancedGroupQueueSender messageSender;
    @Autowired
    private ChannelManager channelManager;

    @Override
    public void doService(String channelId, Map<String, String> msg) {
        LOGGER.info("注册：channelId:{}，msg:{}", channelId, msg);
        String uid = msg.get("UID");
        int res = channelManager.registerChannel(channelId, uid);
        String rsp = new LockReturn(uid, msg.get("TYPE"), res == 1 ? TcpConstant.OK : TcpConstant.ERROR).toString();
        LOGGER.info("注册结果：channelId:{}，rsp:{}", channelId, rsp);
        messageSender.sendMsg(rsp, ServerConstant.DEFAULT);
        LOGGER.info("注册结果发送成功：channelId:{}，rsp:{}", channelId, rsp);
    }
}
