package com.haks.haksvn.common.menu.interceptor;

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
		return true;
	}
	
	private String[] splitUrlByLevel(HttpServletRequest request){
		String path = request.getRequestURI().substring(request.getContextPath().length());
		return path.split("/");
	}
	
	// dynamic list 는 rest 방식으로 뒤에 뭐가 많이 붙는 케이스
	private final String[] dynamicMenuRestPathList = {"/source/browse","/source/changes"};
	private String transUrlToMenuPath(HttpServletRequest request){
		String level[] = splitUrlByLevel(request);
		String viewPath = "/"+level[1]+"/"+level[2] + ((level.length > 3 ) ? ("/" + level[3]):"");
		for( String dynamicMenuRestPath : dynamicMenuRestPathList ){
			if( viewPath.startsWith(dynamicMenuRestPath)) viewPath = dynamicMenuRestPath;
		}
		return viewPath;
	}
	
	private void setTilesContent(String viewName, HttpServletRequest request, HttpServletResponse response){
		String targetJsp = "/WEB-INF/views/" + viewName + ".jsp";
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		attrContext.putAttribute("content", new Attribute(targetJsp), true);
	}

	//TODO 여기 로직 다 뜯어 고칠 것
	@Override
	public void postHandle(HttpServletRequest request,
							HttpServletResponse response, Object handler, ModelAndView mv)
											throws Exception {
		
		if(mv == null || mv.getViewName() == null || "".equals(mv.getViewName())) return;
		
		setTilesContent(mv.getViewName(), request, response);
		
		String viewPath = transUrlToMenuPath(request);
		String[] level = splitUrlByLevel(request);

		List<MenuNode> menuList = menuService.retrieveMenuNodeList();
        List<MenuNode> leftMenuList = new ArrayList<MenuNode>();
        String viewName = "main";
        String selectedMenuNameLevel1 = "";
        String selectedMenuNameLevel2 = "";
        String selectedMenuNameLevel3 = "";
		if("menu.view.type.code.default".equals(menuService.retireveViewType(viewPath))){
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
		}
		else if("menu.view.type.code.leftmenu".equals(menuService.retireveViewType(viewPath))){
			viewName = "mainleftmenu";
			for( MenuNode menuLevel1 : menuList ){
				for( MenuNode menuLevel2 : menuLevel1.getSubMenuList() ){
					for( MenuNode menuLevel3 : menuLevel2.getSubMenuList() ){
						if( ("/"+level[1]+"/"+level[2] + "/" + level[3]).equals(menuLevel3.getMenuUrl()) ){
							leftMenuList = menuLevel2.getSubMenuList();
							selectedMenuNameLevel1 = menuLevel1.getMenuName();
							selectedMenuNameLevel2 = menuLevel2.getMenuName();
							selectedMenuNameLevel3 = menuLevel3.getMenuName();
							break;
						}
					}
				}
			}
		}

		mv.setViewName(viewName);
		mv.addObject("menuList", menuList);
		mv.addObject("leftMenuList", leftMenuList);
		mv.addObject("selectedMenuNameLevel1", selectedMenuNameLevel1);
		mv.addObject("selectedMenuNameLevel2", selectedMenuNameLevel2);
		mv.addObject("selectedMenuNameLevel3", selectedMenuNameLevel3);
	}

}
