package com.haks.haksvn.repository.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.format.util.FormatUtils;
import com.haks.haksvn.common.paging.model.NextPaging;
import com.haks.haksvn.repository.dao.LocalRepositoryFileDao;
import com.haks.haksvn.repository.dao.SVNRepositoryDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
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
	
	// 한 번에 svnentrylog 를 조회해 온 후, 페이징 하는 방식이었으나
	// svnentrylog 를 아예 조금씩 가져오도록 수정. log 가 많아지면 기존 방식으로는 감당하기 어려움.
	// 커넥션이 1-2 번 더 생기나 더 이득임
	public NextPaging<List<SVNSourceLog>> retrieveSVNSourceLogList(Repository repository, NextPaging<SVNSource> paging ){
		
		SVNSource svnSource = svnRepositoryDao.retrieveSVNLogList(repository, paging.getModel(), paging.getStart(), paging.getDirection(), paging.getLimit());
		List<SVNSourceLog> logList = svnSource.getOlderLogs();
		
		SVNSource svnSourceCheckStartLog = SVNSource.Builder.getBuilder(new SVNSource()).path(svnSource.getPath()).revision(logList.get(0).getRevision()).build();
		svnSourceCheckStartLog = svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSourceCheckStartLog);
		
		SVNSource svnSourceCheckEndLog = null;
		boolean isLastPage = logList.size() < paging.getLimit();
		if( !isLastPage ){
			svnSourceCheckEndLog = SVNSource.Builder.getBuilder(new SVNSource()).path(svnSource.getPath()).revision(logList.get(logList.size()-1).getRevision()).build();
			svnSourceCheckEndLog = svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSourceCheckEndLog);
		}
		boolean hasPrev = svnSourceCheckStartLog.getNewerLogs().size() > 0;
		boolean hasNext = isLastPage?false:svnSourceCheckEndLog.getOlderLogs().size() > 0;
		
		NextPaging<List<SVNSourceLog>> resultPaging = new NextPaging<List<SVNSourceLog>>(svnSource.getOlderLogs());
		NextPaging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(logList.get(0).getRevision()).end(logList.get(logList.size()-1).getRevision()).hasNext(hasNext).hasPrev(hasPrev);
		
		return resultPaging;
	}
	
	public SVNSource retrieveSVNSourceContent(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveFileContentByRevision(repository, svnSource);
	}

	public SVNSource retrieveOlderAndNewerAndCurSVNSourceLogList(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
	}
	
	//TODO 
	// 이전 버전이 없을때도 오류 안 나도록
	public SVNSourceDiff retrieveDiffByPrevious(Repository repository, SVNSource svnSource){
		SVNSource svnSourceTrg = svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		SVNSource svnSourceSrc = SVNSource.Builder.getBuilder(new SVNSource()).path(svnSourceTrg.getPath()).revision(svnSourceTrg.getOlderLogs().get(0).getRevision()).build();
		SVNSourceDiff svnSourceDiff = svnRepositoryDao.retrieveDiff(repository, svnSourceSrc, svnSourceTrg);
		svnSourceDiff.setSrc(svnSourceSrc);
		svnSourceDiff.setTrg(svnSourceTrg);
		return svnSourceDiff;
	}
	
	public SVNSourceDiff retrieveDiffByRevisions(Repository repository, SVNSource svnSourceSrc, SVNSource svnSourceTrg){
		SVNSourceDiff svnSourceDiff = svnRepositoryDao.retrieveDiff(repository, svnSourceSrc,  svnSourceTrg);
		svnSourceDiff.setSrc(svnSourceSrc);
		svnSourceDiff.setTrg(svnSourceTrg);
		return svnSourceDiff;
	}
	
	public SVNSource checkIsTagAndChangeRevision(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.checkIsTagAndChangeRevision(repository, svnSource);
	}
	
}
