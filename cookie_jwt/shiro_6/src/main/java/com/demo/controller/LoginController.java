package com.demo.controller;

import cn.hutool.crypto.SecureUtil;
import com.common.util.JWTUtil;
import com.common.util.RedisUtil;
import com.demo.dao.UserDao;
import com.demo.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Value("${redis.open}")
    private Boolean redisOpen;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheManager cacheManager;

    /* 登录行为 */
    @PostMapping("/doLogin")
    public Map doLogin(String name, String password) {
        User select = new User();
        select.setUserName(name);
        Example<User> userExample = Example.of(select);
        Optional<User> exist = userDao.findOne(userExample);

        Map result = new HashMap();
        if (!exist.isPresent()) {
            result.put("msg", "用户不存在");
        } else if (!exist.get().getUserPassword().equals(SecureUtil.md5(password))) {
            result.put("msg", "密码错误");
        } else {
            String token = JWTUtil.sign(name, SecureUtil.md5(password));
            if (redisOpen) {
                redisUtil.create(exist.get().getUuid(), token);
            } else {
                cacheManager.getCache("user").put(exist.get().getUuid(), token);
            }
            result.put("msg", "登录成功");
            result.put("token", token);
        }
        return result;
    }

    /* fixme 注销（JWT的注销需要登录后才能访问） */
    @GetMapping("/doLogout")
    public Map doLogout() {
        Subject subject = SecurityUtils.getSubject();
        String name = JWTUtil.getName((String) subject.getPrincipal());
        User select = new User();
        select.setUserName(name);
        Example<User> userExample = Example.of(select);
        Optional<User> exist = userDao.findOne(userExample);

        Map result = new HashMap();
        if (redisOpen) {
            try {
                exist.ifPresent(user -> redisUtil.delete(user.getUuid()));
                result.put("msg", "注销成功");
            } catch (Exception e) {
                e.printStackTrace();
                result.put("msg", "注销失败");
            }
        } else {
            try {
                exist.ifPresent(user -> cacheManager.getCache("user").put(user.getUuid(), ""));
                result.put("msg", "注销成功");
            } catch (Exception e) {
                e.printStackTrace();
                result.put("msg", "注销失败");
            }
        }
        return result;
    }

    @GetMapping("/exist")
    public Map exist() {
        Map result = new HashMap();
        result.put("msg", "用户不存在");
        return result;
    }

    @GetMapping("/error")
    public Map error() {
        Map result = new HashMap();
        result.put("msg", "密码错误");
        return result;
    }

    @GetMapping("/invalid")
    public Map invalid() {
        Map result = new HashMap();
        result.put("msg", "token无效");
        return result;
    }

}
