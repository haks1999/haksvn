package com.haks.haksvn.common.menu.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.ehcache.annotations.Cacheable;
import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.security.model.LoginUser;
import com.haks.haksvn.common.security.util.ContextHolder;

@Repository
public class MenuDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Cacheable(cacheName="menuCache")
	public List<Menu> retrieveSubMenuList(Menu menu){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				.createAlias("menuAuthorityList", "code")
				.add(Restrictions.eq("code.codeId", ContextHolder.getLoginUser().getAuthTypeCodeId()))
				.add(Restrictions.eq("parentMenuSeq", menu.getMenuSeq()))
				.add(Restrictions.ne("viewType", CodeUtils.getInvisibleMenuViewTypeCodeId()))
				.add(Restrictions.neProperty("parentMenuSeq", "menuSeq"))
				.addOrder(Order.asc("menuOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="menuCache")
	public List<Menu> retrieveViewType(String menuUrl){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Menu> result =
				(List<Menu>)session.createCriteria(Menu.class).add(Restrictions.ilike("menuUrl", menuUrl, MatchMode.START)).list();
		
		return result;
	}
	
	@Cacheable(cacheName="menuCache")
	public List<Menu> retrieveTopMenuList( LoginUser loginUser ){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				.createAlias("menuAuthorityList", "code")
				.add(Restrictions.eq("code.codeId", loginUser.getAuthTypeCodeId()))
				.add(Restrictions.eq("menuLevel", 1))
				.addOrder(Order.asc("menuOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="menuCache")
	public List<Menu> retrieveMenuList(){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				.list();
		
		// fetch subselect manually
		for( Menu menu : result ){
			for( Code code : menu.getMenuAuthorityList()){
				code.getCodeName();
			}
		}
		
		return result;
	}
}
