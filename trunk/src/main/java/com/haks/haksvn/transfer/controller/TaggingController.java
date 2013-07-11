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

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.model.TaggingAuth;
import com.haks.haksvn.transfer.service.TaggingService;

@Controller
@RequestMapping(value="/transfer")
public class TaggingController {

	@Autowired
    private RepositoryService repositoryService;
	
	@Autowired
    private TaggingService taggingService;
    
	@RequestMapping(value="/tagging/list", method=RequestMethod.GET)
    public ModelAndView forwardTaggingListPage( ModelMap model, RedirectAttributes redirectAttributes ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/transfer/tagging/list/" + repositoryList.get(0).getRepositoryKey(), true));
    	}else{
    		return new ModelAndView("/transfer/listTagging");
    	}
    }
	
	@RequestMapping(value={"/tagging/list/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardTaggingListPage( ModelMap model,
    							@RequestParam(value = "tUser", required = false, defaultValue="") String taggingUserId,
    							@RequestParam(value = "tCode", required = false, defaultValue="") String taggingTypeCodeId,
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
    	model.addAttribute("taggingUserId", taggingUserId);
    	model.addAttribute("taggingTypeCodeId", taggingTypeCodeId);
    	model.addAttribute("taggingAuth", TaggingAuth.Builder.getBuilder().tagging(Tagging.Builder.getBuilder().build()).build());
        return "/transfer/listTagging";
    }
	
	@RequestMapping(value="/tagging/list/{repositoryKey}/add")
    public String forwardTaggingAddPage(ModelMap model, 
    										@ModelAttribute("tagging") Tagging tagging,
    										@PathVariable String repositoryKey) {
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
		model.addAttribute("repositoryList", repositoryList );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
		tagging.setRepositoryKey(repositoryKey);
    	model.addAttribute("tagging", tagging);
    	model.addAttribute("taggingAuth", TaggingAuth.Builder.getBuilder().tagging(tagging).build());
    	return "/transfer/modifyTagging";
    }
	
	@RequestMapping(value="/tagging/list/{repositoryKey}/{taggingSeq}")
    public String forwardTaggingDetailPage(ModelMap model, 
    										@PathVariable String repositoryKey,
    										@PathVariable int taggingSeq) {
		Tagging tagging = taggingService.retrieveTagging(Tagging.Builder.getBuilder().repositoryKey(repositoryKey).taggingSeq(taggingSeq).build());
		model.addAttribute("repositoryList", repositoryService.retrieveAccesibleActiveRepositoryList() );
		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
		model.addAttribute("tagging", tagging);
		model.addAttribute("taggingAuth", TaggingAuth.Builder.getBuilder().tagging(tagging).build());
    	return "/transfer/modifyTagging";
    }
	
	
	@RequestMapping(value={"/tagging/list/{repositoryKey}/create"}, method=RequestMethod.POST)
    public ModelAndView createTagging(ModelMap model, 
    									@ModelAttribute("tagging") @Valid Tagging tagging, 
    									BindingResult result,
    									@PathVariable String repositoryKey) throws Exception{
    	if( result.hasErrors() ){
    		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    		model.addAttribute("repositoryList", repositoryList );
    		model.addAttribute("repository", repositoryService.retrieveRepositoryByRepositoryKey(repositoryKey));
    		tagging.setRepositoryKey(tagging.getRepositoryKey());
        	model.addAttribute("tagging", tagging);
        	model.addAttribute("taggingAuth", TaggingAuth.Builder.getBuilder().tagging(tagging).build());
        	model.addAttribute("repositoryKey", repositoryKey );
    		return new ModelAndView("/transfer/modifyTagging");
    	}else{
    		tagging = taggingService.createTagging(tagging);
    		return new ModelAndView(new RedirectView("/transfer/tagging/list/" + tagging.getRepositoryKey() , true));
    		
    	}
    }
	
	@RequestMapping(value={"/tagging/list/{repositoryKey}/restore"}, method=RequestMethod.POST)
    public ModelAndView restoreTagging(ModelMap model, 
    									@ModelAttribute("tagging") Tagging tagging, 
    									BindingResult result,
    									@PathVariable String repositoryKey) throws Exception{
   		taggingService.restoreTagging(tagging);
   		return new ModelAndView(new RedirectView("/transfer/tagging/list/" + repositoryKey , true));
    }
	
 
}
