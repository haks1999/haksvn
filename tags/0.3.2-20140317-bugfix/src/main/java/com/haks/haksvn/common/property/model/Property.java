package com.haks.haksvn.common.property.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="properties")
public class Property {

	@Id
	@Column(name = "property_key",unique = true, nullable = false)
    private String propertyKey;
	
	@Column(name = "property_value", length=2000)
	private String propertyValue;
	
	@Override
	public String toString(){
		return "[ Property ]\n - propertyKey : " + propertyKey + "\n - propertyValue : " + propertyValue;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	
	
}
