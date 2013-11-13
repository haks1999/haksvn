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
import com.haks.haksvn.source.cache.ReviewSummarySimpleCache;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;
import com.haks.haksvn.source.model.SVNSourceTagging;
import com.haks.haksvn.source.model.SVNSourceTransfer;
import com.haks.haksvn.source.util.SourceUtils;
import com.haks.haksvn.user.model.User;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SVNRepositoryService {
	
	@Autowired
	private LocalRepositoryFileDao localRepositoryFileDao;
	
	@Autowired
	private SVNRepositoryDao svnRepositoryDao;
	
	@Autowired
	private ReviewSummarySimpleCache reviewSummarySimpleCache;
	
	
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
	
	public Repository setRepositorySVNInfo(Repository repository){
		return svnRepositoryDao.setSVNInfo(repository);
    	
	}
	
	public boolean isExistingSource(Repository repository, String path, long revision){
		return svnRepositoryDao.isExistingSource(repository, path, revision);
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
					.formattedSize(FormatUtils.fileSize(svnDirEntry.getSize())).revision(svnDirEntry.getRevision()).date(svnDirEntry.getDate().getTime()).build();
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
	
	public List<SVNSource> retrieveSVNSourceListByNodeKind( Repository repository, String path , SVNNodeKind nodeKind ){
		List<SVNSource> svnSourceList = new ArrayList<SVNSource>();
		String searchPath = path.indexOf("/") < 0 ? "/":path.substring(0, path.lastIndexOf("/"));
		Collection<SVNDirEntry> entries = svnRepositoryDao.retrieveSVNDirEntryList(repository, searchPath);
		for( SVNDirEntry svnDirEntry : entries ){
			if( nodeKind != svnDirEntry.getKind()) continue;
			if( !(searchPath+"/"+svnDirEntry.getName()).startsWith(path)) continue;
			SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource())
					.title(svnDirEntry.getName()).name(svnDirEntry.getName()).path(searchPath+"/"+svnDirEntry.getName()).author(svnDirEntry.getAuthor()).size(svnDirEntry.getSize())
					.formattedSize(FormatUtils.fileSize(svnDirEntry.getSize())).revision(svnDirEntry.getRevision()).date(svnDirEntry.getDate().getTime()).build();
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
		
		SVNSource svnSource = svnRepositoryDao.retrieveSVNLogList(repository, paging.getModel(), paging.getStart(), 0, paging.getLimit()+1);
		List<SVNSourceLog> logList = svnSource.getOlderLogs();
		
		boolean hasNext = logList.size() > paging.getLimit();
		long end = hasNext?logList.get(paging.getLimit()).getRevision():0;
		if( hasNext ) svnSource.getOlderLogs().remove(logList.size()-1);
		for( SVNSourceLog svnSourceLog :  svnSource.getOlderLogs() ){
			svnSourceLog.setReviewSummarySimple(reviewSummarySimpleCache.get(repository.getRepositoryKey(),svnSourceLog.getRevision()));
		}
		NextPaging<List<SVNSourceLog>> resultPaging = new NextPaging<List<SVNSourceLog>>(svnSource.getOlderLogs());
		NextPaging.Builder.getBuilder(resultPaging).limit(paging.getLimit()).start(paging.getStart()).end(end).hasNext(hasNext);
		
		return resultPaging;
	}
	
	public SVNSource retrieveSVNSourceContent(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveFileContentByRevision(repository, svnSource);
	}

	public SVNSource retrieveOlderAndNewerAndCurSVNSourceLogList(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
	}
	
	//TODO 
	// 이전 버전이 없을때도 오류 안 나도록 - previous 이므로 이전 버젼을 확인 못 하고 호출 가능함
	public SVNSourceDiff retrieveDiffByPrevious(Repository repository, SVNSource svnSource){
		SVNSource svnSourceTrg = svnRepositoryDao.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		SVNSource svnSourceSrc = SVNSource.Builder.getBuilder(new SVNSource()).path(svnSourceTrg.getPath()).build();
		SVNSourceDiff svnSourceDiff = null;
		//System.out.println( "svnSourceTrg.getOlderLogs().size() : " + svnSourceTrg.getOlderLogs().size());
		if( !svnSourceTrg.getIsDeleted() && svnSourceTrg.getOlderLogs().size() < 1 ){
			svnSourceSrc.setRevision(-1);
			svnSourceDiff = new SVNSourceDiff();
			svnSourceDiff.setIsNewContent(true);
			svnSourceDiff.setDiff(SourceUtils.newContentToDiffFormat(svnRepositoryDao.retrieveFileContentByRevision(repository, svnSourceTrg).getContent()));
		}else if(svnSourceTrg.getIsDeleted() && svnSourceTrg.getNewerLogs().size() < 1){
			svnSourceDiff = new SVNSourceDiff();
			svnSourceDiff.setIsDeletedContent(true);
			svnSourceDiff.setDiff(SourceUtils.deletedContentToDiffFormat(svnRepositoryDao.retrieveFileContentByRevision(repository, svnSourceTrg).getContent()));
		}else{
			
			//System.out.println( "svnSourceTrg.getOlderLogs().get(0).getRevision() : " + svnSourceTrg.getOlderLogs().get(0).getRevision());
			svnSourceSrc.setRevision(svnSourceTrg.getOlderLogs().get(0).getRevision());
			svnSourceDiff = svnRepositoryDao.retrieveDiff(repository, svnSourceSrc, svnSourceTrg);
		}
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
	
	public SVNSource checkIsCopiedOrDeletedAndChangeRevision(Repository repository, SVNSource svnSource){
		return svnRepositoryDao.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
	}
	
	public long transfer(Repository repository, List<SVNSourceTransfer> svnSourceTransferList, String log){
		return svnRepositoryDao.transferSourceList(repository, svnSourceTransferList, log);
	}
	
	public SVNSourceTagging tagging(Repository repository, SVNSourceTagging svnSourceTagging ){
		return svnRepositoryDao.copyPathToPath(repository, svnSourceTagging);
	}
	
}
