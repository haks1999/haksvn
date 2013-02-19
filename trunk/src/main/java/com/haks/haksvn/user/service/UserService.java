package com.haks.haksvn.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
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
	
	public User saveUser(User user) throws Exception{
		if( user.getUserSeq() < 1 ){
			return userDao.addUser(user);
		}else{
			User currentUser = userDao.retrieveUserByUserId(user);
			String currentForSVN = currentUser.getAuthType() + currentUser.getUserPasswd();
			String afterForSVN = user.getAuthType() + user.getUserPasswd();
			
			currentUser = User.Builder.getBuilder(currentUser).active(user.getActive()).authType(user.getAuthType())
				.authTypeCode(user.getAuthTypeCode()).email(user.getEmail()).userId(user.getUserId())
				.userName(user.getUserName()).userPasswd(user.getUserPasswd()).build();
			userDao.updateUser(currentUser);
			
			if( !currentForSVN.equals(afterForSVN) ){
				repositoryService.saveRepositoryList(repositoryService.retrieveActiveRepositoryListByUserId(currentUser.getUserId()));
			}
			return user;
		}
		
	}
	
	public List<User> retrieveActiveUserByUserIdOrUserName(String searchString){
		List<User> userList = userDao.retrieveActiveUserByUserIdOrUserName(searchString);
		return userList;
		
	}
	
}
