package com.haks.haksvn.user.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
}
