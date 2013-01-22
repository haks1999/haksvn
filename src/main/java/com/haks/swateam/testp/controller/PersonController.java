package com.haks.swateam.testp.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haks.swateam.testp.dao.MyDAO;
import com.haks.swateam.testp.model.Person;

@Controller
public class PersonController {
     
    @Autowired
    MyDAO myDAO;
     
    @RequestMapping(value="/addPerson")
    public String addPerson(Locale locale, Model model) {
        Person p = new Person();
        p.setFirstname("seungrin");
        p.setLastname("lee");
         
        myDAO.addPerson(p);
         
        Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
    }
}
