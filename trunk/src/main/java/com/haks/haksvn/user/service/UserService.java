package com.haks.haksvn.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.user.dao.UserDao;
import com.haks.haksvn.user.model.User;

@Service
@Transactional
public class UserService {

	
	@Autowired
	private UserDao userDao;
	
	
	
	public List<User> retrieveUserList(){
		List<User> userList = userDao.retrieveUserList();
		return userList;
		
	}
	
	public User retrieveUserByUserSeq(int userSeq){
		User user = new User();
		user.setUserSeq(userSeq);
		User result = userDao.retrieveUserByUserSeq(user);
		return result;
		
	}
	
	public User saveUser(User user){
		if( user.getUserSeq() < 1 ){
			return userDao.addUser(user);
		}else{
			return userDao.updateUser(user);
		}
		
	}
	
}
