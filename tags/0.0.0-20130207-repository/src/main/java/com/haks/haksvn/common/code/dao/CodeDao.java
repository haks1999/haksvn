package com.haks.haksvn.common.code.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.code.model.Code;

@Repository
public class CodeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Code> retrieveCodeListByCodeGroup(Code code) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Code> result = 
					session.createCriteria(Code.class)
				.add(Restrictions.eq("codeGroup", code.getCodeGroup()))
				.addOrder(Order.asc("codeOrder"))
				.list();
		
		return result;
	}
	
	public List<Code> retrieveCodeList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Code> result = 
					session.createCriteria(Code.class)
				.addOrder(Order.asc("codeGroup"))
				.addOrder(Order.asc("codeOrder"))
				.list();
		
		return result;
	}
	
}
