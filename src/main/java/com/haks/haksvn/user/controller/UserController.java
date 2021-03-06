package com.haks.haksvn.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Controller
@RequestMapping(value="/configuration/users")
public class UserController {
     
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String forwardUserListPage( ModelMap model ) {
        List<User> userList = userService.retrieveUserList();
    	model.addAttribute("userList", userList );
        return "/user/listUser";
    }
    
    
    @RequestMapping(value="/list/{userSeq}", method=RequestMethod.GET)
    public String forwardUserModifyPage(@PathVariable String userSeq, ModelMap model) {
    	User user = userService.retrieveUserByUserSeq(Integer.valueOf(userSeq));
    	user.setUserPasswd("");
    	model.addAttribute("user", user);
    	return "/user/modifyUser";
    }
     
    @RequestMapping(value="/add")
    public String forwardUserAddPage(ModelMap model, @ModelAttribute("user") User user) {
    	model.addAttribute("user", user);
    	return "/user/modifyUser";
    }
    
    @RequestMapping(value={"/add/save","/list/*/save"}, method=RequestMethod.POST)
    public ModelAndView addUser(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult result){
    	if( user.getUserSeq() < 1 ){
    		ResultMessage message = userService.duplicateUser(user);
    		if( !message.isSuccess() ){
    			result.addError(new FieldError("user", "userId",message.getText()));
    		}
    	}
    	
    	if( result.hasErrors() ){
    		return new ModelAndView("/user/modifyUser");
    	}
    	
   		userService.saveUser(user);
   		return new ModelAndView(new RedirectView("/configuration/users/list", true));
    		
    }
    
    @RequestMapping(value={"/list/{userSeq}/delete"}, method=RequestMethod.POST)
    public ModelAndView deleteUser(@PathVariable String userSeq){
    	
   		userService.deleteUser(User.Builder.getBuilder(new User()).userSeq(Integer.parseInt(userSeq)).build());
   		return new ModelAndView(new RedirectView("/configuration/users/list", true));
    		
    }
    
}
