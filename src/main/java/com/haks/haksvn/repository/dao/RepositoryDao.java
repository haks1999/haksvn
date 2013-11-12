package com.haks.haksvn.repository.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.haks.haksvn.common.code.util.CodeUtils;

@Repository
public class RepositoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Cacheable(cacheName="repositoryCache")
	public List<com.haks.haksvn.repository.model.Repository> retrieveRepositoryList() {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<com.haks.haksvn.repository.model.Repository> result = 
					session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.addOrder(Order.asc("repositoryOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="repositoryCache")
	public List<com.haks.haksvn.repository.model.Repository> retrieveActiveRepositoryList() {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<com.haks.haksvn.repository.model.Repository> result = 
					session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.add(Restrictions.eq("active", CodeUtils.getCommonCodeY()))
				.addOrder(Order.asc("repositoryOrder"))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="repositoryCache")
	public List<com.haks.haksvn.repository.model.Repository> retrieveActiveRepositoryListByUserId(String userId) {
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<com.haks.haksvn.repository.model.Repository> result = 
					session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.add(Restrictions.eq("active", CodeUtils.getCommonCodeY()))
				.addOrder(Order.asc("repositoryOrder"))
				.createCriteria("userList")
				.add(Restrictions.eq("userId", userId))
				.list();
		
		return result;
	}
	
	@Cacheable(cacheName="repositoryCache")
	public com.haks.haksvn.repository.model.Repository retrieveActiveRepositoryByRepositoryKeyAndUserId(String repositoryKey,String userId) {
		Session session = sessionFactory.getCurrentSession();
		
		com.haks.haksvn.repository.model.Repository result = 
					(com.haks.haksvn.repository.model.Repository)session.createCriteria(com.haks.haksvn.repository.model.Repository.class)
				.add(Restrictions.eq("active", CodeUtils.getCommonCodeY()))
				.add(Restrictions.eq("repositoryKey", repositoryKey))
				.createCriteria("userList")
				.add(Restrictions.eq("userId", userId))
				.uniqueResult();
		
		return result;
	}
	
	
	@Cacheable(cacheName="repositoryCache")
	public com.haks.haksvn.repository.model.Repository retrieveRepositoryByRepositoryKey(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		com.haks.haksvn.repository.model.Repository result =
				(com.haks.haksvn.repository.model.Repository)session.get(com.haks.haksvn.repository.model.Repository.class, repository.getRepositoryKey());
		return result;
	}
	
	@TriggersRemove(cacheName="repositoryCache", removeAll=true)
	public com.haks.haksvn.repository.model.Repository addRepository(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		session.save(repository);
		return repository;
	}
	
	@TriggersRemove(cacheName="repositoryCache", removeAll=true)
	public com.haks.haksvn.repository.model.Repository updateRepository(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		session.update(repository);
		return repository;
	}
	
	@TriggersRemove(cacheName="repositoryCache", removeAll=true)
	public void deleteRepository(com.haks.haksvn.repository.model.Repository repository) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(repository);
	}
}
