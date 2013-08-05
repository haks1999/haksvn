package com.haks.haksvn.source.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.source.model.Review;
import com.haks.haksvn.source.service.ReviewService;

@Controller
@RequestMapping(value="/source")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
    
	@RequestMapping(value={"/review/{repositoryKey}/{revision}"}, method=RequestMethod.POST)
    public ModelAndView saveReview(ModelMap model, @ModelAttribute("review") @Valid Review review, BindingResult result) throws Exception{
    	if( result.hasErrors() ){
    		return new ModelAndView("/repository/modifyRepository");
    	}else{
    		//repositoryService.saveRepository(repository);
    		return new ModelAndView(new RedirectView("/configuration/repositories/list", true));
    		
    	}
    }
	
	
}
