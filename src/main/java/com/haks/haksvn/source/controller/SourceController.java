package com.haks.haksvn.source.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.service.SourceService;

@Controller
@RequestMapping(value="/source")
public class SourceController {
         

	@Autowired
    private RepositoryService repositoryService;
    
	@RequestMapping(value="/browse", method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/source/sourceBrowse";
    }
	
	@RequestMapping(value="/changes", method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/source/listChange";
    }
 
	
 
}
