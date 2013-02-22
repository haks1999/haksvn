package com.haks.haksvn.source.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNRevisionProperty;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSource;

@Service
public class SourceService {

	@Autowired 
	private SVNRepositoryService svnRepositoryService;
	@Autowired 
	private RepositoryService repositoryService;
	
	public List<SVNSource> retrieveSVNSourceList(String repositorySeq, String path ){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(Integer.parseInt(repositorySeq));
		Collection<SVNDirEntry> svnDirEntryList = svnRepositoryService.retrieveSVNDirEntryListByPath(repository, path);
		List<SVNSource> svnSourceList = new ArrayList<SVNSource>();
		for( SVNDirEntry svnDirEntry : svnDirEntryList ){
			// commitMessage 는 얻어오는 방식이 틀리다.
			SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource())
					.title(svnDirEntry.getName()).author(svnDirEntry.getAuthor())
					.date(svnDirEntry.getDate().toString()).name(svnDirEntry.getName()).path(path+"/"+svnDirEntry.getName())
					.revision(svnDirEntry.getRevision()).size(svnDirEntry.getSize()).build();
			svnSource.setIsFolder(svnDirEntry.getKind() == SVNNodeKind.DIR);
			svnSource.setIsLazy(svnSource.getIsFolder());
			svnSourceList.add(svnSource);
		}
		return svnSourceList;
	}
	
	
}
