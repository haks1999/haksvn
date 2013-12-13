package com.haks.haksvn.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNNodeKind;

import com.haks.haksvn.common.paging.model.NextPaging;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;

@Service
public class SourceService {

	@Autowired 
	private SVNRepositoryService svnRepositoryService;
	
	public List<SVNSource> retrieveSVNSourceList(Repository repository, String path ){
		return svnRepositoryService.retrieveSVNSourceList(repository, path);
	}
	
	public List<SVNSource> retrieveSVNSourceDirList(Repository repository, String path ){
		return svnRepositoryService.retrieveSVNSourceListByNodeKind(repository, path, SVNNodeKind.DIR);
	}
	
	public SVNSource retrieveSVNSource(Repository repository, SVNSource svnSource){
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		svnSource = svnRepositoryService.retrieveSVNSourceContent(repository, svnSource);
		svnSource = svnRepositoryService.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		return svnSource;
	}
	
	public SVNSource retrieveSVNSourceWithoutContent(Repository repository, SVNSource svnSource){
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		svnSource = svnRepositoryService.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		return svnSource;
	}
	
	public SVNSource retrieveSVNSourceWithoutContentAndLogs(Repository repository, SVNSource svnSource){
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		svnSource = svnRepositoryService.retrieveSVNSourceWithoutContentAndLogs(repository, svnSource);
		return svnSource;
	}
	
	public NextPaging<List<SVNSourceLog>> retrieveSVNSourceLogList(Repository repository, NextPaging<SVNSource> paging){
		paging.setModel(svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, paging.getModel()));
		return svnRepositoryService.retrieveSVNSourceLogList(repository, paging );
	}
	
	public SVNSourceDiff retrieveDiffByPrevious(Repository repository, SVNSource svnSource){
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		SVNSourceDiff svnSourceDiff = svnRepositoryService.retrieveDiffByPrevious(repository, svnSource);
		return svnSourceDiff;
	}
	
	public SVNSourceDiff retrieveDiffByRevisions(Repository repository, SVNSource svnSourceSrc, SVNSource svnSourceTrg){
		svnSourceSrc = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSourceSrc);
		svnSourceTrg = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSourceTrg);
		SVNSourceDiff svnSourceDiff = svnRepositoryService.retrieveDiffByRevisions(repository, svnSourceSrc, svnSourceTrg);
		return svnSourceDiff;
	}
	
	public SVNSourceDiff retrieveDiffWithContentsByPrevious(Repository repository, SVNSource svnSource){
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		SVNSourceDiff svnSourceDiff = svnRepositoryService.retrieveDiffByPrevious(repository, svnSource);
		if( !svnSourceDiff.getIsNewContent() ){
			SVNSource svnSourceSrc = svnRepositoryService.retrieveSVNSourceContent(repository, svnSourceDiff.getSrc());
			SVNSource svnSourceTrg = svnRepositoryService.retrieveSVNSourceContent(repository, svnSourceDiff.getTrg());
			svnSourceDiff.getSrc().setContent(svnSourceSrc.getContent());
			svnSourceDiff.getTrg().setContent(svnSourceTrg.getContent());
		}
		return svnSourceDiff;
	}
	
	public SVNSourceDiff retrieveDiffWithContentsByRevisions(Repository repository, SVNSource svnSourceSrc, SVNSource svnSourceTrg){
		svnSourceSrc = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSourceSrc);
		svnSourceTrg = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSourceTrg);
		SVNSourceDiff svnSourceDiff = svnRepositoryService.retrieveDiffByRevisions(repository, svnSourceSrc, svnSourceTrg);
		svnSourceSrc = svnRepositoryService.retrieveSVNSourceContent(repository, svnSourceSrc);
		svnSourceTrg = svnRepositoryService.retrieveSVNSourceContent(repository, svnSourceTrg);
		svnSourceDiff.getSrc().setContent(svnSourceSrc.getContent());
		svnSourceDiff.getTrg().setContent(svnSourceTrg.getContent());
		return svnSourceDiff;
	}
	
}
