package com.haks.haksvn.interceptor;

import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.menu.model.MenuNode;
import com.haks.haksvn.common.menu.service.MenuService;

public class MenuInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private MenuService menuService;
    
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView mv) throws Exception {
		
        List<MenuNode> menuList = menuService.retrieveMenuList();
        
        String path = request.getRequestURI();
        
        String level[] = path.substring(path.lastIndexOf("main")).split("/");
                
		mv.addObject("menuList", menuList);
		mv.addObject("selectedMenuNameLevel1", level[1] );
		mv.addObject("selectedMenuNameLevel2", level[2] );
		
		String targetJsp = "/WEB-INF/views/" + mv.getViewName() + ".jsp";
		
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		attrContext.putAttribute( "content",new Attribute(targetJsp), true);
		
		mv.setViewName("main");
	}

}
