package com.haks.haksvn.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSource;
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
		svnSource = svnRepositoryService.retrieveSVNSourceContent(repository, svnSource);
		//svnSource.setLogs(svnRepositoryService.retrieveSVNSourceLogs(repository, svnSource.getPath(), Paging.Builder.getBuilder(new Paging()).limit(5).build()));
		return svnSource;
	}
	
	public Paging<List<SVNSourceLog>> retrieveSVNSourceLogList(int repositorySeq, Paging<SVNSource> paging){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		return svnRepositoryService.retrieveSVNSourceLogs(repository, paging );
	}
	
	
}
