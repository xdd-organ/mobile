package com.java.mobile.common.utils;

import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

/**
 * @author xdd
 * @date 2018/7/31
 */
public class PortUtil {
    private static PortUtil portUtil = new PortUtil();
    private String port;
    private PortUtil() {
        try {
            MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
            ObjectName name = new ObjectName("Catalina", "type", "Server");
            Server server = (Server)mBeanServer.getAttribute(name, "managedResource");
            Service[] services = server.findServices();
            Service[] var8 = services;
            int var7 = services.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                Service service = var8[var6];
                Connector[] var12;
                int var11 = (var12 = service.findConnectors()).length;

                for(int var10 = 0; var10 < var11; ++var10) {
                    Connector connector = var12[var10];
                    ProtocolHandler protocolHandler = connector.getProtocolHandler();
                    if (protocolHandler instanceof AbstractHttp11Protocol) {
                        this.port = String.valueOf(connector.getPort());
                    }
                }
            }
        } catch (Exception var14) {
            ;
        }
    }

    public static String getPort() {
        return portUtil.port;
    }
}
