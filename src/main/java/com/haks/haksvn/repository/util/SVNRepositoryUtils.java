package com.haks.haksvn.repository.util;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.crypto.util.CryptoUtils;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;

@Component
public class SVNRepositoryUtils {

	
	public static ISVNAuthenticationManager createISVNAuthManagerByUser(Repository repository){
		boolean isPersonalAuth = !CodeUtils.isTrue(repository.getSyncUser());
		String svnUserId = isPersonalAuth?ContextHolder.getLoginUser().getUserId():repository.getAuthUserId();
		String svnUserPasswd = CryptoUtils.decodeAES(isPersonalAuth?ContextHolder.getLoginUser().getUserPasswd():repository.getAuthUserPasswd());
		return SVNWCUtil.createDefaultAuthenticationManager(svnUserId, svnUserPasswd);
	}
}
