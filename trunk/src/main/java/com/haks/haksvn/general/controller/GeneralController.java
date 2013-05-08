package com.haks.haksvn.general.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/configuration/general")
public class GeneralController {
     
    @Autowired
    private RepositoryService repositoryService;
    
    @RequestMapping(value="/commitLog", method=RequestMethod.GET)
    public String forwardCommitLogPage( ModelMap model ) {
    	List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/general/modifyCommitLog";
    }
    
    
    
}
