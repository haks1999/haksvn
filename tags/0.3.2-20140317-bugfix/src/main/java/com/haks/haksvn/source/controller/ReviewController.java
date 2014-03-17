package com.haks.haksvn.source.controller;

import java.util.List;

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

@Controller
@RequestMapping(value="/source")
public class ReviewController {

	@Autowired
    private RepositoryService repositoryService;
	
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
	
	@RequestMapping(value={"/reviewRequest/list/{repositoryKey}"}, method=RequestMethod.GET )
    public String forwardReviewRequestListPage( ModelMap model,
					    		@RequestParam(value = "rUser", required = false, defaultValue="") String reviwerId,
								@RequestParam(value = "qUser", required = false, defaultValue="") String requestorId,
    							@PathVariable String repositoryKey) {
		
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
        for( Repository repository : repositoryList ){
        	if( repository.getRepositoryKey().equals(repositoryKey) ){
        		model.addAttribute("userList", repository.getUserList());
        		break;
        	}
        }
        model.addAttribute("reviewerId", reviwerId);
        model.addAttribute("requestorId", requestorId);
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	
        return "/source/listReviewRequest";
    }
	
}
