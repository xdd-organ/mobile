package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author xdd
 * @date 2018/8/1
 */
@Service
public class LockServiceImpl implements LockService {
    private static final String uid = "123456789";
    private Logger logger = LoggerFactory.getLogger(LockServiceImpl.class);

    @Override
    public String lock() {
        return "OK";
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
            return ret;
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OPEN";
    }
}
