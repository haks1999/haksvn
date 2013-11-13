package com.haks.haksvn.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/transfer")
public class TraceSourceController {

	@Autowired
    private RepositoryService repositoryService;
	
	@RequestMapping(value="/traceSource/list", method=RequestMethod.GET)
    public ModelAndView forwardTaggingListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/traceSource/list/" + repositoryList.get(0).getRepositoryKey(), true));
    	}else{
    		return new ModelAndView("/transfer/listTraceSource");
    	}
    }
	
	@RequestMapping(value={"/traceSource/list/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardTaggingListPage( ModelMap model,
    							@PathVariable String repositoryKey) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
        return "/transfer/listTraceSource";
    }
	
 
}
