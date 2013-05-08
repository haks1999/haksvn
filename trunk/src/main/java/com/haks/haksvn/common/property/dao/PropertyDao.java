package com.haks.haksvn.common.property.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haks.haksvn.common.property.model.Property;

@Repository
public class PropertyDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Property> retrievePropertyList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked") List<Property> result = 
					session.createCriteria(Property.class)
				.list();
		
		return result;
	}
	
	public Property retrievePropertyByPropertyKey(String propertyKey) {
		Session session = sessionFactory.getCurrentSession();
		Property result = (Property)session.createCriteria(Property.class)
					.add(Restrictions.eq("propertyKey", propertyKey))
					.uniqueResult();
		
		return result;
	}
	
	public Property saveProperty(Property property){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(property);
		return property;
	}
	
	
}
