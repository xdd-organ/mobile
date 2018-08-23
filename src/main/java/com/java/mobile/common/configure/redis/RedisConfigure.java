package com.java.mobile.common.configure.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class RedisConfigure {
//    @Autowired(required = false)
    private JedisPoolConfig jedisPoolConfig;
//    @Resource(name = "hostList")
//    @Autowired
    private List<JedisShardInfo> hostList;

//    @Value("${redis.host:}")
    private static String host;
//    @Value("${redis.port:}")
    private static String port;
//    @Value("${redis.password:}")
    private static String password;

//    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setNumTestsPerEvictionRun(3);
        config.setTimeBetweenEvictionRunsMillis(-1);
        config.setMinEvictableIdleTimeMillis(1800000);
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);
        config.setJmxEnabled(true);
        config.setJmxNamePrefix("pool");
        config.setMaxWaitMillis(-1);
        config.setBlockWhenExhausted(false);
//        this.jedisPoolConfig = config;
        return config;
    }

//    @Bean
    public ShardedJedisPool getShardedJedisPool() {
        return new ShardedJedisPool(jedisPoolConfig, hostList);
    }

//    @Bean(name = "hostList")
    public List<JedisShardInfo> getJedisShardInfo() {
        List<JedisShardInfo> list = new ArrayList<>();
        String[] hosts = host.split(",");
        String[] ports = port.split(",");
        String[] passwords = password.split(",");
        if (hosts != null && hosts.length > 0) {
            for (int i = 0; i < hosts.length; i++) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(hosts[i], ports[i]);
                if (StringUtils.isNotBlank(passwords[i])) jedisShardInfo.setPassword(passwords[i]);
                list.add(jedisShardInfo);
            }
        }
//        this.hostList = list;
        return list;
    }

    @Value("${redis.host:}")
    public void setHost(String host) {
        RedisConfigure.host = host;
    }
    @Value("${redis.port:}")
    public void setPort(String port) {
        RedisConfigure.port = port;
    }

    @Value("${redis.password:}")
    public void setPassword(String password) {
        RedisConfigure.password = password;
    }

//    @PostConstruct
    public void init() {
//        zhifuUrl = zhifuUrlTmp;
        System.out.println(RedisConfigure.password);
    }
}
