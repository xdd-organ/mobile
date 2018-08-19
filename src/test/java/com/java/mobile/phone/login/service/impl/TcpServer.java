package com.java.mobile.phone.login.service.impl;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer extends Thread {
    private static Map<String, OutputStream> aa = new HashMap<>();

    private Socket clientSocket;

    public TcpServer() {
    }

    public TcpServer setSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        return this;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = clientSocket.getOutputStream();//获取客户端的OutputStream与inputStream
             InputStream inputStream = clientSocket.getInputStream();) {
            String hostAddress = clientSocket.getInetAddress().getHostAddress();
            aa.put(hostAddress, outputStream);
            while (true) {

                //获得客户端的ip地址和主机名
                System.out.println("接收数据...........");

                //读取数据
                byte[] data = new byte[4096];
                int length = inputStream.read(data);
                if (length != -1) {
                    String str = new String(data, 0, length);
                    System.out.println("接收到的数据是：" + str);
                    //写出数据
                    outputStream.write((">>" + str).getBytes());
                    if ("886".equals(str)) {
                        break;
                    }
                    if ("192.168.1.100".equals(hostAddress)) {
                        OutputStream outputStream1 = aa.get("192.168.1.102");
                        if (outputStream1 != null) {
                            outputStream1.write((">>" + str).getBytes());
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("连接关闭!");
        }
    }

    @Test
    public void test() throws IOException {
        //建立TCP连接服务,绑定端口
        ServerSocket tcpServer = new ServerSocket(9090);
        //接受连接,传图片给连接的客户端,每个TCP连接都是一个java线程
        System.out.println("等待客户端连接。。。");
        while (true) {
            Socket clientSocket = tcpServer.accept();
            System.out.println("已连客户服端。。。");
            new TcpServer().setSocket(clientSocket).start();
        }
    }
}