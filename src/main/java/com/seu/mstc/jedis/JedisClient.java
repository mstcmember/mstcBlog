package com.seu.mstc.jedis;

/**
 * Created by hys on 2018/4/21.
 */
import java.util.List;

public interface JedisClient {

    String set(String key, String value);
    String get(String key);
    Boolean exists(String key);
    Long expire(String key, int seconds);
    Long ttl(String key);
    Long incr(String key);
    Long hset(String key, String field, String value);
    String hget(String key, String field);
    Long hdel(String key, String... field);
    Boolean hexists(String key, String field);
    List<String> hvals(String key);
    long del(String key);
    long sadd(String key,String value);
    long srem(String key,String value);
    long scard(String key);
    boolean sismember(String key,String value);
    long lpush(String key,String value);
    List<String> brpop(int timeout,String key);
}