package com.haks.haksvn.repository.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;
import com.haks.haksvn.source.model.SVNSource;

@Component
public class SVNRepositoryDao {

	
	public boolean isAuthorizedRepository( Repository repository ){
		
		ISVNEditor editor = null; 
		SVNRepository targetRepository = null;
		try{
			targetRepository = SVNRepositoryUtils.getSVNRepositoryForTestConnection(repository);
			String relativeRoot = repository.getRepositoryLocation().replace(targetRepository.getRepositoryRoot( true ).toString(), "");
			
            if( targetRepository.checkPath( relativeRoot  + repository.getTrunkPath() ,  -1 ) == SVNNodeKind.NONE){
            	throw new HaksvnException( "[" + repository.getTrunkPath() + "] is not exist in repository.");
            }
            if( targetRepository.checkPath( relativeRoot + repository.getTagsPath() ,  -1 ) == SVNNodeKind.NONE){
            	throw new HaksvnException( "[" + repository.getTagsPath() + "] is not exist in repository.");
            }
            if( targetRepository.checkPath( relativeRoot + repository.getBranchesPath() ,  -1 ) == SVNNodeKind.NONE){
            	throw new HaksvnException( "[" + repository.getBranchesPath() + "] is not exist in repository.");
            }
            
            editor = targetRepository.getCommitEditor("testCommit", null);
            editor.openRoot(-1);
            
        }catch(Exception e){
        	e.printStackTrace();
        	throw new HaksvnException(e.getMessage());
		} finally {
			if(targetRepository!=null) targetRepository.closeSession();
			if (editor != null) {
				try {
					editor.abortEdit();
				} catch (Exception e) {
					e.printStackTrace();
					throw new HaksvnException(e.getMessage());
				}
			}
		}
    	return true;
	}
	
	
	public Repository getSVNInfo(Repository repository){
		SVNRepository targetRepository = null;
		try{
			targetRepository = SVNRepositoryUtils.getSVNRepositoryForTestConnection(repository);
			String root = targetRepository.getRepositoryRoot( true ).toString();
			repository.setSvnRoot(root);
			repository.setSvnName(root.substring(root.lastIndexOf("/") + 1));
		}catch(Exception e){
			e.printStackTrace();
			throw new HaksvnException(e);
		}finally{
			if(targetRepository!=null) targetRepository.closeSession();
		}
			
        return repository;
    	
	}
	
	
	
	@SuppressWarnings("unchecked")
	//TODO
	// svnrepository의 session 을 사용하여 connection 재사용하도록
	public Collection<SVNDirEntry> retrieveSVNDirEntryList( Repository repository, String path ){
		// list 조회 시, log 는 가져오지 않는다.
		SVNRepository targetRepository = null;
		Collection<SVNDirEntry> entries = new ArrayList<SVNDirEntry>();
		try{
			targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
			entries = targetRepository.getDir( repository.getRepositoryLocation().substring(repository.getSvnRoot().length()) + path, -1 , null , (Collection<SVNDirEntry>) null );
        }catch(Exception e){
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
		return entries;
    }
	
	public SVNSource retrieveFileContentByRevision(Repository repository, SVNSource svnSource){
		
		SVNRepository targetRepository = null;
		ByteArrayOutputStream baos = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	SVNNodeKind nodeKind = targetRepository.checkPath(repository.getRepositoryLocation().substring(repository.getSvnRoot().length()) + svnSource.getPath(), svnSource.getRevision());
            if (nodeKind == SVNNodeKind.NONE || nodeKind == SVNNodeKind.DIR ) {
            	throw new HaksvnException( "[" + svnSource.getPath() + "] is not a file.");
            } 
        	SVNProperties fileProperties = new SVNProperties();
            baos = new ByteArrayOutputStream();
            targetRepository.getFile(repository.getRepositoryLocation().substring(repository.getSvnRoot().length()) + svnSource.getPath(), svnSource.getRevision(), fileProperties, baos);
            String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
            boolean isTextType = SVNProperty.isTextMimeType(mimeType);
            svnSource.setIsTextMimeType(isTextType);
            svnSource.setContent(isTextType?baos.toString():"");
            
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	try{
        		if(baos !=null) baos.close();
        		if(targetRepository!=null) targetRepository.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
		return svnSource;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<SVNLogEntry> retrieveSVNLogEntryList(Repository repository, String path){
		SVNRepository targetRepository = null;
		Collection<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	// path, null, startrevision, endrevision, include all paths, strict
            logEntries = targetRepository.log(new String[]{repository.getRepositoryLocation().substring(repository.getSvnRoot().length()) + path}, null,0, -1, false, true);
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
        return logEntries;
    }
}