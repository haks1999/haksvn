package com.haks.haksvn.repository.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.google.common.collect.Lists;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.repository.util.SVNRepositoryLock;
import com.haks.haksvn.repository.util.SVNRepositoryUtils;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;
import com.haks.haksvn.source.model.SVNSourceLogChanged;
import com.haks.haksvn.source.model.SVNSourceTagging;
import com.haks.haksvn.source.model.SVNSourceTransfer;

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
					//e.printStackTrace();
					//throw new HaksvnException(e.getMessage());
				}
			}
		}
    	return true;
	}
	
	
	public Repository setSVNInfo(Repository repository){
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
        	//e.printStackTrace();
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
        	if( svnSource.getIsFolder() ) return svnSource;
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
	
	public boolean isExistingSource(Repository repository, String path, long revision ){
		SVNRepository targetRepository = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, path), revision);
        	return nodeKind != SVNNodeKind.NONE;
            
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
	}
	
	public SVNSource checkIsCopiedOrDeletedAndChangeRevision(Repository repository, SVNSource svnSource){
		// tagging 은 해당 리비젼으로 찾을 수 없다... //TODO 공통으로 처리 가능하도록	
		//TODO copiedPath 를 사용하면 뭔가 될지도
		//TODO 여기 로직 걸레 됐음. 조만간 손 볼 것.
    	SVNRepository targetRepository = null;
        try{
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	String relativePath = RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath());
        	SVNNodeKind headNodeKind = targetRepository.checkPath(relativePath, -1);
        	svnSource.setIsDeleted(headNodeKind == SVNNodeKind.NONE);
        	// 파일인 경우, revision이 -1 로 들어오는경우 revision을 명시하도록 바꾼다. diff 시 -1 로는 비교가 안 됨
        	if( !svnSource.getIsDeleted() && svnSource.getRevision() < 0 && headNodeKind == SVNNodeKind.FILE){
        		SVNProperties fileProperties = new SVNProperties();
                targetRepository.getFile(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), -1, fileProperties, null);
                svnSource.setRevision(Long.parseLong(fileProperties.getStringValue(SVNProperty.COMMITTED_REVISION)));
        	}
        	SVNNodeKind curNodeKind = targetRepository.checkPath(relativePath, svnSource.getRevision());
        	if( curNodeKind != SVNNodeKind.NONE && !svnSource.getIsDeleted() ) return svnSource;
        	
        	long startRev = -1;
        	long repositorylastestRevision = targetRepository.getLatestRevision();
        	
        	if( svnSource.getIsDeleted() ){
        		//long deletedRevision = targetRepository.getDeletedRevision(relativePath, svnSource.getRevision()<0?0:svnSource.getRevision(), repositorylastestRevision);
				long deletedRevision = targetRepository.getDeletedRevision(relativePath, 0, repositorylastestRevision);
        		//TODO deleted lastest revision 을 꼭 구해야 하나. 오류로 처리해 버리면 안 되나... 
        		// 0 ~ -1 로 찾는 경우는 list change 에서만 쓰이며, diff 등에서는 실제 존재하는 revision을 넘겨준다. ㅅㅂ
        		if( deletedRevision < 0 ){
        			// for loop 로 존재하는 node 를 검색해봤으나 미친짓임. 걍 오류뱉자
        			deletedRevision = repositorylastestRevision-1;
        		}
        		if( curNodeKind == SVNNodeKind.NONE ) deletedRevision = svnSource.getRevision();
        		final List<SVNLogEntry> logListForLastest = new ArrayList<SVNLogEntry>(0);
                targetRepository.log(new String[]{relativePath}, deletedRevision-1, 0, false, true, 1, new ISVNLogEntryHandler() { 
                    public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                    	logListForLastest.add(entry); 
                    } 
                });
        		svnSource.setLastestRevision(logListForLastest.get(0).getRevision());
        		startRev = curNodeKind == SVNNodeKind.NONE? svnSource.getLastestRevision():svnSource.getRevision();
        	}
        	
        	final List<SVNLogEntry> logList = new ArrayList<SVNLogEntry>(0);
            targetRepository.log(new String[]{relativePath}, startRev, 0, false, true, 1, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	logList.add(entry); 
                } 
            });
            
            if( logList.size() < 1 ) return svnSource;
            if( !svnSource.getIsDeleted() ){
            	svnSource.setIsCopied(true);
            	svnSource.setCopiedRevision(svnSource.getRevision());
            }
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
        try {
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	if( startRev < 0 ) startRev = svnSource.getLastestRevision();	// deleted 경우, -1 검색 불가
        	// path, null, startrevision, endrevision, include all paths, strict
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
        	
        	SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath()), svnSource.getRevision());
        	svnSource.setIsFolder(nodeKind == SVNNodeKind.DIR);
        	
        	final List<SVNLogEntry> newerLogList = new ArrayList<SVNLogEntry>(0);
       		targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSource.getPath())}, svnSource.getRevision(), svnSource.getLastestRevision(), false, true, 5, new ISVNLogEntryHandler() { 
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
        	
        	// curLog 는 nodetype 을 명시해준다. request 에서 search by revision 시 사용됨
        	for( SVNSourceLogChanged svnSourceLogChanged: svnSource.getLog().getChangedList()){
        		svnSourceLogChanged.setNodeType(targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository,svnSourceLogChanged.getPath()), svnSource.getRevision()).toString());
        	}
        	
        	
        	
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
        	SVNDiffClient diffClient = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(false), targetRepository.getAuthenticationManager()).getDiffClient();
        	
        	baos = new ByteArrayOutputStream();
        	//SVNEncodingUtil.uriDecode(RepositoryUtils.getAbsoluteRepositoryPath(repository, svnSourceSrc.getPath()));
        	//위 메써드로 대체하라고 하나 SVNURL을 얻어 낼 수가 없다. 걍 deprecated 로 일단 간다.
        	diffClient.doDiff(SVNURL.parseURIDecoded(RepositoryUtils.getAbsoluteRepositoryPath(repository, svnSourceSrc.getPath())), 
        						SVNRevision.create(svnSourceSrc.getRevision()), 
        						SVNURL.parseURIDecoded(RepositoryUtils.getAbsoluteRepositoryPath(repository, svnSourceTrg.getPath())),
        						SVNRevision.create(svnSourceTrg.getRevision()), SVNDepth.FILES, true, baos);
        	svnSourceDiff.setDiff(baos.toString());
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
	
	// argument의 path 는 trunk, branches 등의 경로는 제외한 순수 파일 경로
	public long transferSourceList(Repository repository, List<SVNSourceTransfer> transferList, String log){
		SVNRepository targetRepository = null;
		SVNRepository targetRepositoryForModify = null;
		ISVNEditor editor = null;
		long newRevision = -1;
        try{
        	SVNRepositoryLock.tryLock(repository.getRepositoryKey());
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	
        	// copyclient 는 commit 을 묶어서 처리할 수 없음
        	/*
    		final SVNCopyClient copyClient = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(false), targetRepository.getAuthenticationManager()).getCopyClient();
    		final SVNURL location = targetRepository.getLocation();
    		final SVNURL srcURL = SVNURL.parseURIEncoded(location + fromRootDir + path);
    		final SVNURL dstURL = SVNURL.parseURIEncoded(location + toRootDir + path);
    		final SVNCopySource source = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL);
    		copyClient.doCopy(new SVNCopySource[] { source }, dstURL, false, true,false, "copy 222", null);
    		*/
        	
        	List<String> dirListToAdd = new ArrayList<String>(0);
    		Map<String, List<SVNSourceTransfer>> transferMap = new HashMap<String,List<SVNSourceTransfer>>();
    		Set<String> addedDirs = new HashSet<String>(0);
    		
    		for( SVNSourceTransfer svnSourceTransfer : transferList ){
    			String destFullPath = repository.getBranchesPath() + svnSourceTransfer.getRelativePath();
    			String destDir = SVNRepositoryUtils.extractDir(destFullPath);
    			List<SVNSourceTransfer> svnSourceTrasnferToAddList =
    					transferMap.containsKey(SVNRepositoryUtils.extractDir(svnSourceTransfer.getRelativePath()))?
    							transferMap.get(SVNRepositoryUtils.extractDir(svnSourceTransfer.getRelativePath())):new ArrayList<SVNSourceTransfer>(0);
    			svnSourceTrasnferToAddList.add(svnSourceTransfer);
    			transferMap.put(SVNRepositoryUtils.extractDir(svnSourceTransfer.getRelativePath()), svnSourceTrasnferToAddList);
    			
    			
    			boolean destExist = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, destDir), -1) != SVNNodeKind.NONE;
    			//if( nodeKind == SVNNodeKind.NONE){
    				String currentDir = "";
    				String[] dirFrags = destDir.split("/");
    				//System.out.println( "transferSouce path : " + svnSourceTransfer.getRelativePath());
    				for( String dirFrag : dirFrags ){
    					currentDir += ("/" + dirFrag);
    					currentDir = currentDir.replaceAll("//", "/");
    					//System.out.println( "currentDir: " + currentDir);
    					dirListToAdd.add(currentDir);
    					//System.out.println( "test: " + destExist + " / " + ( targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, currentDir), -1) == SVNNodeKind.NONE));
    					if( !destExist && targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, currentDir), -1) == SVNNodeKind.NONE ){
    						
    					}else{
    						addedDirs.add(currentDir);
    					}
    				}
    			//}
    		}
    		
    		targetRepositoryForModify = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
    		editor = targetRepository.getCommitEditor( log , null, true, null );
    		editor.openRoot( -1 );
    		String befParent = "!@#$";
    		int depth = 0;
    		for( String parent : dirListToAdd ){
    			if( !parent.startsWith(befParent)){
    				for( int inx = 0 ; inx < depth ; inx++ ) editor.closeDir();
    				if( depth < 1 ) editor.openDir(SVNRepositoryUtils.extractDir(parent),-1);
    				//System.out.println("opened: " + parent.substring(0, parent.lastIndexOf("/")) );
    				depth = 0;
    			}
    			
    			if( !addedDirs.contains(parent) ){
    				//System.out.println( "add new dir: " + parent);
    				//editor.addDir(parent.substring(parent.lastIndexOf("/")), null, -1);
    				editor.addDir(parent, null, -1);
    				addedDirs.add(parent);
    			}else{
    				//System.out.println( "open added dir: " + parent);
    				//editor.openDir(parent.substring(parent.lastIndexOf("/")),-1);
    				editor.openDir(parent,-1);
    			}
    			
    			String relPath = parent.replaceFirst(repository.getBranchesPath(), "");
    			//System.out.println( "-relPath: " + relPath + " -contains: "+ transferMap.containsKey(relPath) );
    			
    			if( transferMap.containsKey(relPath)){
    				List<SVNSourceTransfer> svnSourceTransferList = transferMap.get(relPath);
    				for( SVNSourceTransfer svnSourceTransfer : svnSourceTransferList ){
    					if( svnSourceTransfer.getIsToDelete() ){
    						editor.deleteEntry(repository.getBranchesPath() + svnSourceTransfer.getRelativePath(), svnSourceTransfer.getRevision());
        				}else if(svnSourceTransfer.getIsToAdd()){
        					editor.addFile(repository.getBranchesPath() + svnSourceTransfer.getRelativePath(), 
        							RepositoryUtils.getRelativeRepositoryPath(repository, repository.getTrunkPath() + svnSourceTransfer.getRelativePath()) ,svnSourceTransfer.getRevision());
        				}else{
        					
        					ByteArrayOutputStream oldDataBos = new ByteArrayOutputStream();
        					ByteArrayOutputStream newDataBos = new ByteArrayOutputStream();
        					targetRepositoryForModify.getFile(RepositoryUtils.getRelativeRepositoryPath(repository, repository.getBranchesPath() + svnSourceTransfer.getRelativePath()), -1, new SVNProperties(), oldDataBos);
        					targetRepositoryForModify.getFile(RepositoryUtils.getRelativeRepositoryPath(repository, repository.getTrunkPath() + svnSourceTransfer.getRelativePath()), svnSourceTransfer.getRevision(), new SVNProperties(), newDataBos);
        		            byte[] oldData = oldDataBos.toByteArray();
        		            byte[] newData = newDataBos.toByteArray();
        		            
        					editor.openFile( repository.getBranchesPath() + svnSourceTransfer.getRelativePath() , -1 );
        					editor.applyTextDelta( repository.getBranchesPath() + svnSourceTransfer.getRelativePath() , null );
        					
        					SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator( );
        					String checksum = deltaGenerator.sendDelta( repository.getBranchesPath() + svnSourceTransfer.getRelativePath() , new ByteArrayInputStream( oldData ) , 0 , new ByteArrayInputStream( newData ) , editor , true );
        					editor.closeFile( repository.getBranchesPath() + svnSourceTransfer.getRelativePath() , checksum );
        					oldDataBos.flush();
        					oldDataBos.close();
        					newDataBos.flush();
        					newDataBos.close();
        					  
        				}
    					
    				}
    				transferMap.remove(relPath);
    			}
    			depth++;
    			befParent = parent;
    		}
    		SVNCommitInfo info = editor.closeEdit();
    		newRevision = info.getNewRevision();
            
        }catch (Exception e) {
        	if( editor != null ){
        		try{ editor.abortEdit(); }catch(Exception ex){}
        	}
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	SVNRepositoryLock.release(repository.getRepositoryKey());
        	try{
        		if(targetRepository!=null) targetRepository.closeSession();
        		if(targetRepositoryForModify!=null) targetRepositoryForModify.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
        return newRevision;
	}
	
	// srcPath,destPath 는 /trunk~, /branches~ 다 붙여서
	public SVNSourceTagging copyPathToPath(Repository repository, final SVNSourceTagging svnSourceTagging){
		SVNRepository targetRepository = null;
        try{
        	SVNRepositoryLock.tryLock(repository.getRepositoryKey());
        	targetRepository = SVNRepositoryUtils.getUserAuthSVNRepository(repository);
        	
        	SVNClientManager svnClientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(false), targetRepository.getAuthenticationManager());
        	
        	final SVNURL location = targetRepository.getLocation();
    		final SVNURL srcURL = SVNURL.parseURIEncoded(location + svnSourceTagging.getSrcPath());
    		final SVNURL destURL = SVNURL.parseURIEncoded(location + svnSourceTagging.getDestPath());
    		
    		targetRepository.log(new String[]{RepositoryUtils.getRelativeRepositoryPath(repository, svnSourceTagging.getSrcPath())}, -1, 0, false, true, 1, new ISVNLogEntryHandler() { 
                public void handleLogEntry(SVNLogEntry entry) throws SVNException { 
                	svnSourceTagging.setSrcRevision(entry.getRevision());
                } 
            });
    		// delete existing tag path
    		SVNNodeKind nodeKind = targetRepository.checkPath(RepositoryUtils.getRelativeRepositoryPath(repository, svnSourceTagging.getDestPath()), -1);
    		if( nodeKind != SVNNodeKind.NONE){
    			final SVNCommitClient commitClient = svnClientManager.getCommitClient();
            	commitClient.doDelete(new SVNURL[]{destURL}, "<<--DELETE EXISTING TAG FOR TAGGING-->>\n" + svnSourceTagging.getLog() );
    		}
        	
    		final SVNCopyClient copyClient = svnClientManager.getCopyClient();
    		final SVNCopySource source = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL);
    		SVNCommitInfo commitInfo = copyClient.doCopy(new SVNCopySource[] { source }, destURL, false, false, false, svnSourceTagging.getLog(), null);
    		svnSourceTagging.setDestRevision(commitInfo.getNewRevision());
    		
        }catch (Exception e) {
        	e.printStackTrace();
        	throw new HaksvnException(e);
        }finally{
        	try{
        		SVNRepositoryLock.release(repository.getRepositoryKey());
        		if(targetRepository!=null) targetRepository.closeSession();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
        return svnSourceTagging;
	}
	
}
