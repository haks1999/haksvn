package com.haks.haksvn.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.property.service.PropertyService;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/configuration/repositories")
public class RepositoryController {
     
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private PropertyService propertyService;
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String forwardRepositoryListPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/repository/listRepository";
    }
    
    @RequestMapping(value="/list/{repositoryKey}", method=RequestMethod.GET)
    public String forwardRepositoryModifyPage(@PathVariable String repositoryKey, ModelMap model, HttpServletRequest req, final RedirectAttributes redirectAttributes) {
    	Repository repository = repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey);
    	String authzTemplate = String.format(propertyService.retrievePropertyByPropertyKey("svn.authz.template.default").getPropertyValue());
    	if( repository.getAuthzTemplate() == null ) repository.setAuthzTemplate(authzTemplate);
    	repository.setAuthzTemplate(String.format(repository.getAuthzTemplate()));
    	repository.setAuthUserPasswd("");
    	model.addAttribute("repository", repository );
    	model.addAttribute("authzTemplate", authzTemplate );
    	return "/repository/modifyRepository";
    }
     
    /*
    @RequestMapping(value="/list.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Repository> retrieveRepositoryListJson() {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
		return repositoryList;
    }
    */
    
    @RequestMapping(value="/add")
    public String forwardRepositoryAddPage(ModelMap model, @ModelAttribute("repository") Repository repository) {
    	String authzTemplate = String.format(propertyService.retrievePropertyByPropertyKey("svn.authz.template.default").getPropertyValue());
    	repository.setAuthzTemplate(authzTemplate);
    	model.addAttribute("repository", repository );
    	model.addAttribute("authzTemplate", authzTemplate );
    	return "/repository/modifyRepository";
    }
    
    @RequestMapping(value={"/add/save","/list/*/save"}, method=RequestMethod.POST)
    public ModelAndView saveRepository(ModelMap model, @ModelAttribute("repository") @Valid Repository repository, BindingResult result) throws Exception{
    	if( result.hasErrors() ){
    		return new ModelAndView("/repository/modifyRepository");
    		//return new ModelAndView("forward:/configuration/repositories/add");
    	}else{
    		repositoryService.saveRepository(repository);
    		return new ModelAndView(new RedirectView("/configuration/repositories/list", true));
    		
    	}
    }
    
    @RequestMapping(value={"/list/{repositoryKey}/delete"}, method=RequestMethod.POST)
    public ModelAndView deleteRepository(@PathVariable String repositoryKey) throws Exception{
   		repositoryService.deleteRepository(Repository.Builder.getBuilder(new Repository()).repositoryKey(repositoryKey).build());
   		return new ModelAndView(new RedirectView("/configuration/repositories/list", true));
    		
    }
    
    @RequestMapping(value="/listUser", method=RequestMethod.GET)
    public String forwardRepositoryUserPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveActiveRepositoryList();
        
    	model.addAttribute("repositoryList", repositoryList );
        return "/repository/listRepositoryUser";
    }
 
}
