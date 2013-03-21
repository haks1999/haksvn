package com.haks.haksvn.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haks.haksvn.common.paging.model.NextPaging;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;

@Service
public class SourceService {

	@Autowired 
	private SVNRepositoryService svnRepositoryService;
	@Autowired 
	private RepositoryService repositoryService;
	
	public List<SVNSource> retrieveSVNSourceList(int repositorySeq, String path ){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		return svnRepositoryService.retrieveSVNSourceList(repository, path);
	}
	
	public SVNSource retrieveSVNSource(int repositorySeq, SVNSource svnSource){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		svnSource = svnRepositoryService.retrieveSVNSourceContent(repository, svnSource);
		svnSource = svnRepositoryService.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		//svnSource.setLogs(svnRepositoryService.retrieveSVNSourceLogs(repository, svnSource.getPath(), Paging.Builder.getBuilder(new Paging()).limit(5).build()));
		return svnSource;
	}
	
	public SVNSource retrieveSVNSourceWithoutContent(int repositorySeq, SVNSource svnSource){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		svnSource = svnRepositoryService.retrieveOlderAndNewerAndCurSVNSourceLogList(repository, svnSource);
		return svnSource;
	}
	
	public SVNSource retrieveSVNSourceWithoutContentAndLogs(int repositorySeq, SVNSource svnSource){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		svnSource = svnRepositoryService.retrieveSVNSourceWithoutContentAndLogs(repository, svnSource);
		return svnSource;
	}
	
	public NextPaging<List<SVNSourceLog>> retrieveSVNSourceLogList(int repositorySeq, NextPaging<SVNSource> paging){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		paging.setModel(svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, paging.getModel()));
		return svnRepositoryService.retrieveSVNSourceLogList(repository, paging );
	}
	
	public SVNSourceDiff retrieveDiffByPrevious(int repositorySeq, SVNSource svnSource){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		svnSource = svnRepositoryService.checkIsCopiedOrDeletedAndChangeRevision(repository, svnSource);
		//svnSource = retrieveSVNSourceWithoutContentAndLogs(repository.getRepositorySeq(), svnSource);
		SVNSourceDiff svnSourceDiff = svnRepositoryService.retrieveDiffByPrevious(repository, svnSource);
		
		return svnSourceDiff;
	}
	
	public SVNSourceDiff retrieveDiffWithContentsByPrevious(int repositorySeq, SVNSource svnSource){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
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
	
	public SVNSourceDiff retrieveDiffWithContentsByRevisions(int repositorySeq, SVNSource svnSourceSrc, SVNSource svnSourceTrg){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
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
