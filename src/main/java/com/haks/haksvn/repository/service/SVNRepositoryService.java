package com.haks.haksvn.repository.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import com.google.common.collect.Lists;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.repository.dao.LocalRepositoryFileDao;
import com.haks.haksvn.repository.dao.SVNRepositoryDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceLog;
import com.haks.haksvn.user.model.User;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SVNRepositoryService {
	
	@Autowired
	private LocalRepositoryFileDao localRepositoryFileDao;
	
	@Autowired
	private SVNRepositoryDao svnRepositoryDao;
	
	
	public boolean testInitalConnection( Repository repository ){
		boolean testResult = testSVNConnection(repository);
		if( CodeUtils.isTrue(repository.getSyncUser()) ) testResult = testResult && testSVNAccountFile(repository);
		return  testResult;
	}

	public boolean testSVNConnection( Repository repository ){
		return svnRepositoryDao.isAuthorizedRepository(repository);
	}
	
	public boolean testSVNServerConnection( Repository repository ){
		return false;
	}
	
	public boolean testSVNAccountFile( Repository repository ){
		if( !localRepositoryFileDao.hasFileAuth(repository) ) throw new HaksvnException("authz/passwd file does not exist or does not has write auth.");
		return true;
	}
	
	public Repository getRepositorySVNInfo(Repository repository){
		return svnRepositoryDao.getSVNInfo(repository);
    	
	}
	
	// 초기화 시 passwd 에 대해서는 어떤 작업도 일어나지 않는다
	public void initRepositoryUser( Repository repository){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.createAccountFile(repository);
		}
	}
	
	public void addRepositoryUser( Repository repository, List<User> userToAddList, boolean overwrite ){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.addAccount(repository, userToAddList, overwrite);
		}
	}
	
	public void deleteRepositoryUser( Repository repository, List<User> userToDeleteList ){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.deleteAccount(repository, userToDeleteList);
		}
	}
	
	
	public List<SVNSource> retrieveSVNSourceList( Repository repository, String path ){
		// list 조회 시, log 는 가져오지 않는다.
		List<SVNSource> svnSourceList = new ArrayList<SVNSource>();
		Collection<SVNDirEntry> entries = svnRepositoryDao.retrieveSVNDirEntryList(repository, path);
		for( SVNDirEntry svnDirEntry : entries ){
			SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource())
					.title(svnDirEntry.getName()).name(svnDirEntry.getName()).path(path+"/"+svnDirEntry.getName())
					.size(svnDirEntry.getSize()).revision(svnDirEntry.getRevision()).build();
			svnSource.setIsFolder(svnDirEntry.getKind() == SVNNodeKind.DIR);
			svnSource.setIsLazy(svnSource.getIsFolder());
			svnSourceList.add(svnSource);
		}
		return svnSourceList;
    }
	
	public SVNSource retrieveSVNSourceContent(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveFileContentByRevision(repository, svnSource);
	}
	
	public List<SVNSourceLog> retrieveSVNSourceLogs(Repository repository, String path, Paging paging ){
		List<SVNSourceLog> svnSourceLogList = new ArrayList<SVNSourceLog>();
		Collection<SVNLogEntry> entries = svnRepositoryDao.retrieveSVNLogEntryList(repository, path);
		List<SVNLogEntry> entryList = Lists.newArrayList(entries);
		ListIterator<SVNLogEntry> reverseEntries = entryList.listIterator(entryList.size());
		int count = 0;
		while( reverseEntries.hasPrevious() && count++ < paging.getLimit()){
			SVNLogEntry svnLogEntry = reverseEntries.previous();
			svnSourceLogList.add(SVNSourceLog.Builder.getBuilder(new SVNSourceLog())
					.author(svnLogEntry.getAuthor()).date(svnLogEntry.getDate().toString()).message(svnLogEntry.getMessage())
					.revision(svnLogEntry.getRevision()).build());
		}
		return svnSourceLogList;
	}
	
}
