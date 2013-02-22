package com.haks.haksvn.repository.util;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.crypto.util.CryptoUtils;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;

@Component
public class SVNRepositoryUtils {

	static{
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}
	
	public static ISVNAuthenticationManager createISVNAuthManagerByUser(Repository repository){
		boolean isPersonalAuth = !CodeUtils.isTrue(repository.getSyncUser());
		String svnUserId = isPersonalAuth?ContextHolder.getLoginUser().getUserId():repository.getAuthUserId();
		String svnUserPasswd = CryptoUtils.decodeAES(isPersonalAuth?ContextHolder.getLoginUser().getUserPasswd():repository.getAuthUserPasswd());
		return SVNWCUtil.createDefaultAuthenticationManager(svnUserId, svnUserPasswd);
	}
	
	public static SVNRepository getUserAuthSVNRepository(Repository repository) throws Exception{
		SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
		targetRepository.setAuthenticationManager(SVNRepositoryUtils.createISVNAuthManagerByUser(repository));
		return targetRepository;
	}
	
	// repository.authUserPasswd 암호화되기 전 상태
	public static SVNRepository getSVNRepositoryForTestConnection(Repository repository) throws Exception{
		SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), repository.getAuthUserPasswd());
		targetRepository.setAuthenticationManager(authManager);
		return targetRepository;
	}
}
