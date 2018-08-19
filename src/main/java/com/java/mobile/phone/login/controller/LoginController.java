package com.java.mobile.phone.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

//    @PostMapping("login")
    @RequestMapping("login")
    public Map<String, Object> login() {
        logger.debug("------ DEBUG -------------");
        logger.info("------- INFO -------------");
        logger.warn("-------- WARN -------------");
        logger.error("------- ERROR -------------");
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("a", 1);
        objectObjectHashMap.put("b", 2);
        objectObjectHashMap.put("c", 3);
        return objectObjectHashMap;
    }

    @RequestMapping("send")
    public String send(@RequestParam("send") String send) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9090);// 建立TCP服务,连接本机的TCP服务器
             InputStream inputStream2 = socket.getInputStream();// 获得输入流
             OutputStream outputStream = socket.getOutputStream()) {
            // 写入数据
            outputStream.write(send.getBytes());
            byte[] buf = new byte[1024];
            int len = inputStream2.read(buf);
            System.out.println(new String(buf, 0, len));
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
