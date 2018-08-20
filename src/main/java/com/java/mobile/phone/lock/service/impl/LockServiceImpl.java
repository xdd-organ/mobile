package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.constant.TcpConstant;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.lock.service.LockService;
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


    @Override
    public String lock(String uid) {
        try {// 建立TCP服务,连接本机的TCP服务器
            Map<String, Object> params = new HashMap<>();
            params.put("lock_no", uid);
            params.put("update_author", 1);
            params.put("fee", 1);
            lockOrderService.lock(params);
            return TcpConstant.OK;
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }

    @Override
    public String unLock(String openUid) {
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
            params.put("user_id", 1);
            params.put("insert_author", 1);
            params.put("update_author", 1);
            lockOrderService.insert(params);
            return ret;
            //关闭资源
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }

    @Override
    public String status(String uid) {
        try {// 建立TCP服务,连接本机的TCP服务器
            return TcpConstant.OK;
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
}
}
