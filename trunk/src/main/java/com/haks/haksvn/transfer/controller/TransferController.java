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

import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.model.Transfer;

@Controller
@RequestMapping(value="/transfer")
public class TransferController {

	@Autowired
    private RepositoryService repositoryService;
    
	@RequestMapping(value="/request/list", method=RequestMethod.GET)
    public ModelAndView forwardTransferListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	//model.addAttribute("repositoryList", repositoryList );
    	
    	if( repositoryList.size() > 0 ){
    		redirectAttributes.addFlashAttribute("rUser", ContextHolder.getLoginUser().getUserId() );
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + repositoryList.get(0).getRepositorySeq(), true));
    	}else{
    		return new ModelAndView("/transfer/listTransfer");
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardTransferListPage( ModelMap model,
    							@RequestParam(value = "rUser", required = false, defaultValue="") String requestUserId,
    							@RequestParam(value = "sCode", required = false, defaultValue="") String transferStateCodeId,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
        for( Repository repository : repositoryList ){
        	if( repository.getRepositorySeq() == repositorySeq ){
        		model.addAttribute("userList", repository.getUserList());
        		break;
        	}
        }
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("requestUserId", requestUserId);
    	model.addAttribute("transferStateCodeId", transferStateCodeId);
        return "/transfer/listTransfer";
    }
	
 
	@RequestMapping(value="/request/add/{repositorySeq}")
    public String forwardRepositoryAddPage(ModelMap model, 
    										@ModelAttribute("transfer") Transfer transfer,
    										@PathVariable int repositorySeq) {
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
		model.addAttribute("repositoryList", repositoryList );
		transfer.setRepositorySeq(repositorySeq);
    	model.addAttribute("tranfer", transfer );
    	model.addAttribute("isNew", true );
    	return "/transfer/modifyTransfer";
    }
}
