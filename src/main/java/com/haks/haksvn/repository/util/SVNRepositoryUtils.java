package com.haks.haksvn.repository.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNLogEntry;
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
import com.haks.haksvn.common.format.util.FormatUtils;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.source.model.SVNSourceLog;

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
	
	public static List<SVNSourceLog> transform(List<SVNLogEntry> svnLogEntryList, List<SVNSourceLog> svnSourceLogList){
		ListIterator<SVNLogEntry> reverseEntries = svnLogEntryList.listIterator(svnLogEntryList.size());
		while( reverseEntries.hasPrevious()){
			SVNLogEntry svnLogEntry = reverseEntries.previous();
			svnSourceLogList.add(SVNSourceLog.Builder.getBuilder(new SVNSourceLog())
					.author(svnLogEntry.getAuthor()).date(FormatUtils.simpleDate(svnLogEntry.getDate())).message(svnLogEntry.getMessage().length() < 1 ? "[No log message]":svnLogEntry.getMessage())
					.revision(svnLogEntry.getRevision()).build());
		}
		Collections.sort(svnSourceLogList, new Comparator<SVNSourceLog>(){
		     public int compare(SVNSourceLog src1, SVNSourceLog src2){
		    	 long comp = src1.getRevision() - src2.getRevision();
		    	 return comp < 0 ? 1 : (comp > 0 ? -1 : 0);
		     }
		});
		return svnSourceLogList;
	}
}
