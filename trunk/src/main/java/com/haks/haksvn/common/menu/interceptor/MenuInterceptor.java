package com.haks.haksvn.common.menu.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
System.out.println("interceptor : pre : " + request.getRequestURI());
		String path = request.getRequestURI();
		return true;
		//return (!path.startsWith("/resources/") && (path.indexOf(".json") < 0)); 
	}

	//TODO 여기 로직 다 뜯어 고칠 것
	private final String[] dynamicMenuRestPathList = {"/source/browse","/source/changes"};
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {
System.out.println("interceptor : post");		
		
		if(mv == null || mv.getViewName() == null || "".equals(mv.getViewName())) return;
		
		if(mv.getViewName().indexOf("forward:") > -1 || mv.getViewName().indexOf("redirect:") > -1) return;
		
		List<MenuNode> menuList = menuService.retrieveMenuList();
        List<MenuNode> leftMenuList = new ArrayList<MenuNode>();
		HttpSession session = request.getSession();
		
        
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String level[] = path.split("/");
		
		String viewPath = "/"+level[1]+"/"+level[2] + ((level.length > 3 ) ? ("/" + level[3]):"");
		for( String dynamicMenuRestPath : dynamicMenuRestPathList ){
			if( viewPath.startsWith(dynamicMenuRestPath)) viewPath = dynamicMenuRestPath;
		}
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
		if("menu.view.type.code.default".equals(menuService.retireveViewType(viewPath))){
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
			
			session.setAttribute("viewType", "main");
			session.setAttribute("selectedMenuNameLevel1", selectedMenuNameLevel1);
			session.setAttribute("selectedMenuNameLevel2", selectedMenuNameLevel2);
			session.setAttribute("selectedMenuNameLevel3", selectedMenuNameLevel3);
		}
		else if("menu.view.type.code.leftmenu".equals(menuService.retireveViewType(viewPath))){
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
			session.setAttribute("viewType", "mainleftmenu");
			session.setAttribute("selectedMenuNameLevel1", selectedMenuNameLevel1);
			session.setAttribute("selectedMenuNameLevel2", selectedMenuNameLevel2);
			session.setAttribute("selectedMenuNameLevel3", selectedMenuNameLevel3);
		}else{
			mv.setViewName(session.getAttribute("viewType").toString());
		}
		

		mv.addObject("menuList", menuList);
		mv.addObject("leftMenuList", leftMenuList);
		mv.addObject("selectedMenuNameLevel1", session.getAttribute("selectedMenuNameLevel1").toString());
		mv.addObject("selectedMenuNameLevel2", session.getAttribute("selectedMenuNameLevel2").toString());
		mv.addObject("selectedMenuNameLevel3", session.getAttribute("selectedMenuNameLevel3").toString());
	}

}
