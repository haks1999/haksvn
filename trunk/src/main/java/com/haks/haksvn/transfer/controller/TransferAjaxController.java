package com.haks.haksvn.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.transfer.service.TransferService;

@Controller
@RequestMapping(value="/transfer")
public class TransferAjaxController {
     
    @Autowired
    private TransferService transferService;
    
    
    @ExceptionHandler(Exception.class)
    public @ResponseBody ResultMessage exceptionHandler(Exception e) {
    	e.printStackTrace();
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
}
