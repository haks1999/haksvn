package com.haks.haksvn.common.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {
     
    
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String retrieveLoginPage(Model model, HttpServletRequest request, HttpServletResponse response) {
         
		return "login";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public RedirectView doLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
         
		return new RedirectView("/transfer/request/retrieveRequestList",true);
    }
}
