package com.haks.haksvn.repository.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.user.model.User;

@Controller
@RequestMapping(value="/configuration/repositories")
public class RepositoryAjaxController {
     
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SVNRepositoryService svnRepositoryService;
    
    @RequestMapping(value="/list/testConnection", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody DefaultMessage testConnection(@ModelAttribute("repository") Repository repository) throws HaksvnException {
    	ResultMessage message = new ResultMessage("connection test success");
		svnRepositoryService.testInitalConnection(repository);
		return message;
    }
    
    @RequestMapping(value="/listUser/{repositorySeq}", method=RequestMethod.GET)
    public @ResponseBody List<User> listRepositoryUser(@PathVariable int repositorySeq){
    	List<User> userList = repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq).getUserList();
    	return userList;
    }
    
    @RequestMapping(value="/listUser/addUser/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage addRepositoryUser(@PathVariable int repositorySeq,
    												@RequestParam(value = "userId", required = true) String[] userIdList,
    												@RequestParam(value = "overwrite", required = false) boolean overwrite){
    	
    	ResultMessage message = new ResultMessage("users added");
    	repositoryService.addRepositoryUser(Integer.valueOf(repositorySeq),Arrays.asList(userIdList),overwrite);
    	return message;
    }
    
    @RequestMapping(value="/listUser/delUser/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage delRepositoryUser(@PathVariable int repositorySeq,
    													@RequestParam(value = "userId", required = true) String[] userIdList){
    	
    	ResultMessage message = new ResultMessage("users removed");
    	repositoryService.deleteRepositoryUser(Integer.valueOf(repositorySeq),Arrays.asList(userIdList));
    	return message;
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
