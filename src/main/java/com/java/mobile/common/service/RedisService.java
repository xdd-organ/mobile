package com.java.mobile.common.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    /**
     * 根据key，获取值
     * @param key
     * @return 查询的值
     */
    String get(String key);

    /**
     * 根据key，获取值
     * @param key
     * @return 查询的值
     */
    byte[] get(byte[] key);

    /**
     * 设置key-value
     * @param key 键
     * @param value 值
     * @return
     */
    String set(String key, String value);

    /**
     * 设置key-value
     * @param key 键
     * @param value 值
     * @return
     */
    String set(byte[] key, byte[] value);

    /**
     * 添加key-value,并设置key存活时间
     * @param key 键
     * @param value 值
     * @param seconds 存活时间：秒
     * @return
     */
    String set(String key, String value, Integer seconds);

    /**
     * 添加key-value,并设置key存活时间
     * @param key 键
     * @param value 值
     * @param seconds 存活时间：秒
     * @return
     */
    String set(byte[] key, byte[] value, Integer seconds);

    /**
     * 在之前的value追加
     * @param key 键
     * @param value 值
     * @return
     */
    Long append(String key, String value);

    /**
     * 设置key存活时间
     * @param key 键
     * @param seconds 存活时间：秒
     * @return
     */
    Long expire(String key, Integer seconds);

    /**
     * 设置key存活时间
     * @param key 键
     * @param seconds 存活时间：秒
     * @return
     */
    Long expire(byte[] key, Integer seconds);

    /**
     * 删除key
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * 删除key
     * @param key
     * @return
     */
    Long del(byte[] key);

    /**
     * 根据key获取Set集合
     * @param key
     * @return Set<String>
     */
    Set<String> smembers(String key);

    /**
     * 添加Set集合
     * @param key 键
     * @param value 字符串数组
     * @return
     */
    Long sadd(String key, String... value);

    /**
     * 根据key与元素值，删除指定元素
     * @param key 键
     * @param value 指定元素值
     * @return
     */
    Long srem(String key, String value);

    /**
     * 判断set集合指定元素是否存在
     * @param key 键
     * @param value 指定元素值
     * @return
     */
    Boolean sismember(String key, String value);

    /**
     * 判断key是否存在
     * @param key 键
     * @return
     */
    Boolean exists(String key);

    /**
     * 判断key是否存在
     * @param key 键
     * @return
     */
    Boolean exists(byte[] key);

    /**
     * 查看key剩余时间
     * @param key 键
     * @return
     */
    Long ttl(String key);

    /**
     * 移除key的存活时间，即永久生存
     * @param key
     * @return
     */
    Long persist(String key);

    /**
     * 添加list集合
     * @param key 键
     * @param value
     * @return
     */
    Long lpush(String key, String... value);

    /**
     * 获取list集合数据   (start =0,end=-1):查询所有
     * @param key 键
     * @param start 开始角标
     * @param end 结束角标
     * @return
     */
    List<String> lrange(String key, Integer start, Integer end);

    /**
     * 删除key中指定value
     * @param key 键
     * @param count 删除个数（有重复），后add进去的值先被删
     * @param value 值
     * @return
     */
    Long lrem(String key, Integer count, String value);

    /**
     * 删除key中指定角标之外value
     * @param key 键
     * @param start 开始角标
     * @param end 结束角标
     * @return
     */
    String ltrim(String key, long start, long end);

    /**
     * 获取list集合的size
     * @param key 键
     * @return
     */
    Long llen(String key);

    /**
     * 获取list集合指定角标value
     * @param key 键
     * @param index 角标
     * @return
     */
    String lindex(String key, long index);

    /**
     * 存入一个Map
     * @param key
     * @param value
     * @return
     */
    String hmset(String key, Map<String, String> value);

    /**
     * 存入一个Map
     * @param key
     * @param value
     * @return
     */
    String hmset(byte[] key, Map<byte[], byte[]> value);

    /**
     * 向Map追加一个键值对
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hset(String key, String field, String value);

    /**
     * 向Map追加一个键值对
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hset(byte[] key, byte[] field, byte[] value);

    /**
     * 获取一个Map
     * @param key
     * @return
     */
    Map<String, String> hgetAll(String key);

    /**
     * 获取一个Map
     * @param key
     * @return
     */
    Map<byte[], byte[]> hgetAll(byte[] key);

    /**
     * 获取Map指定键的值
     * @param key Map键
     * @param fields Map中的指定字段
     * @return
     */
    List<String> hmget(String key, String... fields);

    /**
     * 获取Map指定键的值
     * @param key Map键
     * @param fields Map中的指定字段
     * @return
     */
    List<byte[]> hmget(byte[] key, byte[]... fields);

    /**
     * 删除Map指定键的值
     * @param key Map键
     * @param fields
     * @return
     */
    Long hdel(String key, String... fields);

    /**
     * 删除Map指定键的值
     * @param key Map键
     * @param fields
     * @return
     */
    Long hdel(byte[] key, byte[]... fields);

    /**
     * 获取所有Map中所有值
     * @param key Map键
     * @return
     */
    List<String> hvals(String key);

    /**
     * 获取所有Map中所有值
     * @param key Map键
     * @return
     */
    Collection<byte[]> hvals(byte[] key);

    /**
     * 获取所有Map中所有key
     * @param key
     * @return
     */
    Set<String> hkeys(String key);

    /**
     * 获取所有Map中所有key
     * @param key
     * @return
     */
    Set<byte[]> hkeys(byte[] key);

    /**
     * 获取Map键值对数
     * @param key
     * @return
     */
    Long hlen(String key);


}