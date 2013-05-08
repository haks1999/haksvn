package com.haks.haksvn.general.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.general.model.CommitLogTemplate;
import com.haks.haksvn.general.service.GeneralService;

@Controller
@RequestMapping(value="/configuration/general")
public class GeneralAjaxController {
     
    @Autowired
    private GeneralService generalService;
    
    @RequestMapping(value="/commitLog/{repositorySeq}", method=RequestMethod.GET)
    public @ResponseBody CommitLogTemplate retrieveCommitLogTemplate(
													@PathVariable int repositorySeq){
    	return generalService.retrieveCommitLogTemplate(repositorySeq);
    }
    
    @RequestMapping(value="/commitLog/default", method=RequestMethod.GET)
    public @ResponseBody CommitLogTemplate retrieveDefaultCommitLogTemplate(){
    	return generalService.retrieveDefaultCommitLogTemplate();
    }
    
    @RequestMapping(value="/commitLog/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody DefaultMessage saveCommitLogTemplate(
													@PathVariable int repositorySeq,
													@ModelAttribute("commitLogTemplate") CommitLogTemplate commitLogTemplate){
    	generalService.saveCommitLogTemplate(commitLogTemplate);
    	return new ResultMessage("save success");
    }
    
    
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
