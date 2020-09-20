package com.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    /*
     * ①因为RedisTemplate是由Spring注入的，不能用static修饰，不能用new RedisUtil方法调用，而应该注入RedisUtil
     * */
    @Autowired
    private RedisTemplate redisTemplate;

    public void create(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    public String read(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
