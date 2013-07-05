package com.haks.haksvn.source.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerMapping;

public class SourceUrlRewriteUtils {

	public static String reverseUrlRewrite(HttpServletRequest request, String mapping, int repositorySeq){
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		path = path.replaceFirst(mapping + "/" + String.valueOf(repositorySeq),"").replaceAll("%2E", ".").replaceAll("_resources_","resources");
		if(path.startsWith("/")) path=path.replaceFirst("/","");
		return path;
	}
}
