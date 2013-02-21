package com.haks.haksvn.source.controller;

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
@RequestMapping(value="/source/browse")
public class SourceBrowseController {
         
	@Autowired
    private RepositoryService repositoryService;
    
	@RequestMapping(value="", method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	System.out.println("re???");
        return "/source/sourceBrowse";
    }
 
}
