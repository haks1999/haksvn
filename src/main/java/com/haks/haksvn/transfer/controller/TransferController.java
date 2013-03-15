package com.haks.haksvn.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/transfer")
public class TransferController {

	@Autowired
    private RepositoryService repositoryService;
    
	@RequestMapping(value="/request/list", method=RequestMethod.GET)
    public ModelAndView forwardTransferListPage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + repositoryList.get(0).getRepositorySeq(), true));
    	}else{
    		return new ModelAndView("/transfer/listTransfer");
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/transfer/listTransfer";
    }
	
 
}
