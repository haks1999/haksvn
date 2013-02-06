package com.haks.haksvn.repository.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.repository.dao.RepositoryDao;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.user.dao.UserDao;
import com.haks.haksvn.user.model.User;

@Service
@Transactional
public class RepositoryService {

	
	@Autowired
	private RepositoryDao repositoryDao;
	@Autowired
	private UserDao userDao;
	
	
	
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
	
	public Repository saveRepository(Repository repository){
		if( repository.getRepositorySeq() < 1 ){
			return repositoryDao.addRepository(repository);
		}else{
			// get repository in hibernate session 
			// 그냥 신규 repo obj 로 업뎃하믄 다른 객체로 인식해서 cascade 가 엉망이 됨
			// manytoone 등과 같은 relation 이 설정된 경우 주의해야 함
			Repository repositoryInHibernate = repositoryDao.retrieveRepositoryByRepositorySeq(repository);
			
			// exclude userList
			Repository.Builder.getBuilder(repositoryInHibernate)
				.active(repository.getActive())
				.authUserId(repository.getAuthUserId()).authUserPasswd(repository.getAuthUserPasswd())
				.repositoryLocation(repository.getRepositoryLocation()).repositoryName(repository.getRepositoryName())
				.tagsPath(repository.getTagsPath()).trunkPath(repository.getTrunkPath());
			
			return repositoryDao.updateRepository(repositoryInHibernate);
		}
		
	}
	
	public Repository addRepositoryUser(int repositorySeq, List<String> userIdList) throws HaksvnException{
		
		Repository repository = repositoryDao.retrieveRepositoryByRepositorySeq(
						Repository.Builder.getBuilder(new Repository()).repositorySeq(repositorySeq).build());
		List<User> userList = repository.getUserList();
		for( String userId : userIdList ){
			for( User currentUser : userList ){
				if( currentUser.getUserId().equals(userId)) throw new HaksvnException("duplicate user error");
			}
			userList.add(userDao.retrieveUserByUserId(User.Builder.getBuilder(new User()).userId(userId).build()));
		}
		return repositoryDao.updateRepository(repository);
	}
	
	
	
	public Repository deleteRepositoryUser(int repositorySeq, List<String> userIdList) throws HaksvnException{
		
		// TODO
		// List.remove 를 통하여 삭제하여 update 실행 시 concurrentmodification 오류 발생
		// 테스트용으로 신규 리스트에 추가하는 방식으로 하니 잘 돌아감. 이유 분석 필요
		Repository repository = repositoryDao.retrieveRepositoryByRepositorySeq(
						Repository.Builder.getBuilder(new Repository()).repositorySeq(repositorySeq).build());
		List<User> currentUserList = repository.getUserList();
		List<User> userList = new ArrayList<User>();
		for( String userId : userIdList ){
			for( User currentUser : currentUserList ){
				if( !currentUser.getUserId().equals(userId)){
					userList.add(currentUser);
				}
			}
		}
		repository.setUsers(userList);
		return repositoryDao.updateRepository(repository);
	}
}
