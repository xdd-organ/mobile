package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.sms.AliSmsService;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.lock.constant.TcpConstant;
import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.lock.service.LockService;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@Service
public class LockServiceImpl implements LockService {
    private static final String uid = "123456789";
    private Logger logger = LoggerFactory.getLogger(LockServiceImpl.class);

    @Autowired
    private LockInfoService lockInfoService;
    @Autowired
    private LockOrderService lockOrderService;
    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private DeferredResultCache cache;
    @Autowired
    private UserService userService;
    @Autowired
    private AliSmsService aliSmsService;
    @Autowired
    private LockOrderMapper lockOrderMapper;


    @Override
    public String lock(String uid) {
        logger.info("上锁设备[{}]", uid);
        try {// 建立TCP服务,连接本机的TCP服务器
            Map<String, Object> params = new HashMap<>();
            String fee = "-" + lockOrderService.calcLockOrderFee(uid);
            params.put("lock_no", uid);
            params.put("fee", fee);
            params.put("type", "1");
            lockOrderService.lock(params);
            int i = lockInfoService.updateLockState(uid, "0");
            return TcpConstant.OK;
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }


    @Override
    public String unLock(String openUid, Object userId) {
        String state = lockInfoService.getLockState(openUid);
        logger.info("解锁设备[{}],状态[{}]", openUid, state);
        if (state == null) {
            return "该设备编号未录入";
        } else if ("0".equals(state)) {
            try (Socket socket = new Socket(InetAddress.getLocalHost(), 8090);// 建立TCP服务,连接本机的TCP服务器
                 InputStream inputStream = socket.getInputStream();// 获得输入流
                 OutputStream outputStream = socket.getOutputStream()) {
                // 写入数据
                outputStream.write(("{\"TYPE\":\"OPEN\",\"UID\":\"" + uid + "\",\"OPEN_UID\":\"" + openUid + "\"}").getBytes());
                byte[] buf = new byte[1024];
                int len = inputStream.read(buf);
                String ret = new String(buf, 0, len);

                //入库
                Map<String, Object> params = new HashMap<>();
                params.put("lock_no", openUid);
                params.put("user_id", userId);
                params.put("insert_author", userId);
                params.put("update_author", userId);
                lockOrderService.insert(params);
                lockInfoService.updateLockState(openUid, "3");
                return ret;
                //关闭资源
            } catch (Exception e) {
                logger.error("异常：" + e.getMessage(), e);
            }
            return TcpConstant.ERROR;
        } else if ("2".equals(state)){
            return "暂停使用";
        } else if ("3".equals(state)) {
            return "正在使用中";
        } else {
            return TcpConstant.ERROR;
        }
    }

    @Override
    public String status(String uid, String type, String ret, String status) {
        logger.info("设备更新状态UID：{},TYPE:{},TET:{},status:{}", uid, type, ret, status);
        try {// 建立TCP服务,连接本机的TCP服务器
            if ("STATUS".equals(type)) {//设备更新状态
                String state = "0";
                if ("OPEN".equals(status)) {
                    state = "3";
                } else if ("STOP".equals(status)) {
                    state = "2";
                }
                int i = lockInfoService.updateLockState(uid, state);
                if (i == 1) {
                    return TcpConstant.OK;
                } else {
                    return TcpConstant.ERROR;
                }
            } else if ("OPEN".equals(type)) {//设备返回开锁结果
                if ("OK".equals(ret)) {
                    logger.info("解锁，设备返回解锁成功，lockNo:{}", uid);
                    cache.get(uid).setResult(new Result(100));
                } else {
                    DeferredResult deferredResult = cache.get(uid);
                    if (deferredResult != null) {
                        logger.warn("解锁，设备返回解锁失败，lockNo:{}", uid);
                        deferredResult.setResult(new Result(500));
                        lockInfoService.updateLockState(uid, "0");
                        lockOrderService.deleteLockOrder(uid);
                        lockOrderService.refundLockOrder(uid);
                    }
                }
            }
            return TcpConstant.OK;
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }
}
