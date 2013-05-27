package com.haks.haksvn.transfer.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.service.TaggingService;
import com.haks.haksvn.user.model.User;

@Controller
@RequestMapping(value="/transfer")
public class TaggingAjaxController {
     
    @Autowired
    private TaggingService taggingService;
    
    @RequestMapping(value="/tagging/list/{repositorySeq}", method=RequestMethod.POST, headers = "Accept=application/json", produces="application/json")
    public @ResponseBody Paging<List<Tagging>> retrieveTransferList(@ModelAttribute("paging") Paging<Tagging> paging,
										    		@RequestParam(value = "tUser", required = false, defaultValue="") String taggingUserId,
													@RequestParam(value = "tCode", required = false, defaultValue="") String taggingTypeCodeId,
													@PathVariable int repositorySeq) throws HaksvnException {
    	
    	Tagging tagging = Tagging.Builder.getBuilder().repositorySeq(repositorySeq)
    			.taggingTypeCode(Code.Builder.getBuilder().codeId(taggingTypeCodeId).build())
    			.taggingUser(User.Builder.getBuilder().userId(taggingUserId).build()).build();
    	paging.setModel(tagging);
    	
    	Paging<List<Tagging>> taggingListPaging = taggingService.retrieveTaggingList(paging);
    	return taggingListPaging;
    }
    
    
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}