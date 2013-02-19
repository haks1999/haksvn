package com.haks.haksvn.user.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
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
	
	public User saveUser(User user) throws Exception{
		if( user.getUserSeq() < 1 ){
			return userDao.addUser(user);
		}else{
			User currentUser = userDao.retrieveUserByUserId(user);
			String currentForSVN = currentUser.getAuthType() + currentUser.getUserPasswd();
			String afterForSVN = user.getAuthType() + user.getUserPasswd();
			boolean currentActive = CodeUtils.isTrue(currentUser.getActive());
			boolean afterActive = CodeUtils.isTrue(user.getActive());
			
			currentUser = User.Builder.getBuilder(currentUser).active(user.getActive()).authType(user.getAuthType())
				.authTypeCode(user.getAuthTypeCode()).email(user.getEmail()).userId(user.getUserId())
				.userName(user.getUserName()).userPasswd(user.getUserPasswd()).build();
			userDao.updateUser(currentUser);
			
			boolean needDeleteUser =  currentActive && !afterActive;
			boolean needReposiotyInit = !currentForSVN.equals(afterForSVN) || needDeleteUser;
			
			if( needReposiotyInit ){
				//List<Repository> repositoryList = repositoryService.retrieveActiveRepositoryListByUserId(currentUser.getUserId());
				List<Repository> repositoryList = currentUser.getRepositoryList();
				repositoryService.saveRepositoryList(repositoryList);
				if( needDeleteUser ){
					for( Repository repository : repositoryList ){
						repositoryService.deleteRepositoryUser(repository.getRepositorySeq(), Arrays.asList(new String[]{user.getUserId()}));
					}
				}
			}
			return user;
		}
		
	}
	
	public List<User> retrieveActiveUserByUserIdOrUserName(String searchString){
		List<User> userList = userDao.retrieveActiveUserByUserIdOrUserName(searchString);
		return userList;
		
	}
	
	public void deleteUser(User user) throws HaksvnException{
		User userToDelete = userDao.retrieveUserByUserSeq(user);
		if( userToDelete.getRepositoryList() !=null && userToDelete.getRepositoryList().size() > 0 ){
			for( Repository repository : userToDelete.getRepositoryList() ){
				repositoryService.deleteRepositoryUser(repository.getRepositorySeq(), Arrays.asList(new String[]{userToDelete.getUserId()}));
			}
		}
		userDao.deleteUser(userToDelete);
	}
	
}
