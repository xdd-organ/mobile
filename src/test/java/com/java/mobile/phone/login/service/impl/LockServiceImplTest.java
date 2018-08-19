package com.java.mobile.phone.login.service.impl;

import org.junit.Test;

public class LockServiceImplTest {

    @Test
    public void test() {
        System.err.println("----------------");
    }

    @Test
    public void test2 () {
        String a = "GET /test?test=1 HTTP/1.1 Content-Type: application/json;charset=utf-8 Host: 192.168.1.100 Connection: Keep Alive";
//        String a = "GET http://192.168.1.100:8080/test?test=1 HTTP/1.1 Content-Type: application/json;charset=utf-8 Host: 192.168.1.100 Connection: Keep Alive";
        byte[] bytes = a.getBytes();
        System.out.println(a.length());

    }

}