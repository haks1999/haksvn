package com.haks.haksvn.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.dao.LocalRepositoryFileDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.user.model.User;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SVNRepositoryService {
	
	@Autowired
	LocalRepositoryFileDao localRepositoryFileDao;
	
	public boolean testInitalConnection( Repository repository ) throws HaksvnException{
		boolean testResult = testSVNConnection(repository);
		if( repository.usingSyncUser() ) testResult = testResult && testSVNAccountFile(repository);
		return  testResult;
	}

	public boolean testSVNConnection( Repository repository ) throws HaksvnException{
		
		ISVNEditor editor = null; 
		try{
			SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), repository.getAuthUserPasswd());
			targetRepository.setAuthenticationManager(authManager);
            
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
            
            editor = targetRepository.getCommitEditor("testConnection", null);
            editor.openRoot(-1);
            
        }catch(Exception e){
        	e.printStackTrace();
        	throw new HaksvnException(e.getMessage());
		} finally {
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
	
	public boolean testSVNServerConnection( Repository repository ) throws HaksvnException{
		return false;
	}
	
	public boolean testSVNAccountFile( Repository repository ) throws HaksvnException{
		if( !localRepositoryFileDao.hasFileAuth(repository) ) throw new HaksvnException("authz/passwd file does not exist or does not has write auth.");
		return true;
	}
	
	public Repository getRepositorySVNName(Repository repository) throws Exception{
		
		SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), repository.getAuthUserPasswd());
		targetRepository.setAuthenticationManager(authManager);
           
		String root = targetRepository.getRepositoryRoot( true ).toString();
		repository.setSvnName(root.substring(root.lastIndexOf("/") + 1));
			
        return repository;
    	
	}
	
	public void addRepositoryUser( Repository repository, List<User> userToAddList ) throws HaksvnException{
		if( !repository.usingSyncUser() ) return;
		
		if( repository.usingLocalConnect()){
			localRepositoryFileDao.addAccount(repository, userToAddList);
		}
	}
	
	public void deleteRepositoryUser( Repository repository, List<User> userToDeleteList ) throws HaksvnException{
		if( !repository.usingSyncUser() ) return;
		
		if( repository.usingLocalConnect()){
			localRepositoryFileDao.deleteAccount(repository, userToDeleteList);
		}
	}
	
	public void initializeSVNAccountFile(Repository repository) throws HaksvnException{
		localRepositoryFileDao.backupAccountFile(repository);
	}
	
}
