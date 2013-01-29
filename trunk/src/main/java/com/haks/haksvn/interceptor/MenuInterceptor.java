package com.haks.haksvn.interceptor;

import java.util.ArrayList;
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
import org.springframework.web.servlet.view.RedirectView;

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
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String path = request.getRequestURI();
		return (path.indexOf("/resources/") < 0 && (path.indexOf(".json") < 0)); 
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {

		if(mv.getViewName() == null || "".equals(mv.getViewName())) return;
		List<MenuNode> menuList = menuService.retrieveMenuList();

        List<MenuNode> leftMenuList = new ArrayList<MenuNode>();
        
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String level[] = path.split("/");
		
		String viewPath = "/"+level[1]+"/"+level[2] + "/" + level[3];
		String targetJsp = "/WEB-INF/views/" + mv.getViewName() + ".jsp";

		ApplicationContext ac = ServletUtil.getApplicationContext(request
				.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(
				tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer
				.getAttributeContext(tilesRequest);
		attrContext.putAttribute("content", new Attribute(targetJsp), true);


        String selectedMenuNameLevel1 = "";
        String selectedMenuNameLevel2 = "";
        String selectedMenuNameLevel3 = "";
				
		if("00".equals(menuService.retireveViewType(viewPath))){
			// ecache 로 변경할 것 menuList 포함
			//String requestJspPath = "";
			for( MenuNode menuLevel1 : menuList ){
				for( MenuNode menuLevel2 : menuLevel1.getSubMenuList() ){
					String[] splitedMenuNames = menuLevel2.getMenuUrl().split("/");
					String exactMenuLevel2Name = splitedMenuNames[1] + "/" + splitedMenuNames[2];
					if( (level[1]+"/"+level[2]).equals(exactMenuLevel2Name) ){
						selectedMenuNameLevel1 = menuLevel1.getMenuName();
						selectedMenuNameLevel2 = menuLevel2.getMenuName();
						//requestJspPath = menuLevel2.getMenuJspPath();
						break;
					}
				}
			}
			
			mv.setViewName("main");
		}
		else{
			// ecache 로 변경할 것 menuList 포함
			//String requestJspPath = "";
			for( MenuNode menuLevel1 : menuList ){
				for( MenuNode menuLevel2 : menuLevel1.getSubMenuList() ){
					for( MenuNode menuLevel3 : menuLevel2.getSubMenuList() ){
						if( ("/"+level[1]+"/"+level[2] + "/" + level[3]).equals(menuLevel3.getMenuUrl()) ){
							//requestJspPath = menuLevel3.getMenuJspPath();
							leftMenuList = menuLevel2.getSubMenuList();
							selectedMenuNameLevel1 = menuLevel1.getMenuName();
							selectedMenuNameLevel2 = menuLevel2.getMenuName();
							selectedMenuNameLevel3 = menuLevel3.getMenuName();
							break;
						}
					}
				}
			}
			
			mv.setViewName("mainleftmenu");
		}
		

		mv.addObject("menuList", menuList);
		mv.addObject("leftMenuList", leftMenuList);
		mv.addObject("selectedMenuNameLevel1", selectedMenuNameLevel1);
		mv.addObject("selectedMenuNameLevel2", selectedMenuNameLevel2);
		mv.addObject("selectedMenuNameLevel3", selectedMenuNameLevel3);
	}

}
