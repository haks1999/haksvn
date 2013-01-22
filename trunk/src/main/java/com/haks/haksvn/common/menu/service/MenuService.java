package com.haks.haksvn.common.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.menu.dao.MenuDao;
import com.haks.haksvn.common.menu.model.Menu;

@Service
@Transactional
public class MenuService {

	@Autowired
	private MenuDao menuDao;
	
	
	public List<Menu> retrieveMenuList(){
		
		
		List<Menu> menuList = menuDao.retrieveMenuList();
		
		return menuList;
		
	}

	
}
