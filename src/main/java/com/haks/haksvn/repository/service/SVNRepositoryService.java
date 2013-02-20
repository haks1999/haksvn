package com.haks.haksvn.repository.service;

import java.util.ArrayList;
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

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.dao.LocalRepositoryFileDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.user.model.User;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class SVNRepositoryService {
	
	@Autowired
	LocalRepositoryFileDao localRepositoryFileDao;
	
	public boolean testInitalConnection( Repository repository ){
		boolean testResult = testSVNConnection(repository);
		if( CodeUtils.isTrue(repository.getSyncUser()) ) testResult = testResult && testSVNAccountFile(repository);
		return  testResult;
	}

	public boolean testSVNConnection( Repository repository ){
		
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
	
	public boolean testSVNServerConnection( Repository repository ){
		return false;
	}
	
	public boolean testSVNAccountFile( Repository repository ){
		if( !localRepositoryFileDao.hasFileAuth(repository) ) throw new HaksvnException("authz/passwd file does not exist or does not has write auth.");
		return true;
	}
	
	public Repository getRepositorySVNInfo(Repository repository){
		try{
			SVNRepository targetRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(repository.getRepositoryLocation()));
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(repository.getAuthUserId(), repository.getAuthUserPasswd());
			targetRepository.setAuthenticationManager(authManager);
	           
			String root = targetRepository.getRepositoryRoot( true ).toString();
			repository.setSvnRoot(root);
			repository.setSvnName(root.substring(root.lastIndexOf("/") + 1));
		}catch(Exception e){
			throw new HaksvnException(e);
		}
			
        return repository;
    	
	}
	
	// 초기화 시 passwd 에 대해서는 어떤 작업도 일어나지 않는다
	public void initRepositoryUser( Repository repository){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.createAccountFile(repository);
		}
	}
	
	public void addRepositoryUser( Repository repository, List<User> userToAddList ){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.addAccount(repository, userToAddList);
		}
	}
	
	public void deleteRepositoryUser( Repository repository, List<User> userToDeleteList ){
		if( !CodeUtils.isTrue(repository.getSyncUser()) ) return;
		
		if( CodeUtils.usingLocalConnect(repository.getConnectType())){
			localRepositoryFileDao.backupAccountFile(repository);
			localRepositoryFileDao.deleteAccount(repository, userToDeleteList);
		}
	}
	
	
}
