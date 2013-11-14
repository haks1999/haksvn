package com.haks.haksvn.rest.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.security.model.LoginUser;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.service.TaggingService;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Controller
@RequestMapping(value="/rest/transfer")
public class TransferRestController {
     
    @Autowired
    private TaggingService taggingService;
    @Autowired
    private UserService userService;
    
   
    // 임시로 만든 rest tagging create 
    // 0.2.2 에서만 쓰는거. 0.3+ 에서는 제대로 적용 
    @RequestMapping(value="/taggings", method=RequestMethod.POST )
    public @ResponseBody Tagging createTagging(	@RequestParam(value = "tagName", required = true) String tagName,
    											@RequestParam(value = "description", required = true) String description,
    											@RequestParam(value = "taggingUserId", required = true) String taggingUserId,
    											@RequestParam(value = "repositoryKey", required = true) String repositoryKey) throws HaksvnException {
    	
    	User user = userService.retrieveUserByUserId(taggingUserId);
    	if( user == null ) throw new HaksvnException("invalid user");
    	ContextHolder.addLoginUser(new LoginUser(user));
    	Tagging tagging = Tagging.Builder.getBuilder().tagName(tagName).description(description).repositoryKey(repositoryKey).build();
    	tagging = taggingService.createTagging(tagging);
    	ContextHolder.deleteLoginUser();
    	return tagging;
    }
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
