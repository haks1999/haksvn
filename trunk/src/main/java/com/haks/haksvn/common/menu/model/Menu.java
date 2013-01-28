package com.haks.haksvn.common.menu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="menu")
public class Menu {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "menu_seq",unique = true, nullable = false)
    protected int menuSeq;
	
	@Column(name = "menu_name",nullable = false)
	protected String menuName;
	
	@Column(name = "menu_url",nullable = false)
	protected String menuUrl;
	
	@Column(name = "menu_jsp_path",nullable = true)
	protected String menuJspPath;
	
	@Column(name = "menu_order",nullable = false)
	protected String menuOrder;
	
	@Column(name = "menu_level",nullable = false)
	protected int menuLevel;
	
	//@ManyToOne(cascade = { CascadeType.ALL })
	//@JoinColumn(name = "parent_menu_seq", nullable=true, insertable = false, updatable = false)
	
	/*
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinTable(name = "menu",
		    joinColumns = {
		      @JoinColumn(name="menu_seq")           
		    },
		    inverseJoinColumns = {
		      @JoinColumn(name="parent_menu_seq")
		    }
		  )
	private Menu menu;
	*/
	
	@Column(name = "parent_menu_seq",nullable = true, unique = false)
	protected int parentMenuSeq;
	
	//@OneToMany(mappedBy = "menu", fetch=FetchType.EAGER)
	/*
	@OneToMany(fetch=FetchType.EAGER)
	  @JoinTable(name = "menu",
	    joinColumns = {
	      @JoinColumn(name="menu_seq")           
	    },
	    inverseJoinColumns = {
	      @JoinColumn(name="parent_menu_seq")
	    }
	  )
	  */
	/*
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(
		    joinColumns = {
		      @JoinColumn(name="menu_seq")           
		    },
		    inverseJoinColumns = {
		      @JoinColumn(name="parent_menu_seq")
		    })
		    */
	//private List<Menu> subMenus = new ArrayList<Menu>();
	
	public Menu(){
		
	}

	public int getMenuSeq() {
		return menuSeq;
	}

	public void setMenuSeq(int menuSeq) {
		this.menuSeq = menuSeq;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuJspPath() {
		return menuJspPath;
	}

	public void setMenuJspPath(String menuJspPath) {
		this.menuJspPath = menuJspPath;
	}
	
	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}
	
	public int getMenuLevel(){
		return menuLevel;
	}
	
	public void setMenuLevel(int menuLevel){
		this.menuLevel = menuLevel;
	}
	
	/*
	public Menu getMenu(){
		return menu;
	}
	
	public void setMenu(Menu menu){
		this.menu = menu;
	}
	
	public List<Menu> getSubMenus(){
		return subMenus;
	}
	
	public void setSubMenus(List<Menu> subMenus){
		this.subMenus = subMenus;
	}
	*/
	
	public int getParentMenuSeq(){
		return parentMenuSeq;
	}
	
	public void setParentMenuId(int parentMenuSeq){
		this.parentMenuSeq = parentMenuSeq;
	}
	
}
