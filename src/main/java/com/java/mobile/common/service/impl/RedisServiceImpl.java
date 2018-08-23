package com.java.mobile.common.service.impl;

import com.java.mobile.common.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("redisService")
public class RedisServiceImpl implements RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    @Override
    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public byte[] get(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String set(String key,String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.set(key,value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String set(byte[] key, byte[] value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String set(String key, String value, Integer seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String str = shardedJedis.set(key, value);
            shardedJedis.expire(key , seconds);
            return str;
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String set(byte[] key, byte[] value, Integer seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String str = shardedJedis.set(key, value);
            shardedJedis.expire(key, seconds);
            return str;
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long append(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.append(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long expire(String key, Integer seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expire(key,seconds);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long expire(byte[] key, Integer seconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expire(key,seconds);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long del(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.del(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long del(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.del(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Set<String> smembers(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.smembers(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long sadd(String key, String... value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.sadd(key,value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long srem(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.srem(key,value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Boolean sismember(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Boolean exists(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.exists(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Boolean exists(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.exists(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long ttl(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.ttl(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long persist(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.persist(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long lpush(String key, String... value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public List<String> lrange(String key, Integer start, Integer end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long lrem(String key, Integer count, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrem(key, count, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String ltrim(String key, long start, long end) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long llen(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.llen(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String lindex(String key, long index) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lindex(key,index);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String hmset(String key, Map<String, String> value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hmset(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hmset(key, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long hdel(String key, String... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public List<String> hvals(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hvals(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Collection<byte[]> hvals(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hvals(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Set<String> hkeys(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hkeys(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hkeys(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long hlen(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hlen(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long hset(String key, String field, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("异常：", e);
        } finally {
            if (null != shardedJedis) shardedJedis.close();
        }
        return null;
    }


}