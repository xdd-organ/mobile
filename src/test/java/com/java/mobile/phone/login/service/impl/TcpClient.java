package com.java.mobile.phone.login.service.impl;

import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
//    @Test//普通的TCP连接
//    public void test() {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8090);// 建立TCP服务,连接本机的TCP服务器
             InputStream inputStream2 = socket.getInputStream();// 获得输入流
             BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));// 获得输入流
             OutputStream outputStream = socket.getOutputStream()) {
            String line = null;
            while ((line = inputStream.readLine()) != null) {
                // 写入数据
                outputStream.write((line + "\n").getBytes());
                byte[] buf = new byte[1024];
                int len = inputStream2.read(buf);
                System.out.println(new String(buf, 0, len));
                if ("886".equals(line)) {
                    break;
                }
            }
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test//使用TCP发送http请求
    public void sendHttpRequest() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);// 建立TCP服务,连接本机的TCP服务器
             InputStream inputStream = socket.getInputStream();// 获得输入流
             OutputStream outputStream = socket.getOutputStream()) {
            // 写入数据
            outputStream.write(("GET http://192.168.59.1:9999/login HTTP/1.1\n" +
                    "Host: 192.168.59.1:9999\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Accept-Language: zh-CN,zh;q=0.8\n" +
                    "\n").getBytes());
            byte[] buf = new byte[1024];
            int len = inputStream.read(buf);
            System.out.println(new String(buf, 0, len));
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}