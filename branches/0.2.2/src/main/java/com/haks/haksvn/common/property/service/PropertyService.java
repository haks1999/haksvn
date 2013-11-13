package com.haks.haksvn.common.property.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.property.dao.PropertyDao;
import com.haks.haksvn.common.property.model.Property;

@Service
@Transactional
public class PropertyService {

	
	@Autowired
	private PropertyDao propertyDao;
	
	public Property retrievePropertyByPropertyKey(String propertyKey){
		Property result = propertyDao.retrievePropertyByPropertyKey(propertyKey);
		return result;
	}
	
	public Property saveProperty(String propertyKey, String propertyValue){
		Property property = new Property();
		property.setPropertyKey(propertyKey);
		property.setPropertyValue(propertyValue);
		return propertyDao.saveProperty(property);
	}
}
