package com.haks.haksvn.transfer.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferGroup;
import com.haks.haksvn.transfer.service.TransferGroupService;
import com.haks.haksvn.transfer.service.TransferService;

@Controller
@RequestMapping(value="/transfer")
public class TransferGroupAjaxController {
     
    @Autowired
    private TransferGroupService transferGroupService;
    @Autowired
    private TransferService transferService;
    
    @RequestMapping(value="/requestGroup/list/{repositoryKey}", method=RequestMethod.POST)
    public @ResponseBody Paging<List<TransferGroup>> retrieveTransferGroupList(@ModelAttribute("paging") Paging<TransferGroup> paging,
										    		@RequestParam(value = "sCode", required = false, defaultValue="") String transferGroupStateCodeId,
													@RequestParam(value = "tCode", required = false, defaultValue="") String transferGroupTypeCodeId,
													@RequestParam(value = "title", required = false, defaultValue="") String title,
													@PathVariable String repositoryKey) throws HaksvnException {
    	TransferGroup transferGroup = TransferGroup.Builder.getBuilder().repositoryKey(repositoryKey)
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
    
    @RequestMapping(value="/requestGroup/list/{repositoryKey}/{transferGroupSeq}/requests")
    public @ResponseBody List<Transfer> retrieveAddedTransferList(@PathVariable String repositoryKey,
													@PathVariable int transferGroupSeq){
    	
    	List<Transfer> transferList = transferService.retrieveTransferListByTransferGroupSeq(transferGroupSeq);
    	for( Transfer resultTransfer : transferList ){
    		resultTransfer.setSourceList(null);
    		if( resultTransfer.getTransferGroup() !=null ) resultTransfer.setTransferGroup(null);
    	}
    	return transferList;
    }
    
    @RequestMapping(value={"/requestGroup/list/{repositoryKey}/transfer"}, method=RequestMethod.POST)
    public @ResponseBody ResultMessage transferTranferGroup(@PathVariable String repositoryKey,
    														@ModelAttribute("transferGroup") @Valid TransferGroup transferGroup){
    	
    	ResultMessage message = new ResultMessage("Transfer success");
    	transferGroupService.transferTransferGroup(transferGroup);
    	return message;
    }
    
    
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
