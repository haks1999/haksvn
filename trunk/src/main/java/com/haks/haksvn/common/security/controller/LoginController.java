package com.haks.haksvn.common.security.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.security.model.LoginUser;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Controller
public class LoginController {
     
    @Autowired
    UserService userService;
    
    private static final String WELCOME_MENU_URL = "/transfer/request/list";
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public ModelAndView forwardLoginPage() {
        
    	if( ContextHolder.isLoggedIn() ){
    		return new ModelAndView(new RedirectView(WELCOME_MENU_URL,true));
    	}else{
    		return new ModelAndView("login");
    	}
    	
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView doLogin(@RequestParam(value = "userId", required = true) String userId,
    								@RequestParam(value = "userPasswd", required = true) String userPasswd,
    								@RequestHeader(value = "referer", required = false) final String referer,
    								HttpServletRequest request,
    								ModelMap model) {
         
    	if( userId != null && userId.length() > 0 && userPasswd != null && userPasswd.length() > 0 ){
    		User user = userService.retrieveActiveUserByUserIdAndPasswd(User.Builder.getBuilder(new User()).userId(userId).userPasswd(userPasswd).build());
    		if( user != null ){
    			ContextHolder.addLoginUser(new LoginUser(user));
    			String redirectUrl = WELCOME_MENU_URL;
    			try{
    				URI uri =  new URI(referer);
    				String refererPath = uri.getPath().substring(request.getContextPath().length());
    				if(uri.getQuery() !=null ) refererPath += "?"+uri.getQuery();
    				redirectUrl = refererPath.length() < 2 || refererPath.contains("/login") ? redirectUrl:refererPath;
    			}catch(Exception e){
    				throw new HaksvnException(e);
    			}
    			return new ModelAndView(new RedirectView(redirectUrl,true));
    		}
    	}
    	model.addAttribute("resultMessage", "invalid account");
		return new ModelAndView("login", model);
    }
    
    @RequestMapping(value="/logout")
    public ModelAndView doLogout() {
    	ContextHolder.deleteLoginUser();
    	return new ModelAndView(new RedirectView("/login",true));
    }
    
}
