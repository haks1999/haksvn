package com.haks.haksvn.repository.controller;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
    public @ResponseBody ResultMessage testConnection(@ModelAttribute("repository") Repository repository) throws HaksvnException {
    	ResultMessage message = new ResultMessage("connection test success");
		svnRepositoryService.testInitalConnection(repository);
		return message;
    }
    
    @RequestMapping(value="/list/changeOrder", method=RequestMethod.POST)
    public @ResponseBody ResultMessage changeOrder(@RequestParam(value = "repositoryList") String repositoryListJson) throws Exception {
    	ResultMessage message = new ResultMessage("repository order changed success");
    	ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
   		List<Repository> repositoryList = mapper.readValue(repositoryListJson, new TypeReference<List<Repository>>(){});
   		repositoryService.changeRepositoryOrder(repositoryList);
		return message;
    }
    
    @RequestMapping(value="/listUser/{repositoryKey}", method=RequestMethod.GET)
    public @ResponseBody List<User> listRepositoryUser(@PathVariable String repositoryKey){
    	List<User> userList = repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey).getUserList();
    	return userList;
    }
    
    @RequestMapping(value="/listUser/addUser/{repositoryKey}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage addRepositoryUser(@PathVariable String repositoryKey,
    												@RequestParam(value = "userId", required = true) String[] userIdList,
    												@RequestParam(value = "overwrite", required = false) boolean overwrite){
    	
    	ResultMessage message = new ResultMessage("users added");
    	repositoryService.addRepositoryUser(repositoryKey,Arrays.asList(userIdList),overwrite);
    	return message;
    }
    
    @RequestMapping(value="/listUser/delUser/{repositoryKey}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage delRepositoryUser(@PathVariable String repositoryKey,
    													@RequestParam(value = "userId", required = true) String[] userIdList){
    	
    	ResultMessage message = new ResultMessage("users removed");
    	repositoryService.deleteRepositoryUser(repositoryKey,Arrays.asList(userIdList));
    	return message;
    }
    
    @RequestMapping(value="/add/validateRepositoryKey", params ={"repositoryKey"})
    public @ResponseBody String validateRepositoryKey(@RequestParam(value = "repositoryKey", required = true) String repositoryKey) {
    	return String.valueOf(repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey) == null);
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
