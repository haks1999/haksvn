package com.haks.haksvn.repository.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.haks.haksvn.common.format.util.FormatUtils;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.repository.dao.LocalRepositoryFileDao;
import com.haks.haksvn.repository.dao.SVNRepositoryDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;
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
	
	public SVNSource retrieveSVNSourceWithoutContentAndLogs( Repository repository, SVNSource svnSource ){
		return svnRepositoryDao.retrieveSVNSourceWithoutContentAndLogs(repository, svnSource);
    }
	
	public List<SVNSource> retrieveSVNSourceList( Repository repository, String path ){
		// list 조회 시, log 는 가져오지 않는다.
		List<SVNSource> svnSourceList = new ArrayList<SVNSource>();
		Collection<SVNDirEntry> entries = svnRepositoryDao.retrieveSVNDirEntryList(repository, path);
		for( SVNDirEntry svnDirEntry : entries ){
			SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource())
					.title(svnDirEntry.getName()).name(svnDirEntry.getName()).path(path+"/"+svnDirEntry.getName()).author(svnDirEntry.getAuthor()).size(svnDirEntry.getSize())
					.formattedSize(FormatUtils.fileSize(svnDirEntry.getSize())).revision(svnDirEntry.getRevision()).date(FormatUtils.simpleDate(svnDirEntry.getDate())).build();
			svnSource.setIsFolder(svnDirEntry.getKind() == SVNNodeKind.DIR);
			svnSource.setIsLazy(svnSource.getIsFolder());
			svnSourceList.add(svnSource);
		}
		Collections.sort(svnSourceList, new Comparator<SVNSource>(){
		     public int compare(SVNSource src1, SVNSource src2){
		    	 return src1.getName().compareToIgnoreCase(src2.getName());
		     }
		});
		return svnSourceList;
    }
	
	public SVNSource retrieveSVNSourceContent(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveFileContentByRevision(repository, svnSource);
	}
	
	public Paging<List<SVNSourceLog>> retrieveSVNSourceLogList(Repository repository, Paging<SVNSource> paging ){
		List<SVNSourceLog> svnSourceLogList = new ArrayList<SVNSourceLog>();
		Paging<List<SVNSourceLog>> resultPaging = new Paging<List<SVNSourceLog>>(svnSourceLogList);
		
		Collection<SVNLogEntry> entries = svnRepositoryDao.retrieveSVNLogEntryList(repository, paging.getModel().getPath());
		List<SVNLogEntry> entryList = Lists.newArrayList(entries);
		Paging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart()).total(entryList.size()).build();
		entryList = entryList.subList(entryList.size()-paging.getStart()-paging.getLimit() < 0 ? 0:entryList.size()-paging.getStart()-paging.getLimit(), entryList.size()-paging.getStart());
		SVNRepositoryUtils.transform(entryList, svnSourceLogList);
		return resultPaging;
	}
	
	public SVNSource retrieveOlderAndNewerAndCurSVNSourceLogList(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
	}
	
}
