package com.haks.haksvn.general.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.haks.haksvn.general.model.MailConfiguration;
import com.haks.haksvn.general.service.GeneralService;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/configuration/general")
public class GeneralController {
     
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private GeneralService generalService;
    
    @RequestMapping(value="/commitLog", method=RequestMethod.GET)
    public String forwardToCommitLogPage( ModelMap model ) {
    	List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/general/modifyCommitLog";
    }
    
    @RequestMapping(value="/mail", method=RequestMethod.GET)
    public String forwardToMailConfigurationPage( ModelMap model ) {
    	model.addAttribute("mailConfiguration", generalService.retrieveMailConfiguration());
        return "/general/modifyMail";
    }
    
    @RequestMapping(value="/mail", method=RequestMethod.POST)
    public String saveMailConfiguration(ModelMap model, 
    								@ModelAttribute("mailConfiguration") MailConfiguration mailConfiguration) {
    	generalService.saveMailConfiguration(mailConfiguration);
    	model.addAttribute("mailConfiguration", mailConfiguration );
        return "/general/modifyMail";
    }
    
    @RequestMapping(value="/mailTemplate", method=RequestMethod.GET)
    public String forwardToMailTemplatePage( ModelMap model ) {
    	List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/general/modifyMailTemplate";
    }
    
}
