package com.haks.haksvn.common.security.model;

import org.springframework.stereotype.Component;

import com.haks.haksvn.user.model.User;

@Component
public class LoginUser {

    private int userSeq;
	private String userId;
	private String userName;
	private String email;
	private String userPasswd;
	private String authType;
	
	public LoginUser(){
		
	}
	
	public LoginUser(User user){
		userSeq = user.getUserSeq();
		userId = user.getUserId();
		userName = user.getUserName();
		email = user.getEmail();
		userPasswd = user.getUserPasswd();
		authType = user.getAuthType();
	}

	@Override
	public String toString(){
		return "\n[ LoginUser ]\n - userSeq : " + userSeq + "\n - userId : " + userId +
					"\n - userName : " + userName + "\n - authType : " + authType + "\n - email : " + email;
	}
	
	public int getUserSeq() {
		return userSeq;
	}

	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
}
