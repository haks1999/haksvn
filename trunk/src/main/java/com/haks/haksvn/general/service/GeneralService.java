package com.haks.haksvn.general.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.property.model.Property;
import com.haks.haksvn.common.property.service.PropertyService;
import com.haks.haksvn.common.property.util.PropertyUtils;
import com.haks.haksvn.general.model.CommitLogTemplate;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class GeneralService {

	
	@Autowired
	private PropertyService propertyService;
	
	public CommitLogTemplate retrieveDefaultCommitLogTemplate(String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		Property property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey():PropertyUtils.getCommitLogTemplateTaggingKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public CommitLogTemplate retrieveCommitLogTemplate(String repositoryKey, String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		Property property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey(repositoryKey):PropertyUtils.getCommitLogTemplateTaggingKey(repositoryKey));
		if( property == null ) property = propertyService.retrievePropertyByPropertyKey(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey():PropertyUtils.getCommitLogTemplateTaggingKey());
		return CommitLogTemplate.Builder.getBuilder(new CommitLogTemplate()).repositoryKey(repositoryKey).template(property.getPropertyValue().replaceAll("%n", "\r")).build();
	}
	
	public void saveCommitLogTemplate(CommitLogTemplate commitLogTemplate, String logTemplateCodeId){
		boolean isRequestType = CodeUtils.isLogTemplateRequest(logTemplateCodeId);
		propertyService.saveProperty(isRequestType?PropertyUtils.getCommitLogTemplateRequestKey(commitLogTemplate.getRepositoryKey()):PropertyUtils.getCommitLogTemplateTaggingKey(commitLogTemplate.getRepositoryKey()), commitLogTemplate.getTemplate().replaceAll("\r", "%n"));
	}
}
