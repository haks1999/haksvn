package com.haks.haksvn.common.code.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.haks.haksvn.common.code.model.Code;

@Repository
public class CodeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Cacheable(cacheName="codeCache")
	public List<Code> retrieveCodeListByCodeGroup(Code code) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Code> result = 
					session.createCriteria(Code.class)
				.add(Restrictions.eq("codeGroup", code.getCodeGroup()))
				.addOrder(Order.asc("codeOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="codeCache")
	public List<Code> retrieveCodeList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Code> result = 
					session.createCriteria(Code.class)
				.addOrder(Order.asc("codeGroup"))
				.addOrder(Order.asc("codeOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="codeCache")
	public Code retrieveCode(String codeId){
		Session session = sessionFactory.getCurrentSession();
		return (Code)session.get(Code.class, codeId);
	}
	
	@TriggersRemove(cacheName="codeCache", removeAll=true)
	public void saveCode(Code code){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(code);
	}
	
}
