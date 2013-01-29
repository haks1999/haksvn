package com.haks.haksvn.common.menu.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;

public class MenuUtil {

	
	public static void setContentJspPath( HttpServletRequest request, HttpServletResponse response, String jspPath ){
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		Attribute currentContent = attrContext.getAttribute("content");
		if( currentContent == null ){
			attrContext.putAttribute( "content",new Attribute(jspPath), true);
		}
	}
	
	public static String getFowardUrl( HttpServletRequest request ){
		return "forward:/main" + request.getRequestURI().substring(request.getContextPath().length());
	}
}
