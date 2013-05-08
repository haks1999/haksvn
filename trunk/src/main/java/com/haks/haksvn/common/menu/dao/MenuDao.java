package com.haks.haksvn.common.menu.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.security.util.ContextHolder;

@Repository
public class MenuDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Menu> retrieveSubMenuList(Menu menu){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				.createAlias("menuAuthorityList", "code")
				.add(Restrictions.eq("code.codeId", ContextHolder.getLoginUser().getAuthTypeCodeId()))
				.add(Restrictions.eq("parentMenuSeq", menu.getMenuSeq()))
				.add(Restrictions.neProperty("parentMenuSeq", "menuSeq"))
				.addOrder(Order.asc("menuOrder"))
				.list();
		
		return result;
	}
	
	public List<Menu> retrieveViewType(String menuUrl){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Menu> result =
				(List<Menu>)session.createCriteria(Menu.class).add(Restrictions.ilike("menuUrl", menuUrl, MatchMode.START)).list();
		
		return result;
	}
	
	public List<Menu> retrieveTopMenuList(){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				.createAlias("menuAuthorityList", "code")
				.add(Restrictions.eq("code.codeId", ContextHolder.getLoginUser().getAuthTypeCodeId()))
				.add(Restrictions.eq("menuLevel", 1))
				.addOrder(Order.asc("menuOrder"))
				.list();
		
		return result;
	}
}
