package com.haks.haksvn.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haks.haksvn.request.service.RequestService;

@Controller
@RequestMapping(value="/main/Transfer/Request")
public class RequestController {

	@Autowired
	private RequestService service;
	
	@RequestMapping(value="/retrieveRequestList")
	public String retrieveRequestList(Model model){
		
		model.addAttribute("requestList", service.retrieveRequestList());
		
		return "request/requestMain";
	}
}
