package com.haks.haksvn.repository.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.dao.RepositoryDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.user.model.User;
import com.haks.haksvn.user.service.UserService;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class RepositoryService {

	
	@Autowired
	private RepositoryDao repositoryDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SVNRepositoryService svnRepositoryService;
	
	
	
	public List<Repository> retrieveRepositoryList(){
		List<Repository> repositoryList = repositoryDao.retrieveRepositoryList();
		return repositoryList;
		
	}
	
	public List<Repository> retrieveActiveRepositoryList(){
		List<Repository> repositoryList = repositoryDao.retrieveActiveRepositoryList();
		return repositoryList;
		
	}
	
	public Repository retrieveRepositoryByRepositorySeq(int repositorySeq){
		Repository repository = new Repository();
		repository.setRepositorySeq(repositorySeq);
		
		Repository result = repositoryDao.retrieveRepositoryByRepositorySeq(repository);
		return result;
		
	}
	
	public List<Repository> retrieveRepositoryListByUserId(String userId){
		List<Repository> result = repositoryDao.retrieveRepositoryListByUserId(userId);
		return result;
		
	}
	
	public List<Repository> saveRepositoryList(List<Repository> repositoryList) throws Exception{
		List<Repository> result = new ArrayList<Repository>(0);
		for( Repository repository : repositoryList){
			result.add(saveRepository(repository));
		}
		return result;
	}
	
	public Repository saveRepository(Repository repository) throws Exception{
		repository = svnRepositoryService.getRepositorySVNInfo(repository);
		repository.formatAuthzTemplate();
		if( repository.getRepositorySeq() < 1 ){
			if( repository.usingSyncUser() ) svnRepositoryService.initRepositoryUser(repository);
			return repositoryDao.addRepository(repository);
		}else{
			// get repository in hibernate session 
			// 그냥 신규 repo obj 로 업뎃하믄 다른 객체로 인식해서 cascade 가 엉망이 됨
			// manytoone 등과 같은 relation 이 설정된 경우 주의해야 함
			//repositoryServerDao.updateRepository(repository.getRepositoryServer());
			Repository repositoryInHibernate = repositoryDao.retrieveRepositoryByRepositorySeq(repository);
			// exclude userList
			Repository.Builder.getBuilder(repositoryInHibernate)
				.active(repository.getActive())
				.authUserId(repository.getAuthUserId()).authUserPasswd(repository.getAuthUserPasswd())
				.repositoryLocation(repository.getRepositoryLocation()).repositoryName(repository.getRepositoryName()).svnName(repository.getSvnName())
				.tagsPath(repository.getTagsPath()).trunkPath(repository.getTrunkPath()).branchesPath(repository.getBranchesPath()).syncUser(repository.getSyncUser())
				.connectType(repository.getConnectType()).serverIp(repository.getServerIp())
				.userId(repository.getUserId()).userPasswd(repository.getUserPasswd())
				.authzPath(repository.getAuthzPath()).passwdPath(repository.getPasswdPath()).passwdType(repository.getPasswdType()).authzTemplate(repository.getAuthzTemplate());
			repositoryDao.updateRepository(repositoryInHibernate);
			if( repositoryInHibernate.usingSyncUser() ) svnRepositoryService.initRepositoryUser(repositoryInHibernate);
			return repositoryInHibernate;
		}
		
	}
	
	public Repository addRepositoryUser(int repositorySeq, List<String> userIdList) throws HaksvnException{
		
		Repository repository = repositoryDao.retrieveRepositoryByRepositorySeq(
						Repository.Builder.getBuilder(new Repository()).repositorySeq(repositorySeq).build());
		List<User> userList = repository.getUserList();
		List<User> userToAddList = new ArrayList<User>();
		for( String userId : userIdList ){
			for( User currentUser : userList ){
				if( currentUser.getUserId().equals(userId)) throw new HaksvnException("duplicate user error");
			}
			User userToAdd = userService.retrieveUserByUserId(userId);
			userToAddList.add(userToAdd);
			userList.add(userToAdd);
		}
		repositoryDao.updateRepository(repository);
		svnRepositoryService.addRepositoryUser(repository, userToAddList);
		return repository;
	}
	
	
	
	public Repository deleteRepositoryUser(int repositorySeq, List<String> userIdList) throws HaksvnException{
		
		if( userIdList.size() < 1) throw new HaksvnException("does not select user ");
		// TODO
		// List.remove 를 통하여 삭제하여 update 실행 시 concurrentmodification 오류 발생
		Repository repository = repositoryDao.retrieveRepositoryByRepositorySeq(
						Repository.Builder.getBuilder(new Repository()).repositorySeq(repositorySeq).build());
		List<User> currentUserList = repository.getUserList();
		List<User> userToDeleteList = new ArrayList<User>();
		List<User> userList = new ArrayList<User>();
		boolean deleteFlag = false;
		for( User currentUser : currentUserList ){
			deleteFlag = false;
			for( String userId : userIdList ){
				deleteFlag = currentUser.getUserId().equals(userId);
				if(deleteFlag) break;
			}
			if( deleteFlag ){
				userToDeleteList.add(currentUser);
			}else{
				userList.add(currentUser);
			}
		}
		repository.setUsers(userList);
		repositoryDao.updateRepository(repository);
		svnRepositoryService.deleteRepositoryUser(repository, userToDeleteList);
		return repository;
		
	}
}
