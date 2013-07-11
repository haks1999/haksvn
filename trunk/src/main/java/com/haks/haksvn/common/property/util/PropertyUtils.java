package com.haks.haksvn.common.property.util;

public class PropertyUtils {

	public static String getCommitLogTemplateRequestKey(){
		return "commit.log.template.request.default";
	}
	
	public static String getCommitLogTemplateRequestKey(String repositoryKey){
		return "commit.log.template.request." + repositoryKey;
	}
	
	public static String getCommitLogTemplateTaggingKey(){
		return "commit.log.template.tagging.default";
	}
	
	public static String getCommitLogTemplateTaggingKey(String repositoryKey){
		return "commit.log.template.tagging." + repositoryKey;
	}
	
	public static String getApplicationVersionKey(){
		return "application.version";
	}
}
