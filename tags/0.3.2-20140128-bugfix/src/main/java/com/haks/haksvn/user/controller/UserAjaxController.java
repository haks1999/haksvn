package com.haks.haksvn.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.user.service.UserService;

/*
 * url 기반 권한 관리를 할 예정이므로 사용자 검색과 같은 공통 기능은 controller 를 분리하여 관리함 
 * 
 */
@Controller
@RequestMapping(value="/configuration/users")
public class UserAjaxController {
     
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/add/validateUserId", params ={"userId"})
    public @ResponseBody String validateUserId(@RequestParam(value = "userId", required = true) String userId) {
    	return String.valueOf(userService.retrieveUserByUserId(userId) == null);
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
