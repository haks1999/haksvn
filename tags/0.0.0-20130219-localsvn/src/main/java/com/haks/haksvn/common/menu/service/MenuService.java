package com.haks.haksvn.common.menu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.menu.dao.MenuDao;
import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.menu.model.MenuNode;

@Service
@Transactional
public class MenuService {

	@Autowired
	private MenuDao menuDao;
	
	/*
	public List<Menu> retrieveMenuList(){
		
		
		List<Menu> menuList = menuDao.retrieveMenuList();
		
		return menuList;
		
	}
	*/
	
	public List<MenuNode> retrieveMenuList(){
		
		List<MenuNode> topMenuList = convertMenuToMenuNode(menuDao.retrieveTopMenuList());
		
		for( MenuNode level1Menu : topMenuList ){
			List<MenuNode> level2MenuList = retrieveSubMenuList(level1Menu);
			level1Menu.setSubMenuList(level2MenuList);
			for( MenuNode level2Menu : level2MenuList ){
				level2Menu.setSubMenuList(retrieveSubMenuList(level2Menu));
			}
		}
		
		return topMenuList;
		
	}
	
	public String retireveViewType(String menuUrl){
		
		List<Menu> list = menuDao.retrieveViewType(menuUrl);
		
		if (list.size() > 0) {
			Menu menu = list.get(0);
		
			return menu.getViewType();
		}else{
			return null;
		}
	}
	
	public List<MenuNode> retrieveSubMenuList(Menu menu){
		
		
		List<MenuNode> subMenuList = convertMenuToMenuNode(menuDao.retrieveSubMenuList(menu));
		
		return subMenuList;
		
	}
	
	private List<MenuNode> convertMenuToMenuNode( List<Menu> menuList ){
		List<MenuNode> menuNodeList = new ArrayList<MenuNode>();
		for( Menu menu : menuList ){
			menuNodeList.add( new MenuNode(menu));
		}
		return menuNodeList;
	}

	
}
