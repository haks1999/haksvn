package com.haks.haksvn.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.model.TransferGroup;
import com.haks.haksvn.transfer.model.TransferGroupStateAuth;

@Controller
@RequestMapping(value="/transfer")
public class TransferGroupController {

	@Autowired
    private RepositoryService repositoryService;
    
	@RequestMapping(value="/requestGroup/list", method=RequestMethod.GET)
    public ModelAndView forwardTransferGroupListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/requestGroup/list/" + repositoryList.get(0).getRepositorySeq(), true));
    	}else{
    		return new ModelAndView("/transfer/listTransferGroup");
    	}
    }
	
	@RequestMapping(value={"/requestGroup/list/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardTransferGroupListPage( ModelMap model,
    							@RequestParam(value = "sCode", required = false, defaultValue="") String transferStateCodeId,
    							@RequestParam(value = "tCode", required = false, defaultValue="") String transferTypeCodeId,
    							@RequestParam(value = "title", required = false, defaultValue="") String title,
    							RedirectAttributes redirectAttributes,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("transferGroupStateCodeId", transferStateCodeId);
    	model.addAttribute("transferGroupTypeCodeId", transferTypeCodeId);
    	model.addAttribute("title", title);
    	
        return "/transfer/listTransferGroup";
    }
	
 
	@RequestMapping(value="/requestGroup/list/{repositorySeq}/add")
    public String forwardTransferGroupAddPage(ModelMap model, 
    										@ModelAttribute("transferGroup") TransferGroup transferGroup,
    										@PathVariable int repositorySeq) {
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
		model.addAttribute("repositoryList", repositoryList );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq));
		transferGroup.setRepositorySeq(repositorySeq);
    	model.addAttribute("transferGroup", transferGroup );
    	model.addAttribute("transferGroupStateAuth", TransferGroupStateAuth.Builder.getBuilder().transferGroup(transferGroup).build());
    	return "/transfer/modifyTransferGroup";
    }
	
}
