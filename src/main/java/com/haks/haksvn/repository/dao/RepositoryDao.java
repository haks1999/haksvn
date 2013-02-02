package com.haks.haksvn.repository.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
	
	public List<com.haks.haksvn.repository.model.Repository> retrieveActiveRepositoryList() {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<com.haks.haksvn.repository.model.Repository> result = 
					session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.add(Restrictions.eq("active", true))
				.addOrder(Order.asc("repositoryName"))
				.list();
		
		return result;
	}
	
	public com.haks.haksvn.repository.model.Repository retrieveRepositoryByRepositorySeq(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		
		com.haks.haksvn.repository.model.Repository result =
				(com.haks.haksvn.repository.model.Repository)session.get(com.haks.haksvn.repository.model.Repository.class, repository.getRepositorySeq());
		
		return result;
	}
	
	public com.haks.haksvn.repository.model.Repository addRepository(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		session.save(repository);
		return repository;
	}
	
	public com.haks.haksvn.repository.model.Repository updateRepository(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		session.update(repository);
		return repository;
	}
}
