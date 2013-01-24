package com.haks.haksvn.repository.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<com.haks.haksvn.repository.model.Repository> retrieveRepositoryList() {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<com.haks.haksvn.repository.model.Repository> result = 
					session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.addOrder(Order.asc("repositorySeq"))
				.list();
		
		return result;
	}
}