package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.constant.TcpConstant;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.lock.service.LockService;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
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


    @Override
    public String lock(String uid) {
        logger.info("上锁设备[{}]", uid);
        try {// 建立TCP服务,连接本机的TCP服务器
            Map<String, Object> params = new HashMap<>();
            String fee = "-100";
            params.put("lock_no", uid);
            params.put("fee", fee);
            transFlowInfoService.saveTrans(uid, fee, "0", "消费", "0");
            lockOrderService.lock(params);
            lockInfoService.updateLockState(uid, "0");
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
    public String status(String uid, String status, String type) {
        logger.info("设备更新状态[{}],状态[{}],类型[{}]", uid, status, type);
        try {// 建立TCP服务,连接本机的TCP服务器
            if ("STATUS".equals(type)) {
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
            }
            return TcpConstant.OK;
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }
}