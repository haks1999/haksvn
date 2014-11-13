package com.haks.haksvn.repository.util;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.haks.haksvn.common.exception.HaksvnException;

@Component
public class SVNRepositoryLock {

	private static final ConcurrentHashMap<String,Boolean> LOCKED_REPOSITORIES = new ConcurrentHashMap<String,Boolean>(0);
	
	public static void tryLock(String repositoryKey){
		if( isLocked( repositoryKey )) throw new HaksvnException("[ " + repositoryKey + " ] repository is locked by another transfer or tagging");
		LOCKED_REPOSITORIES.put(repositoryKey, true);
	}
	
	public static void release(String repositoryKey){
		LOCKED_REPOSITORIES.remove(repositoryKey);
	}
	
	public static boolean isLocked(String repositoryKey){
		return LOCKED_REPOSITORIES.contains(repositoryKey);
	}
}
