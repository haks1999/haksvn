package com.haks.haksvn.general.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.property.model.Property;
import com.haks.haksvn.common.property.service.PropertyService;
import com.haks.haksvn.common.property.util.PropertyUtils;
import com.haks.haksvn.general.model.CommitLogTemplate;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class GeneralService {

	
	@Autowired
	private PropertyService propertyService;
	
	public CommitLogTemplate retrieveDefaultCommitLogTemplate(){
		Property property = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getCommitLogTemplateKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public CommitLogTemplate retrieveCommitLogTemplate(int repositorySeq){
		Property property = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getCommitLogTemplateKey(repositorySeq));
		if( property == null ) property = propertyService.retrievePropertyByPropertyKey(PropertyUtils.getCommitLogTemplateKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).repositorySeq(repositorySeq).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public void saveCommitLogTemplate(CommitLogTemplate commitLogTemplate){
		propertyService.saveProperty(PropertyUtils.getCommitLogTemplateKey(commitLogTemplate.getRepositorySeq()), commitLogTemplate.getTemplate().replaceAll("\r", "%n"));
	}
}
