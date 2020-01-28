package com.common.shiro;

import com.common.util.JWTUtil;
import com.common.util.RedisUtil;
import com.demo.dao.UserDao;
import com.demo.model.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Value("${redis.open}")
    private Boolean redisOpen;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheManager cacheManager;

    /*
     * ①必须重写此方法，否则报错
     * ②此处将AuthenticationToken转换为JWTToken
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /*
     * 登录
     * ①Cookie：登录时从本机内存/Redis中取出Session与Cookie中的SessionId比对
     * ②JWT：登录时校验token本身（没有注销，手动等待token失效）
     * ③JWT+Redis：登录时从Redis中取出token与Header中的token比对（与Cookie机制又有什么区别？）
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials(); /* 此处token由JWTFilter获取 */
        String name = JWTUtil.getName(token);
        if (name == null) {
            throw new AuthenticationException("token无效");
        } else {
            User select = new User();
            select.setUserName(name);
            Example<User> userExample = Example.of(select);
            Optional<User> exist = userDao.findOne(userExample);
            if (!exist.isPresent()) {
                throw new AuthenticationException("用户不存在");
            } else if (!JWTUtil.verify(token, name, exist.get().getUserPassword())) {
                throw new AuthenticationException("密码错误");
            } else {
                String cacheToken = "";
                if (redisOpen) {
                    cacheToken = redisUtil.read(exist.get().getUuid());
                } else {
                    cacheToken = cacheManager.getCache("user").get(exist.get().getUuid(), String.class);
                }
                if (!token.equals(cacheToken)) {
                    throw new AuthenticationException("token无效");
                }
            }
        }
        return new SimpleAuthenticationInfo(token, token, "JWTRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

}
