package com.haks.haksvn.repository.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.google.common.collect.Lists;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;

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
			entries = targetRepository.getDir( RepositoryUtils.getRelativeRepositoryPath(repository, path), -1 , null , (Collection<SVNDirEntry>) null );
        }catch(Exception e){
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
		return entries;
    }
	
	public SVNSource retrieveSVNSourceWithoutContentAndLogs(Repository repository, SVNSource svnSource){
		
		SVNRepository targetRepository = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision());
        	svnSource.setIsFolder(nodeKind == SVNNodeKind.DIR);
        	SVNProperties fileProperties = new SVNProperties();
            targetRepository.getFile(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision(), fileProperties, null);
            String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
            boolean isTextType = SVNProperty.isTextMimeType(mimeType);
            svnSource.setIsTextMimeType(isTextType);
            svnSource.setSize(targetRepository.info(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision()).getSize());
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	try{
        		if(targetRepository!=null) targetRepository.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
		return svnSource;
		
	}
	
	public SVNSource checkIsTagAndChangeRevision(Repository repository, SVNSource svnSource){
		// tagging 은 해당 리비젼으로 찾을 수 없다... //TODO 공통으로 처리 가능하도록
    	SVNRepository targetRepository = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	
        	String relativePath = RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath());
        	//if( relativePath.startsWith(repository.getTagsPath()) || relativePath.startsWith(repository.getBranchesPath()) ) 
        	SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision());
        	if( nodeKind != SVNNodeKind.NONE ) return svnSource;
        	
        	final List<SVNLogEntry> logList = new ArrayList<SVNLogEntry>(0);
            targetRepository.log(new String[]{relativePath}, -1, 0, true, true, 1, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	logList.add(entry); 
                } 
            });
            if( logList.size() < 1 ) return svnSource;
            svnSource.setRevision(logList.get(0).getRevision());
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	try{
        		if(targetRepository!=null) targetRepository.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
		return svnSource;
	}
	
	public SVNSource retrieveFileContentByRevision(Repository repository, SVNSource svnSource){
		
		SVNRepository targetRepository = null;
		ByteArrayOutputStream baos = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision());
            if (nodeKind == SVNNodeKind.NONE || nodeKind == SVNNodeKind.DIR ) {
            	svnSource.setContent( "[" + svnSource.getPath() + "] is not a file.");
            	return svnSource;
            	//throw new HaksvnException( "[" + svnSource.getPath() + "] is not a file.");
            } 
        	SVNProperties fileProperties = new SVNProperties();
            baos = new ByteArrayOutputStream();
            targetRepository.getFile(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision(), fileProperties, baos);
            String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
            boolean isTextType = SVNProperty.isTextMimeType(mimeType);
            svnSource.setIsTextMimeType(isTextType);
            svnSource.setContent(isTextType?baos.toString("utf-8"):"not a text file");
            svnSource.setSize(targetRepository.info(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision()).getSize());
            
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
	
	/*
	@SuppressWarnings("unchecked")
	public Collection<SVNLogEntry> retrieveSVNLogEntryList(Repository repository, String path){
		SVNRepository targetRepository = null;
		Collection<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	// path, null, startrevision, endrevision, include all paths, strict
            logEntries = targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, path)}, null,0, -1, false, true);
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
        return logEntries;
    }
    */
	
	public SVNSource retrieveSVNLogList(Repository repository, final SVNSource svnSource, long startRev, long endRev, long limit){
		SVNRepository targetRepository = null;
		//Collection<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	// path, null, startrevision, endrevision, include all paths, strict
            //logEntries = targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, path)}, null,0, -1, false, true);
        	final List<SVNLogEntry> logList = new ArrayList<SVNLogEntry>();
        	targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath())}, startRev, endRev, false, true, limit, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	//if( entry.getRevision() == svnSource.getRevision() ) return;
                	logList.add(entry); 
                } 
            });
        	// older 가 아닌 newer 가 될 수 있으나, 편의상 older 로 이용함
        	svnSource.setOlderLogs(SVNRepositoryUtils.transform(logList, new ArrayList<SVNSourceLog>(0),svnSource.getPath(), repository));
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
        return svnSource;
    }
	
	//TODO
	// log조회는 캐슁이 가능할듯
	public SVNSource retrieveOlderAndNewerAndCurSVNSourceLogList(Repository repository, final SVNSource svnSource){
		SVNRepository targetRepository = null;
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	
        	final List<SVNLogEntry> newerLogList = new ArrayList<SVNLogEntry>(0);
        	targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath())}, svnSource.getRevision(), -1, false, true, 5, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	if( entry.getRevision() == svnSource.getRevision() ) return;
                	newerLogList.add(entry); 
                } 
            });
        	
        	final List<SVNLogEntry> olderLogList = new ArrayList<SVNLogEntry>(0);
           	targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath())}, svnSource.getRevision(), 0, false, true, 5, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	if( entry.getRevision() == svnSource.getRevision() ) return;
                   	olderLogList.add(entry); 
                } 
            });
        	
        	// curLog 조회 시, changedPath flag true 로 해서 조회함. 위의 것들과 차이 있음
        	@SuppressWarnings("unchecked")
			List<SVNLogEntry> curLog = Lists.newArrayList(targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath())}, null,svnSource.getRevision(), svnSource.getRevision(), true, true));
        	svnSource.setNewerLogs(SVNRepositoryUtils.transform(newerLogList, new ArrayList<SVNSourceLog>(0),svnSource.getPath(), repository));
        	svnSource.setOlderLogs(SVNRepositoryUtils.transform(olderLogList, new ArrayList<SVNSourceLog>(0),svnSource.getPath(), repository));
        	svnSource.setLog(SVNRepositoryUtils.transform(curLog, new ArrayList<SVNSourceLog>(0),svnSource.getPath(), repository).get(0));
            
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	if(targetRepository!=null) targetRepository.closeSession();
        }
        return svnSource;
    }
	
	public SVNSourceDiff retrieveDiff(Repository repository, SVNSource svnSourceSrc, SVNSource svnSourceTrg){
		SVNRepository targetRepository = null;
		ByteArrayOutputStream baos = null;
		SVNSourceDiff svnSourceDiff = new SVNSourceDiff();
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	SVNDiffClient diffClient = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true), targetRepository.getAuthenticationManager()).getDiffClient();
        	
        	baos = new ByteArrayOutputStream();
        	diffClient.doDiff(SVNURL.parseURIDecoded(RepositoryUtils.getAbsoluteRepositoryPath(repository, svnSourceSrc.getPath())), 
        						SVNRevision.create(svnSourceSrc.getRevision()), 
        						SVNURL.parseURIDecoded(RepositoryUtils.getAbsoluteRepositoryPath(repository, svnSourceTrg.getPath())),
        						SVNRevision.create(svnSourceTrg.getRevision()), SVNDepth.FILES, true, baos);
        	svnSourceDiff.setDiff(baos.toString("utf-8"));
        }catch(Exception e){
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	try{
	        	if(baos!=null)baos.flush();baos.close();
	        	if(targetRepository!=null) targetRepository.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
            	throw new HaksvnException(e);
        	}
        }
        return svnSourceDiff;
	}
	/*
	@SuppressWarnings("unchecked")
	public Collection<SVNLogEntry> retrieveSVNLogEntryList(Repository repository, String path){
		
		new SVNLogClient(ISVNAuthenticationManager)
		
		 final List entries = new LinkedList(); 
  svnClient.doList(mrepositoryURL, SVNRevision.HEAD, SVNRevision.HEAD, 
false, 
  false, new ISVNDirEntryHandler() { 
      public void handleDirEntry(SVNDirEntry entry) throws SVNException { 
          entries.add(entry); 
      } 
  }); 
    }
    */
}
