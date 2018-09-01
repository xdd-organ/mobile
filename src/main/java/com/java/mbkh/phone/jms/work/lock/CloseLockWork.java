package com.java.mbkh.phone.jms.work.lock;

import com.java.common.jms.AdvancedGroupQueueSender;
import com.java.mbkh.phone.jms.constant.ServerConstant;
import com.java.mbkh.phone.jms.vo.LockReturn;
import com.java.mbkh.phone.jms.work.LockWork;
import com.java.mbkh.phone.lock.constant.TcpConstant;
import com.java.mbkh.phone.lock.service.LockInfoService;
import com.java.mbkh.phone.lock.service.LockOrderService;
import com.java.mbkh.phone.user.service.TransFlowInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("closeLockWork")
public class CloseLockWork implements LockWork {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseLockWork.class);

    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private LockOrderService lockOrderService;
    @Autowired
    private LockInfoService lockInfoService;

    @Resource(name = "messageSender")
    private AdvancedGroupQueueSender messageSender;

    @Override
    @Transactional
    public void doService(String channelId, Map<String, String> msg) {
        String uid = msg.get("UID");
        LOGGER.info("上锁设备[{}]", uid);
        String res = TcpConstant.ERROR;
        try {// 建立TCP服务,连接本机的TCP服务器
            Map<String, Object> params = new HashMap<>();
            String fee = "-100";
            params.put("lock_no", uid);
            params.put("fee", fee);
            params.put("type", "1");
            transFlowInfoService.saveTrans(uid, fee, "0", "消费", "0");
            lockOrderService.lock(params);
            lockInfoService.updateLockState(uid, "0");
            res = TcpConstant.OK;
        } catch (Exception e) {
            LOGGER.error("异常：" + e.getMessage(), e);
        }
        String rsp = new LockReturn(uid, msg.get("TYPE"), res).toString();
        LOGGER.info("关锁返回:{}", rsp);
        messageSender.sendMsg(rsp, ServerConstant.DEFAULT);
        LOGGER.info("关锁消息返回成功：{}", rsp);
    }
}
