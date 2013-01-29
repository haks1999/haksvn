package com.haks.haksvn.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.menu.util.MenuUtil;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/configuration/repositories")
public class RepositoryController {
     
    @Autowired
    private RepositoryService repositoryService;
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String forwardRepositoryListPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        MenuUtil.setContentJspPath(request, response, "/WEB-INF/views/configuration/listRepository.jsp");
        return MenuUtil.getFowardUrl(request);
    }
     
    /*
    @RequestMapping(value="/list.json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<Repository> retrieveRepositoryListJson() {
        List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
		return repositoryList;
    }
    */
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String forwardRepositoryAddPage(HttpServletRequest request, HttpServletResponse response) {
        MenuUtil.setContentJspPath(request, response, "/WEB-INF/views/configuration/modifyRepository.jsp");
        return MenuUtil.getFowardUrl(request);
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public RedirectView addRepository(@ModelAttribute("repository") Repository repository, HttpServletRequest request, HttpServletResponse response) {

    	//MenuUtil.setContentJspPath(request, response, "/WEB-INF/views/request/requestMain.jsp");
    	repositoryService.saveRepository(repository);
    	
    	
    	return new RedirectView("/configuration/repositories/list",true);
    	//return "forward:/main/configuration/repositories/list";
		
    }
    
    
}
