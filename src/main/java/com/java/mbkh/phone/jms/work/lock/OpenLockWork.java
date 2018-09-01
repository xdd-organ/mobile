package com.java.mbkh.phone.jms.work.lock;

import com.java.common.cache.DeferredResultCache;
import com.java.mbkh.common.vo.Result;
import com.java.mbkh.phone.jms.work.LockWork;
import com.java.mbkh.phone.lock.service.LockInfoService;
import com.java.mbkh.phone.lock.service.LockOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@Service("openLockWork")
public class OpenLockWork implements LockWork {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenLockWork.class);

    @Autowired
    private DeferredResultCache cache;
    @Autowired
    private LockInfoService lockInfoService;
    @Autowired
    private LockOrderService lockOrderService;

    @Override
    @Transactional
    public void doService(String channelId, Map<String, String> msg) {
        LOGGER.info("开锁：channelId:{}，msg:{}", channelId, msg);
        String uid = msg.get("UID");
        String ret = msg.get("RET");
        try {
            if ("OK".equals(ret)) {
                LOGGER.info("解锁，设备返回解锁成功，lockNo:{}", uid);
                cache.get(uid).setResult(new Result(100));
            } else {
                DeferredResult deferredResult = cache.get(uid);
                if (deferredResult != null) {
                    LOGGER.warn("解锁，设备返回解锁失败，lockNo:{}", uid);
                    deferredResult.setResult(new Result(500));
                    lockInfoService.updateLockState(uid, "0");
                    lockOrderService.deleteLockOrder(uid);
                }
            }
        } catch (Exception e) {
            LOGGER.error("异常：" + e.getMessage(), e);
        }
    }
}
