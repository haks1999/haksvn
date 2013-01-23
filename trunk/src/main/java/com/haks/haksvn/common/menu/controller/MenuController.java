package com.haks.haksvn.common.menu.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.menu.service.MenuService;

@Controller
public class MenuController {
     
    @Autowired
    private MenuService menuService;
     
    
    /*
    @RequestMapping(value="/main/{menuNameLevel1}/{menuNameLevel2}")
    public String retrieveMenuList(@PathVariable String menuNameLevel1, @PathVariable String menuNameLevel2, Model model, HttpServletRequest request, HttpServletResponse response) {
        Map menuMap = menuService.retrieveMenuList();
         
		model.addAttribute("menuListLevel1", menuMap.get("menuListLevel1"));
		model.addAttribute("menuListLevel2", menuMap.get("menuListLevel2"));
		model.addAttribute("selectedMenuNameLevel1", menuNameLevel1 );
		model.addAttribute("selectedMenuNameLevel2", menuNameLevel2 );
		
		List<Menu> menuList = (List)menuMap.get("menuListLevel1");
		
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		
		attrContext.putAttribute( "content",new Attribute("/WEB-INF/views/template_table.jsp"), true);
		
		return "index";
    }
    */
    
    @RequestMapping(value="/main/{menuNameLevel1}/{menuNameLevel2}")
    public String retrieveMenuList(@PathVariable String menuNameLevel1, @PathVariable String menuNameLevel2, Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Menu> menuList = menuService.retrieveMenuList();
         
		model.addAttribute("menuList", menuList);
		model.addAttribute("selectedMenuNameLevel1", menuNameLevel1 );
		model.addAttribute("selectedMenuNameLevel2", menuNameLevel2 );
		
		// ecache 로 변경할 것 menuList 포함
		String requestJspPath = "";
		for( Menu menuLevel1 : menuList ){
			for( Menu menuLevel2 : menuLevel1.getSubMenus() ){
				if( (menuNameLevel1+"/"+menuNameLevel2).equals(menuLevel2.getMenuUrl()) ){
					requestJspPath = menuLevel2.getMenuJspPath();
					break;
				}
			}
		}
		
		ApplicationContext ac = ServletUtil.getApplicationContext(request.getSession().getServletContext());
		TilesContainer tilesContainer = TilesAccess.getContainer(ac);
		Request tilesRequest = new ServletRequest(tilesContainer.getApplicationContext(), request, response);
		AttributeContext attrContext = tilesContainer.getAttributeContext(tilesRequest);
		
		attrContext.putAttribute( "content",new Attribute(requestJspPath), true);
		
		return "main";
    }
}
