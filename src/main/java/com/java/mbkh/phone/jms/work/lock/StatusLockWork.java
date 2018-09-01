package com.java.mbkh.phone.jms.work.lock;

import com.java.common.jms.AdvancedGroupQueueSender;
import com.java.mbkh.phone.jms.constant.ServerConstant;
import com.java.mbkh.phone.jms.vo.LockReturn;
import com.java.mbkh.phone.jms.work.LockWork;
import com.java.mbkh.phone.lock.constant.TcpConstant;
import com.java.mbkh.phone.lock.service.LockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("statusLockWork")
public class StatusLockWork implements LockWork {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusLockWork.class);

    @Resource(name = "messageSender")
    private AdvancedGroupQueueSender messageSender;

    @Autowired
    private LockInfoService lockInfoService;

    @Override
    public void doService(String channelId, Map<String, String> msg) {
        LOGGER.info("锁上传状态：channelId:{}，msg:{}", channelId, msg);
        String state = "0";
        String status = msg.get("STATUS");
        String uid = msg.get("UID");
        if ("OPEN".equals(status)) {
            state = "3";
        } else if ("STOP".equals(status)) {
            state = "2";
        }
        int res = lockInfoService.updateLockState(uid, state);
        String rsp = new LockReturn(uid, msg.get("TYPE"), res == 1 ? TcpConstant.OK : TcpConstant.ERROR).toString();
        LOGGER.info("锁上传状态结果：channelId:{}，rsp:{}", channelId, rsp);
        messageSender.sendMsg(rsp, ServerConstant.DEFAULT);
        LOGGER.info("锁上传状态结果发送成功：channelId:{}，msg:{}", channelId, msg);
    }
}
