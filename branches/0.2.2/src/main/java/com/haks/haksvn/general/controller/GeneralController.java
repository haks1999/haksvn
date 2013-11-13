package com.haks.haksvn.general.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.general.model.MailConfiguration;
import com.haks.haksvn.general.service.GeneralService;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/configuration/general")
public class GeneralController {
     
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private GeneralService generalService;
    
    @RequestMapping(value="/commitLog", method=RequestMethod.GET)
    public String forwardCommitLogPage( ModelMap model ) {
    	List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/general/modifyCommitLog";
    }
    
    @RequestMapping(value="/mail", method=RequestMethod.GET)
    public String forwardMailConfigurationPage( ModelMap model ) {
    	model.addAttribute("mailConfiguration", generalService.retrieveMailConfiguration());
        return "/general/modifyMail";
    }
    
    @RequestMapping(value="/mail", method=RequestMethod.POST)
    public ModelAndView saveMailConfiguration(ModelMap model, 
    								@ModelAttribute("mailConfiguration") MailConfiguration mailConfiguration) {
    	generalService.saveMailConfiguration(mailConfiguration);
    	return new ModelAndView(new RedirectView("/configuration/general/mail", true));
    }
    
    @RequestMapping(value="/mailNotice", method=RequestMethod.POST)
    public ModelAndView saveMailNoticeConfiguration(ModelMap model, 
    									HttpServletRequest request) {
    	Map<String, String[]> paramMap = request.getParameterMap();
    	List<Code> mailNoticeConfList = new ArrayList<Code>(0);
    	for (Map.Entry<String, String[]> entry : paramMap.entrySet()){
    		mailNoticeConfList.add(Code.Builder.getBuilder().codeId(entry.getKey()).codeValue(entry.getValue()[0]).build());
    	}
    	generalService.saveMailNoticeConfiguration(mailNoticeConfList);
    	return new ModelAndView(new RedirectView("/configuration/general/mail", true));
    }
    
    @RequestMapping(value="/mailTemplate", method=RequestMethod.GET)
    public String forwardMailTemplatePage( ModelMap model ) {
    	List<Repository> repositoryList = repositoryService.retrieveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
        return "/general/modifyMailTemplate";
    }
    
}
