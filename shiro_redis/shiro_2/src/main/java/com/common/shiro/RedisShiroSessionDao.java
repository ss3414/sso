package com.common.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class RedisShiroSessionDao extends EnterpriseCacheSessionDAO {

    /*
    * 流程
    * ①第一次访问：读取Session/更新Session（更新即是创建）
    * ②登录：更新Session（需要User类实现序列化）
    * ③注销：删除Session
    * */
    @Autowired
    private RedisTemplate redisTemplate;

    private void setShiroSession(String key, Session session) {
        redisTemplate.opsForValue().set(key, session);
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
    }

    private Session getShiroSession(String key) {
        return (Session) redisTemplate.opsForValue().get(key);
    }

    /* 创建Session */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        final String key = sessionId.toString();
        setShiroSession(key, session);
        return sessionId;
    }

    /* 读取Session */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            final String key = sessionId.toString();
            session = getShiroSession(key);
        }
        return session;
    }

    /* 更新Session */
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        final String key = session.getId().toString();
        setShiroSession(key, session);
    }

    /* 删除Session */
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        final String key = session.getId().toString();
        redisTemplate.delete(key);
    }

}
