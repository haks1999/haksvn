package com.haks.haksvn.common.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.security.model.LoginUser;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Controller
public class LoginController {
     
    @Autowired
    UserService userService;
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String forwardToLoginPage() {
         
		return "login";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView doLogin(@RequestParam(value = "userId", required = true) String userId,
    								@RequestParam(value = "userPasswd", required = true) String userPasswd,
    								ModelMap model) {
         
    	if( userId != null && userId.length() > 0 && userPasswd != null && userPasswd.length() > 0 ){
    		User user = userService.retrieveActiveUserByUserIdAndPasswd(User.Builder.getBuilder(new User()).userId(userId).userPasswd(userPasswd).build());
    		if( user != null ){
    			ContextHolder.addLoginUser(new LoginUser(user));
    			return new ModelAndView(new RedirectView("/transfer/request/retrieveRequestList",true));
    		}
    	}
    	model.addAttribute("resultMessage", "invalid account");
		return new ModelAndView("login", model);
    }
    
    @RequestMapping(value="/logout")
    public ModelAndView doLogout() {
    	ContextHolder.deleteLoginUser();
		return new ModelAndView("login");
    }
    
}
