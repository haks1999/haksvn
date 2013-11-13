package com.haks.haksvn.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.crypto.util.CryptoUtils;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.user.dao.UserDao;
import com.haks.haksvn.user.model.User;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class UserService {

	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RepositoryService repositoryService;
	
	
	
	public List<User> retrieveUserList(){
		List<User> userList = userDao.retrieveUserList();
		return userList;
		
	}
	
	public ResultMessage duplicateUser( User user ){
		ResultMessage message = new ResultMessage();
		if(retrieveUserByUserId(user.getUserId()) != null ){
			message.setSuccess(false);
			message.setType(DefaultMessage.TYPE.ERROR);
			message.setText("exist user id");
		}
		return message;
	}
	
	public User retrieveUserByUserSeq(int userSeq){
		User user = new User();
		user.setUserSeq(userSeq);
		User result = userDao.retrieveUserByUserSeq(user);
		return result;
		
	}
	
	public User retrieveUserByUserId(String userId){
		User user = new User();
		user.setUserId(userId);
		User result = userDao.retrieveUserByUserId(user);
		return result;
		
	}
	
	public User saveUser(User user){
		if( user.getUserSeq() < 1 ){
			return addUser(user);
		}else{
			return updateUser(user);
		}
	}
	
	private User addUser(User user){
		user.setUserPasswd(CryptoUtils.encodeAES(user.getUserPasswd()));
		return userDao.addUser(user);
	}
	
	private User updateUser(User user){
		User currentUser = userDao.retrieveUserByUserId(user);
		boolean changedAuth = !currentUser.getAuthType().equals(user.getAuthType());
		boolean changedPasswd = user.getUserPasswd().length() > 0;
		boolean changedToInActive = CodeUtils.isTrue(currentUser.getActive()) && !CodeUtils.isTrue(user.getActive());
		
		currentUser = User.Builder.getBuilder(currentUser).active(user.getActive()).authType(user.getAuthType())
			.authTypeCode(user.getAuthTypeCode()).email(user.getEmail()).userId(user.getUserId())
			.userName(user.getUserName()).build();
		if(changedPasswd) currentUser.setUserPasswd(CryptoUtils.encodeAES(user.getUserPasswd()));
		userDao.updateUser(currentUser);
		
		if( changedAuth || changedPasswd || changedToInActive ){
			//List<Repository> repositoryList = repositoryService.retrieveActiveRepositoryListByUserId(currentUser.getUserId());
			List<Repository> repositoryList = currentUser.getRepositoryList();
			for( Repository repository : repositoryList ){
				repository.setAuthUserPasswdEncrypted(true);
			}
			repositoryService.saveRepositoryList(repositoryList);
			if( changedToInActive ){
				for( Repository repository : repositoryList ){
					repositoryService.deleteRepositoryUser(repository.getRepositoryKey(), Arrays.asList(new String[]{user.getUserId()}));
				}
			}
		}
		return user;
	}
	
	public List<User> retrieveActiveUserByUserIdOrUserName(String searchString){
		List<User> userList = userDao.retrieveActiveUserByUserIdOrUserName(searchString);
		return userList;
		
	}
	
	public List<User> retrieveRepositoryActiveUserByUserIdOrUserName(String searchString, String repositoryKey){
		// hibernate 쿼리를 못 짜서 아래와 같이 이중 for loop 로 만듦. 수정 필요
		List<User> userList = userDao.retrieveActiveUserByUserIdOrUserName(searchString);
		List<User> repoUserList = new ArrayList<User>(0);
		for( User user: userList ){
			for( Repository repository: user.getRepositoryList() ){
				if( repository.getRepositoryKey().equals(repositoryKey)){
					repoUserList.add(user);
				}
			}
		}
		return repoUserList;
		
	}
	
	public User retrieveActiveUserByUserIdAndPasswd(User user) {
		return userDao.retrieveActiveUserByUserIdAndPasswd(user);
	}
	
	public void deleteUser(User user){
		User userToDelete = userDao.retrieveUserByUserSeq(user);
		if( userToDelete.getRepositoryList() !=null && userToDelete.getRepositoryList().size() > 0 ){
			for( Repository repository : userToDelete.getRepositoryList() ){
				repositoryService.deleteRepositoryUser(repository.getRepositoryKey(), Arrays.asList(new String[]{userToDelete.getUserId()}));
			}
		}
		userDao.deleteUser(userToDelete);
	}
	
}
