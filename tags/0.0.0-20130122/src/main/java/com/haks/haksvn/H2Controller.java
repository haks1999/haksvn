package com.haks.haksvn;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class H2Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(H2Controller.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,HttpServletRequest request, HttpServletResponse response) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		model.addAttribute("content", "/WEB-INF/views/template_table.jsp" );

		
		
		// change tiles attribute value
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		
		attrContext.putAttribute( "content",new Attribute("/WEB-INF/views/template_table.jsp"), true);
		
		//return "home";
		return "tile";
	}
	
}
