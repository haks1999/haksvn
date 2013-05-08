package com.haks.haksvn.common.property.util;

public class PropertyUtils {

	public static String getCommitLogTemplateKey(){
		return "commit.log.template.default";
	}
	
	public static String getCommitLogTemplateKey(int repositorySeq){
		return "commit.log.template." + repositorySeq;
	}
}
