package com.java.mobile.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xdd
 * @date 2018/7/31
 */
public class IPUtil {
    private static IPUtil ipUtil = new IPUtil();
    private String ip;

    private IPUtil() {
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var2) {
            this.ip = "0.0.0.0";
        }

    }

    public static String getIp() {
        return ipUtil.ip;
    }
}
