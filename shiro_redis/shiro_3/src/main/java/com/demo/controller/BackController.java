package com.demo.controller;

import com.common.util.JWTUtil;
import com.demo.dao.UserDao;
import com.demo.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/back")
public class BackController {

    @Autowired
    private UserDao userDao;

    /* 后台首页（登录后就允许访问） */
    @RequiresAuthentication
    @GetMapping("/home")
    public Map home() {
        Subject subject = SecurityUtils.getSubject();
        String name = JWTUtil.getName((String) subject.getPrincipal());
        User select = new User();
        select.setUserName(name);
        Example<User> userExample = Example.of(select);
        Optional<User> exist = userDao.findOne(userExample);

        Map result = new HashMap();
        result.put("msg", "后台首页");
        exist.ifPresent(user -> result.put("user", user));
        return result;
    }

}
