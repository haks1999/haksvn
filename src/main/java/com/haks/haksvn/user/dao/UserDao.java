package com.haks.haksvn.user.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.user.model.User;

@Repository
public class UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<User> retrieveUserList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<User> result = 
					session.createCriteria(User.class)
				.addOrder(Order.asc("userName"))
				.list();
		
		return result;
	}
	
	
	public User retrieveUserByUserSeq(User user) {
		Session session = sessionFactory.getCurrentSession();
		User result = (User)session.get(User.class, user.getUserSeq());
		return result;
	}
	
	public User retrieveUserByUserId(User user) {
		Session session = sessionFactory.getCurrentSession();
		User result = (User)session.createCriteria(User.class)
				.add(Restrictions.eq("userId", user.getUserId())).uniqueResult();
		return result;
	}
	
	public User retrieveActiveUserByUserId(User user) {
		Session session = sessionFactory.getCurrentSession();
		User result = (User)session.createCriteria(User.class)
				.add(Restrictions.eq("userId", user.getUserId()))
				.add(Restrictions.eq("active", "common.boolean.yn.code.y"))
				.uniqueResult();
		return result;
	}
	
	public User addUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
		return user;
	}
	
	public User updateUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
		return user;
	}
	
	public List<User> retrieveActiveUserByUserIdOrUserName(String searchString) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(User.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.like("userName","%" + searchString + "%"))
			.add(Restrictions.like("userId","%" + searchString + "%"));
		criteria.add(or)
			.add(Restrictions.eq("active", "common.boolean.yn.code.y"))
			.addOrder(Order.asc("userName"));
		
		@SuppressWarnings("unchecked") List<User> result = criteria.list();
		
		return result;
	}
	
	
}
