package com.haks.haksvn.request.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haks.haksvn.request.service.RequestService;

@Controller
@RequestMapping(value="/transfer/request")
public class RequestController {

	@Autowired
	private RequestService service;
	
	@RequestMapping(value="/retrieveRequestList")
	public String retrieveRequestList(Model model, HttpServletRequest request, HttpServletResponse response){
		
		
		
		model.addAttribute("requestList", service.retrieveRequestList());
		
		return "request/requestMain";
	}
}
