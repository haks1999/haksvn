package com.haks.haksvn.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

/*
 * url 기반 권한 관리를 할 예정이므로 사용자 검색과 같은 공통 기능은 controller 를 분리하여 관리함 
 * 
 */
@Controller
@RequestMapping(value="/common/users")
public class CommonUserController {
     
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/find", params ={"searchString"})
    public @ResponseBody List<Map<String,String>> listActiveUserByUserIdOrUserName(@RequestParam(value = "searchString", required = true) String searchString) {
    	List<User> userList = userService.retrieveActiveUserByUserIdOrUserName(searchString);
    	List<Map<String,String>> simpleUserList = new ArrayList<Map<String,String>>();
    	for( User user : userList ){
    		Map<String, String> simpleUser = new HashMap<String,String>();
    		simpleUser.put("userId", user.getUserId());
    		simpleUser.put("userName", user.getUserName());
    		simpleUser.put("label", user.getUserName() + "(" + user.getUserId() + ")");
    		simpleUser.put("value", user.getUserId());
    		simpleUserList.add( simpleUser );
    	}
    	return simpleUserList;
    }
    
}
