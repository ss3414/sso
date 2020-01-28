package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class DemoController {

    @RequestMapping("/login2")
    public ModelAndView login2(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();

        System.out.println(request.getRemoteUser());
        System.out.println(request.getUserPrincipal());

        view.addObject("user", request.getRemoteUser());
        view.setViewName("/login2");
        return view;
    }

    @RequestMapping("/logout2")
    public ModelAndView logout2(HttpSession session) {
        session.invalidate();
        return new ModelAndView("/logout2");
    }

}
