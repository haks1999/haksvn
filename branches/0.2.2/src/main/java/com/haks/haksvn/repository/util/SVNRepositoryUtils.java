package com.haks.haksvn.repository.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
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
import com.haks.haksvn.source.model.SVNSourceLog;
import com.haks.haksvn.source.model.SVNSourceLogChanged;

@Component
public class SVNRepositoryUtils {

	static{
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}
	
	public static ISVNAuthenticationManager createISVNAuthManagerByUser(Repository repository){
		boolean isPersonalAuth = CodeUtils.isTrue(repository.getSyncUser());
		String svnUserId = isPersonalAuth?ContextHolder.getLoginUser().getUserId():repository.getAuthUserId();
		String svnUserPasswd = CryptoUtils.decodeAES(isPersonalAuth?ContextHolder.getLoginUser().getUserPasswd():repository.getAuthUserPasswd());
		return SVNWCUtil.createDefaultAuthenticationManager(svnUserId, svnUserPasswd);
	}
	
	public static SVNRepository getUserAuthSVNRepository(Repository repository) throws Exception{
		SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
		targetRepository.setAuthenticationManager(SVNRepositoryUtils.createISVNAuthManagerByUser(repository));
		return targetRepository;
	}
	
	public static SVNRepository getSVNRepositoryForTestConnection(Repository repository) throws Exception{
		String authzUserPasswd = repository.getAuthUserPasswd();
		if( repository.getAuthUserPasswdEncrypted() ) authzUserPasswd = CryptoUtils.decodeAES(repository.getAuthUserPasswd());
		SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), authzUserPasswd);
		targetRepository.setAuthenticationManager(authManager);
		return targetRepository;
	}
	
	public static String extractDir(String path){
		return path.substring(0,path.lastIndexOf("/"));
	}
	
	public static String extractFileName(String path){
		return path.substring(path.lastIndexOf("/")+1);
	}
	
	public static List<SVNSourceLog> transform(List<SVNLogEntry> svnLogEntryList, List<SVNSourceLog> svnSourceLogList, String path, Repository repository){
		ListIterator<SVNLogEntry> reverseEntries = svnLogEntryList.listIterator(svnLogEntryList.size());
		String abstractRoot = ("/" + repository.getRepositoryLocation().substring(repository.getSvnRoot().length())).replaceAll("//", "/");
		boolean existAbstractRoot = abstractRoot.length() > 1;
		while( reverseEntries.hasPrevious()){
			SVNLogEntry svnLogEntry = reverseEntries.previous();
			ArrayList<SVNSourceLogChanged> changedList = new ArrayList<SVNSourceLogChanged>(0);
        	for( Map.Entry<String, SVNLogEntryPath> elem : ((Map<String,SVNLogEntryPath>)svnLogEntry.getChangedPaths()).entrySet() ){
        		String changedPath = elem.getValue().getPath();
        		if( existAbstractRoot && changedPath.startsWith(abstractRoot)){
        			changedPath = changedPath.substring(abstractRoot.length());
        		}
        		if( !changedPath.startsWith(repository.getTagsPath()) || !changedPath.startsWith(repository.getTrunkPath()) || !changedPath.startsWith(repository.getBranchesPath())){
        			if( changedPath.indexOf(repository.getTagsPath()) > -1 ){
        				changedPath = changedPath.substring(changedPath.indexOf(repository.getTagsPath()));
        			}else if( changedPath.indexOf(repository.getTrunkPath()) > -1 ){
        				changedPath = changedPath.substring(changedPath.indexOf(repository.getTrunkPath()));
        			}else if( changedPath.indexOf(repository.getBranchesPath()) > -1 ){
        				changedPath = changedPath.substring(changedPath.indexOf(repository.getBranchesPath()));
        			}
        		}
        		if(path.length() > 0 && !changedPath.startsWith("/"+path)) continue;	// changedpath 는 path과 관련없이 다 가져오므로 여기서 걸러낸다
        		changedList.add(SVNSourceLogChanged.Builder.getBuilder(new SVNSourceLogChanged()).path(changedPath).type(elem.getValue().getType()).build());
        	}
        	Collections.sort(changedList, new Comparator<SVNSourceLogChanged>(){
	   		     public int compare(SVNSourceLogChanged src1, SVNSourceLogChanged src2){
	   		    	 return src1.getPath().compareToIgnoreCase(src2.getPath());
	   		     }
        	});
        	
        	svnSourceLogList.add(SVNSourceLog.Builder.getBuilder(new SVNSourceLog())
					.author(svnLogEntry.getAuthor())
					.date(svnLogEntry.getDate().getTime())
					.message(svnLogEntry.getMessage() == null || svnLogEntry.getMessage().length() < 1 ? "[No log message]":svnLogEntry.getMessage())
					.revision(svnLogEntry.getRevision()).changedList(changedList).build());
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
