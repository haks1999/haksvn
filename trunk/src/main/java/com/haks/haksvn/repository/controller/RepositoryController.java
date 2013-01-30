package com.haks.haksvn.repository.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;

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
        return "/configuration/listRepository";
    }
    
    @RequestMapping(value="/list/{repositorySeq}", method=RequestMethod.GET)
    public String forwardRepositoryModifyPage(@PathVariable String repositorySeq, ModelMap model) {
    	Repository repository = repositoryService.retrieveRepositoryByRepositorySeq(Integer.valueOf(repositorySeq));
    	model.addAttribute("repository", repository );
        return "/configuration/modifyRepository";
    }
     
    /*
    @RequestMapping(value="/list.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Repository> retrieveRepositoryListJson() {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
		return repositoryList;
    }
    */
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String forwardRepositoryAddPage(ModelMap model) {
    	model.addAttribute("repository", new Repository() );
    	return "/configuration/modifyRepository";
    }
    
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public RedirectView addRepository(@ModelAttribute("repository") Repository repository) {
    	repositoryService.saveRepository(repository);
    	return new RedirectView("/configuration/repositories/list",true);
    }
    
    @RequestMapping(value="/testConnection", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody DefaultMessage testConnection(@ModelAttribute("repository") Repository repository) {
		DefaultMessage defaultMessage = svnRepositoryService.testConnection(repository);
		return defaultMessage;
    }
}
