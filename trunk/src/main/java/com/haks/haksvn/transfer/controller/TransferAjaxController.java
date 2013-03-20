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
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.service.TransferService;
import com.haks.haksvn.user.model.User;

@Controller
@RequestMapping(value="/transfer")
public class TransferAjaxController {
     
    @Autowired
    private TransferService transferService;
    
    @RequestMapping(value="/request/list/{repositorySeq}", method=RequestMethod.POST, headers = "Accept=application/json", produces="application/json")
    public @ResponseBody Paging<List<Transfer>> retrieveTransferList(@ModelAttribute("paging") Paging<Transfer> paging,
										    		@RequestParam(value = "rUser", required = false, defaultValue="") String requestUserId,
													@RequestParam(value = "sCode", required = false, defaultValue="") String transferStateCodeId,
													@PathVariable int repositorySeq) throws HaksvnException {
    	Transfer transfer = Transfer.Builder.getBuilder(new Transfer()).repositorySeq(repositorySeq)
    		.transferStateCode(Code.Builder.getBuilder(new Code()).codeId(transferStateCodeId).build())
    		.requestUser(User.Builder.getBuilder(new User()).userId(requestUserId).build()).build();
    	paging.setModel(transfer);
    	Paging<List<Transfer>> transferListPaging = transferService.retrieveTransferList(paging);
    	for( Transfer resultTransfer : transferListPaging.getModel() ){
    		resultTransfer.setSourceList(null);
    	}
    	return transferListPaging;
    }
    
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
