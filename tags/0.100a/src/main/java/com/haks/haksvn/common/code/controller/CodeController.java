package com.haks.haksvn.common.code.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/common/code")
public class CodeController {
     
    
    @RequestMapping(value="/list/{codeGroup}", method=RequestMethod.GET)
    public String forwardRepositoryModifyPage(@PathVariable String codeGroup, Model model, HttpServletRequest request, HttpServletResponse response) {

    	return "common/codeList";
    }
     
    
}
