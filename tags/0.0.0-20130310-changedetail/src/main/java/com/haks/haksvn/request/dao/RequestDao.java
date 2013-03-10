package com.haks.haksvn.request.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.request.model.Request;

@Repository
public class RequestDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Request> retrieveRequestList(){
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Request> result = session.createCriteria(Request.class)
				.addOrder(Order.asc("transferSeq"))
				.list();
		
		return result;
	}
}
