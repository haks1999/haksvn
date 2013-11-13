package com.haks.haksvn.transfer.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.haks.haksvn.common.code.service.CodeService;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferStateAuth;
import com.haks.haksvn.transfer.service.TransferService;

@Controller
@RequestMapping(value="/transfer")
public class TransferController {

	@Autowired
    private RepositoryService repositoryService;
	@Autowired
	private TransferService transferService;
	@Autowired
	private CodeService codeService;
    
	@RequestMapping(value="/request/list", method=RequestMethod.GET)
    public ModelAndView forwardTransferListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + repositoryList.get(0).getRepositoryKey() + "?rUser="+ContextHolder.getLoginUser().getUserId(), true));
    	}else{
    		return new ModelAndView("/transfer/listTransfer");
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardTransferListPage( ModelMap model,
    							@RequestParam(value = "rUser", required = false, defaultValue="") String requestUserId,
    							@RequestParam(value = "sCode", required = false, defaultValue="") String transferStateCodeId,
    							@RequestParam(value = "path", required = false, defaultValue="") String path,
    							RedirectAttributes redirectAttributes,
    							@PathVariable String repositoryKey) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
        for( Repository repository : repositoryList ){
        	if( repository.getRepositoryKey().equals(repositoryKey) ){
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
	
 
	@RequestMapping(value="/request/list/{repositoryKey}/add")
    public String forwardTransferAddPage(ModelMap model, 
    										@ModelAttribute("transfer") Transfer transfer,
    										@PathVariable String repositoryKey) {
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
		model.addAttribute("repositoryList", repositoryList );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
		transfer.setRepositoryKey(repositoryKey);
    	model.addAttribute("tranfer", transfer );
    	model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
    	return "/transfer/modifyTransfer";
    }
	
	@RequestMapping(value="/request/list/{repositoryKey}/{transferSeq}")
    public String forwardTransferDetailPage(ModelMap model, 
    										@PathVariable String repositoryKey,
    										@PathVariable int transferSeq) {
		Transfer transfer = transferService.retrieveTransferDetail(Transfer.Builder.getBuilder().repositoryKey(repositoryKey).transferSeq(transferSeq).build());
		model.addAttribute("repositoryList", repositoryService.retrieveAccesibleActiveRepositoryList() );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
		model.addAttribute("transfer", transfer);
		model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
		model.addAttribute("useMailNoticeRequest", codeService.retrieveCode(CodeUtils.getMailNoticeTransferRequestCodeId()).getCodeValue());
		transfer.setSourceList(null);	// lazy loading
    	return "/transfer/modifyTransfer";
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/save"}, method=RequestMethod.POST)
    public ModelAndView saveTransfer(ModelMap model, 
    									@ModelAttribute("transfer") @Valid Transfer transfer, 
    									BindingResult result,
    									@PathVariable String repositoryKey) throws Exception{
		transfer.setRepositoryKey(repositoryKey);
		transfer.setTransferGroup(null);
    	if( result.hasErrors() ){
    		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    		model.addAttribute("repositoryList", repositoryList );
    		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
        	model.addAttribute("tranfer", transfer );
        	model.addAttribute("transferStateAuth", TransferStateAuth.Builder.getBuilder().transfer(transfer).build());
        	model.addAttribute("repositoryKey", repositoryKey );
    		return new ModelAndView("/transfer/modifyTransfer");
    	}else{
    		transfer = transferService.saveTransfer(transfer);
    		String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    		return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    		
    	}
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/delete"}, method=RequestMethod.POST)
    public ModelAndView deleteTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable String repositoryKey){
    	transfer = transferService.deleteTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/request"}, method=RequestMethod.POST)
    public ModelAndView requestTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable String repositoryKey,
    									@RequestParam(value = "noticeUserList", required = false) String[] noticeUserIdList){
    	transfer = transferService.requestTransfer(transfer, noticeUserIdList);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/requestCancel"}, method=RequestMethod.POST)
    public ModelAndView requestCancelTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable String repositoryKey){
    	transfer = transferService.requestCancelTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/approveCancel"}, method=RequestMethod.POST)
    public ModelAndView approveCancelTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable String repositoryKey){
    	transfer = transferService.approveCancelTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    }
	
	@RequestMapping(value={"/request/list/{repositoryKey}/reject"}, method=RequestMethod.POST)
    public ModelAndView rejectTransfer(ModelMap model, 
    									@ModelAttribute("transfer") Transfer transfer, 
    									@PathVariable String repositoryKey){
    	transfer = transferService.rejectTransfer(transfer);
    	String param = "?rUser=" + ContextHolder.getLoginUser().getUserId() + "&sCode=" + transfer.getTransferStateCode().getCodeId();
    	return new ModelAndView(new RedirectView("/transfer/request/list/" + transfer.getRepositoryKey() + param, true));
    }
}
