package com.haks.haksvn.repository.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.user.model.User;

@Controller
@RequestMapping(value="/configuration/repositories")
public class RepositoryController {
     
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SVNRepositoryService svnRepositoryService;
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String forwardRepositoryListPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/repository/listRepository";
    }
    
    @RequestMapping(value="/list/{repositorySeq}", method=RequestMethod.GET)
    public String forwardRepositoryModifyPage(@PathVariable String repositorySeq, ModelMap model, HttpServletRequest req, final RedirectAttributes redirectAttributes) {
    	Repository repository = repositoryService.retrieveRepositoryByRepositorySeq(Integer.valueOf(repositorySeq));
    	model.addAttribute("repository", repository );

		//ModelAndView mv = new ModelAndView("forward:/configuration/repositories/add",model);
		//return mv;
    	return "/repository/modifyRepository";
    	//return new ModelAndView("forward:/configuration/repositories/add");
    }
     
    /*
    @RequestMapping(value="/list.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Repository> retrieveRepositoryListJson() {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
		return repositoryList;
    }
    */
    
    @RequestMapping(value="/add")
    public String forwardRepositoryAddPage(ModelMap model, @ModelAttribute("repository") Repository repository, HttpServletRequest request) {
    	model.addAttribute("repository", repository );
    	return "/repository/modifyRepository";
    }
    
    @RequestMapping(value={"/add/save","/list/*/save"}, method=RequestMethod.POST)
    public ModelAndView addRepository(ModelMap model, @ModelAttribute("repository") @Valid Repository repository, BindingResult result) {
    	
    	if( result.hasErrors() ){
    		return new ModelAndView("/repository/modifyRepository");
    		//return new ModelAndView("forward:/configuration/repositories/add");
    	}else{
    		repositoryService.saveRepository(repository);
    		return new ModelAndView(new RedirectView("/configuration/repositories/list", true));
    		
    	}
    	
    	//return new RedirectView("/configuration/repositories/list",true);
    	//return "redirect:/configuration/repositories/list";
    }
    
    @RequestMapping(value="/testConnection", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody DefaultMessage testConnection(@ModelAttribute("repository") Repository repository) throws HaksvnException {
		DefaultMessage defaultMessage = svnRepositoryService.testConnection(repository);
		return defaultMessage;
    }
    
    @RequestMapping(value="/listUser", method=RequestMethod.GET)
    public String forwardRepositoryUserPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveActiveRepositoryList();
        
    	model.addAttribute("repositoryList", repositoryList );
        return "/repository/listRepositoryUser";
    }
    
    @RequestMapping(value="/listUser/{repositorySeq}", method=RequestMethod.GET)
    public @ResponseBody List<User> listRepositoryUser(@PathVariable int repositorySeq){
    	
    	List<User> userList = repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq).getUserList();
    	return userList;
    	
    }
    
    @RequestMapping(value="/addUser/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage addRepositoryUser(@PathVariable int repositorySeq,
    												@RequestParam(value = "userId", required = true) String[] userIdList) throws HaksvnException{
    	
    	ResultMessage message = new ResultMessage("users added");
    	repositoryService.addRepositoryUser(Integer.valueOf(repositorySeq),Arrays.asList(userIdList));
    	return message;
    }
    
    @RequestMapping(value="/delUser/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody ResultMessage delRepositoryUser(@PathVariable int repositorySeq,
    													@RequestParam(value = "userId", required = true) String[] userIdList) throws HaksvnException{
    	
    	ResultMessage message = new ResultMessage("users removed");
    	repositoryService.deleteRepositoryUser(Integer.valueOf(repositorySeq),Arrays.asList(userIdList));
    	return message;
    }
    
    @ExceptionHandler(HaksvnException.class)
    public @ResponseBody ResultMessage HaksvnExceptionHandler(HaksvnException e) {
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
}