package com.haks.haksvn.source.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.SVNSource;

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
	
	@RequestMapping(value={"/browse/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", "");
        return "/source/sourceBrowse";
    }
	
	//@RequestMapping(value={"/browse/{repositorySeq}/{path}"}, method=RequestMethod.GET)
	@RequestMapping(value={"/browse/{repositorySeq}/**"}, method=RequestMethod.GET)
	//@RequestMapping(value={"/browse/{repositorySeq}/{path:.*}"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							HttpServletRequest request,
    							@PathVariable int repositorySeq) {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		path = path.replaceFirst("/source/browse/" + String.valueOf(repositorySeq),"");
		if(path.startsWith("/")) path=path.replaceFirst("/","");
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", path);
        return "/source/sourceBrowse";
    }
	
	@RequestMapping(value="/changes", method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/source/listChange";
    }
	
	/*
	@RequestMapping(value="/changes/{repositorySeq}")
    public String forwardSourceChangePage(@PathVariable int repositorySeq,
    										@ModelAttribute("paging") Paging<SVNSource> paging){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path("").build();
		paging.setModel(svnSource);
		return sourceService.retrieveSVNSourceLogList(repositorySeq, paging);
    }
 */
	
 
}
