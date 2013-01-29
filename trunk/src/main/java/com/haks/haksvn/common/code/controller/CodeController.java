package com.haks.haksvn.common.code.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.common.menu.util.MenuUtil;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;

@Controller
@RequestMapping(value="/common/code")
public class CodeController {
     
    
    @RequestMapping(value="/list/{codeGroup}", method=RequestMethod.GET)
    public String forwardRepositoryModifyPage(@PathVariable String codeGroup, Model model, HttpServletRequest request, HttpServletResponse response) {

    	return "common/codeList";
    }
     
    
}
