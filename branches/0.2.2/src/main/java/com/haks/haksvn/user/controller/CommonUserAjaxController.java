package com.haks.haksvn.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

/*
 * url 기반 권한 관리를 할 예정이므로 사용자 검색과 같은 공통 기능은 controller 를 분리하여 관리함 
 * 
 */
@Controller
@RequestMapping(value="/common/users")
public class CommonUserAjaxController {
     
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/find", params ={"searchString"})
    public @ResponseBody List<User> listActiveUserByUserIdOrUserName(@RequestParam(value = "searchString", required = true) String searchString) {
    	List<User> userList = userService.retrieveActiveUserByUserIdOrUserName(searchString);
    	return userList;
    }
    
    @RequestMapping(value="/find/{repositoryKey}", params ={"searchString"})
    public @ResponseBody List<User> listRepositoryActiveUserByUserIdOrUserName(
    								@PathVariable String repositoryKey,
    								@RequestParam(value = "searchString", required = true) String searchString) {
    	List<User> userList = userService.retrieveRepositoryActiveUserByUserIdOrUserName(searchString, repositoryKey);
    	return userList;
    }
    
    @ExceptionHandler(HaksvnException.class)
    public @ResponseBody ResultMessage haksvnExceptionHandler(Exception e) {
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
    
    @ExceptionHandler(Exception.class)
    public @ResponseBody ResultMessage exceptionHandler(Exception e) {
    	e.printStackTrace();
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
}
