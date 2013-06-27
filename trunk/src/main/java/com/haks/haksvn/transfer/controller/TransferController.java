package com.haks.haksvn.transfer.controller;

import java.util.List;

import javax.validation.Valid;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
import com.haks.haksvn.transfer.model.TransferSource;
import com.haks.haksvn.transfer.model.TransferStateAuth;
import com.haks.haksvn.transfer.service.TransferService;

@Controller
@RequestMapping(value="/transfer")
public class TransferController {

	@Autowired
    private RepositoryService repositoryService;
	@Autowired
	private TransferService transferService;
    
	@RequestMapping(value="/request/list", method=RequestMethod.GET)
    public ModelAndView forwardTransferListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + repositoryList.get(0).getRepositorySeq() + "?rUser="+ContextHolder.getLoginUser().getUserId(), true));
    	}else{
    		return new ModelAndView("/transfer/listTransfer");
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardTransferListPage( ModelMap model,
    							@RequestParam(value = "rUser", required = false, defaultValue="") String requestUserId,
    							@RequestParam(value = "sCode", required = false, defaultValue="") String transferStateCodeId,
    							@RequestParam(value = "path", required = false, defaultValue="") String path,
    							RedirectAttributes redirectAttributes,
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
    	model.addAttribute("path", path);
        return "/transfer/listTransfer";
    }
	
 
	@RequestMapping(value="/request/list/{repositorySeq}/add")
    public String forwardTransferAddPage(ModelMap model, 
    										@ModelAttribute("transfer") Transfer transfer,
    										@PathVariable int repositorySeq) {
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
		model.addAttribute("repositoryList", repositoryList );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq));
		transfer.setRepositorySeq(repositorySeq);
    	model.addAttribute("tranfer", transfer );
    	model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
    	return "/transfer/modifyTransfer";
    }
	
	@RequestMapping(value="/request/list/{repositorySeq}/{transferSeq}")
    public String forwardTransferDetailPage(ModelMap model, 
    										@PathVariable int repositorySeq,
    										@PathVariable int transferSeq) {
		Transfer transfer = transferService.retrieveTransferDetail(Transfer.Builder.getBuilder().repositorySeq(repositorySeq).transferSeq(transferSeq).build());
		model.addAttribute("repositoryList", repositoryService.retrieveAccesibleActiveRepositoryList() );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq));
		model.addAttribute("transfer", transfer);
		model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
		transfer.setSourceList(null);	// lazy loading
    	return "/transfer/modifyTransfer";
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/save"}, method=RequestMethod.POST)
    public ModelAndView saveTransfer(ModelMap model, 
    									@ModelAttribute("transfer") @Valid Transfer transfer, 
    									@RequestParam(value = "transferSourceList") String sourceListJson,
    									BindingResult result,
    									@PathVariable int repositorySeq) throws Exception{
    	if( result.hasErrors() ){
    		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    		model.addAttribute("repositoryList", repositoryList );
    		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositorySeq(repositorySeq));
    		transfer.setRepositorySeq(transfer.getRepositorySeq());
        	model.addAttribute("tranfer", transfer );
        	model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
        	model.addAttribute("repositorySeq", repositorySeq );
    		return new ModelAndView("/transfer/modifyTransfer");
    	}else{
    		ObjectMapper mapper = new ObjectMapper();
   			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       		List<TransferSource> transferSourceList = mapper.readValue(sourceListJson, new TypeReference<List<TransferSource>>(){});
       		transfer.setSourceList(transferSourceList);
    		transfer.setTransferGroup(null);
    		transfer = transferService.saveTransfer(transfer);
    		String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    		
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/delete"}, method=RequestMethod.POST)
    public ModelAndView deleteTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable int repositorySeq){
    	transfer = transferService.deleteTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/request"}, method=RequestMethod.POST)
    public ModelAndView requestTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable int repositorySeq){
    	transfer = transferService.requestTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/requestCancel"}, method=RequestMethod.POST)
    public ModelAndView requestCancelTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable int repositorySeq){
    	transfer = transferService.requestCancelTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/approve"}, method=RequestMethod.POST)
    public ModelAndView approveTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable int repositorySeq){
    	transfer = transferService.approveTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositorySeq}/reject"}, method=RequestMethod.POST)
    public ModelAndView rejectTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable int repositorySeq){
    	transfer = transferService.rejectTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositorySeq() + param, true));
    }
}
