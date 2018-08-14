package com.java.mobile.phone.login.service.impl;

import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    @Test
    public void test() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9090);// 建立TCP服务,连接本机的TCP服务器
             InputStream inputStream = socket.getInputStream();// 获得输入流
             OutputStream outputStream = socket.getOutputStream()) {
            // 写入数据
            outputStream.write("hello word.".getBytes());
            byte[] buf = new byte[1024];
            int len = inputStream.read(buf);
            System.out.println(new String(buf, 0, len));
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}