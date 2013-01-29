package com.haks.haksvn.common.menu.model;

import java.util.ArrayList;
import java.util.List;

public class MenuNode extends Menu{
	
	private List<MenuNode> subMenuList = new ArrayList<MenuNode>();
	
	public MenuNode(){}
	
	public MenuNode( Menu menu ){
		this.menuSeq = menu.getMenuSeq();
		this.menuName = menu.getMenuName();
		this.menuUrl = menu.getMenuUrl();
		this.menuOrder = menu.getMenuOrder();
		this.menuLevel = menu.getMenuLevel();
		this.parentMenuSeq = menu.getParentMenuSeq();
		
	}

	public List<MenuNode> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<MenuNode> subMenuList) {
		this.subMenuList = subMenuList;
	}
	
	

}
