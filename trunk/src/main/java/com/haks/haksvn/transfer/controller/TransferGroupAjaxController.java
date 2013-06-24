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
import com.haks.haksvn.transfer.model.TransferGroup;
import com.haks.haksvn.transfer.service.TransferGroupService;

@Controller
@RequestMapping(value="/transfer")
public class TransferGroupAjaxController {
     
    @Autowired
    private TransferGroupService transferGroupService;
    
    @RequestMapping(value="/requestGroup/list/{repositorySeq}", method=RequestMethod.POST)
    public @ResponseBody Paging<List<TransferGroup>> retrieveTransferGroupList(@ModelAttribute("paging") Paging<TransferGroup> paging,
										    		@RequestParam(value = "sCode", required = false, defaultValue="") String transferGroupStateCodeId,
													@RequestParam(value = "tCode", required = false, defaultValue="") String transferGroupTypeCodeId,
													@RequestParam(value = "title", required = false, defaultValue="") String title,
													@PathVariable int repositorySeq) throws HaksvnException {
    	TransferGroup transferGroup = TransferGroup.Builder.getBuilder().repositorySeq(repositorySeq)
    			.transferGroupStateCode(Code.Builder.getBuilder().codeId(transferGroupStateCodeId).build())
    			.transferGroupTypeCode(Code.Builder.getBuilder().codeId(transferGroupTypeCodeId).build())
    			.title(title).build();
    	paging.setModel(transferGroup);
    	Paging<List<TransferGroup>> transferGroupListPaging = transferGroupService.retrieveTransferGroupList(paging);
    	for( TransferGroup resultTransferGroup : transferGroupListPaging.getModel() ){
    		resultTransferGroup.setTransferList(null);
    	}
    	return transferGroupListPaging;
    }
    
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
