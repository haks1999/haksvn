package com.haks.haksvn.common.menu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.haks.haksvn.common.menu.model.MenuNode;
import com.haks.haksvn.common.menu.service.MenuService;
import com.haks.haksvn.common.menu.util.MenuUtil;

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
        List<MenuNode> menuList = menuService.retrieveMenuList();
        String selectedMenuNameLevel1 = "";
        String selectedMenuNameLevel2 = "";
		
		// ecache 로 변경할 것 menuList 포함
		//String requestJspPath = "";
		for( MenuNode menuLevel1 : menuList ){
			for( MenuNode menuLevel2 : menuLevel1.getSubMenuList() ){
				String[] splitedMenuNames = menuLevel2.getMenuUrl().split("/");
				String exactMenuLevel2Name = splitedMenuNames[1] + "/" + splitedMenuNames[2];
				if( (menuNameLevel1+"/"+menuNameLevel2).equals(exactMenuLevel2Name) ){
					selectedMenuNameLevel1 = menuLevel1.getMenuName();
					selectedMenuNameLevel2 = menuLevel2.getMenuName();
					//requestJspPath = menuLevel2.getMenuJspPath();
					break;
				}
			}
		}
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("selectedMenuNameLevel1", selectedMenuNameLevel1);
		model.addAttribute("selectedMenuNameLevel2", selectedMenuNameLevel2);
		
		
		//MenuUtil.setContentJspPath(request, response, requestJspPath);
		return "main";
    }
    
    @RequestMapping(value="/main/{menuNameLevel1}/{menuNameLevel2}/{menuNameLevel3}")
    public String retrieveMenuWithLeftList(@PathVariable String menuNameLevel1, 
    										@PathVariable String menuNameLevel2, 
    										@PathVariable String menuNameLevel3,
    										Model model, HttpServletRequest request, HttpServletResponse response) {
        List<MenuNode> menuList = menuService.retrieveMenuList();
        List<MenuNode> leftMenuList = new ArrayList<MenuNode>();
        
        String selectedMenuNameLevel1 = "";
        String selectedMenuNameLevel2 = "";
        String selectedMenuNameLevel3 = "";
         
		// ecache 로 변경할 것 menuList 포함
		//String requestJspPath = "";
		for( MenuNode menuLevel1 : menuList ){
			for( MenuNode menuLevel2 : menuLevel1.getSubMenuList() ){
				for( MenuNode menuLevel3 : menuLevel2.getSubMenuList() ){
					if( ("/"+menuNameLevel1+"/"+menuNameLevel2 + "/" + menuNameLevel3).equals(menuLevel3.getMenuUrl()) ){
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
		
		model.addAttribute("menuList", menuList);
		model.addAttribute("leftMenuList", leftMenuList);
		model.addAttribute("selectedMenuNameLevel1", selectedMenuNameLevel1 );
		model.addAttribute("selectedMenuNameLevel2", selectedMenuNameLevel2 );
		model.addAttribute("selectedMenuNameLevel3", selectedMenuNameLevel3 );
		
		//MenuUtil.setContentJspPath(request, response, requestJspPath);
		
		return "mainleftmenu";
    }
}
