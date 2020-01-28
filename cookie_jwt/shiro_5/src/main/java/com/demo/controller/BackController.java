package com.demo.controller;

import com.demo.dao.UserDao;
import com.demo.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/back")
public class BackController {

    @Autowired
    private UserDao userDao;

    /* 后台首页（登录后就允许访问） */
    @GetMapping("/home")
    public ModelAndView home() {
        /* 根据用户拥有的权限决定其内容 */
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        ModelAndView view = new ModelAndView();
        view.addObject("user", user);
        view.setViewName("/back/home");
        return view;
    }

}
