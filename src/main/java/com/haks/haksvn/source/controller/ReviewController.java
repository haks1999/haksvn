package com.haks.haksvn.source.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.ReviewAuth;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.service.ReviewService;
import com.haks.haksvn.source.service.SourceService;
import com.haks.haksvn.source.util.SourceUrlRewriteUtils;
import com.haks.haksvn.source.util.SourceUtils;

@Controller
@RequestMapping(value="/source")
public class ReviewController {
         

	@Autowired
    private RepositoryService repositoryService;
	@Autowired
    private SourceService sourceService;
	@Autowired
	private ReviewService reviewService;
    
	
	
	@RequestMapping(value="/reviewRequest/list", method=RequestMethod.GET)
    public ModelAndView forwardReviewRequestListPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/source/reviewRequest/list/" + repositoryList.get(0).getRepositoryKey(), true));
    	}else{
    		return new ModelAndView("/source/listReviewRequest");
    	}
    }
	
	@RequestMapping(value={"/reviewRequest/list/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardReviewRequestListPage( ModelMap model,
    							@PathVariable String repositoryKey) {
		
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	
        return "/source/listReviewRequest";
    }
	
}
