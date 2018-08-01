package com.java.mobile.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xdd
 * @date 2018/7/31
 */
public class HostNameUtil {
    private static HostNameUtil hostNameUtil = new HostNameUtil();
    private String hostname;

    private HostNameUtil() {
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var2) {
            this.hostname = "UnknowHost";
        }

    }

    public static String getHostname() {
        return hostNameUtil.hostname;
    }
}
