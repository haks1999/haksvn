package com.haks.haksvn.common.property.util;

public class PropertyUtils {

	public static String getCommitLogTemplateRequestKey(){
		return "commit.log.template.request.default";
	}
	
	public static String getCommitLogTemplateRequestKey(int repositorySeq){
		return "commit.log.template.request." + repositorySeq;
	}
	
	public static String getCommitLogTemplateTaggingKey(){
		return "commit.log.template.tagging.default";
	}
	
	public static String getCommitLogTemplateTaggingKey(int repositorySeq){
		return "commit.log.template.tagging." + repositorySeq;
	}
}
