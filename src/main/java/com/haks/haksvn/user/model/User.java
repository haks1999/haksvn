package com.haks.haksvn.user.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.repository.model.Repository;

@Entity
@Table(name="users")
public class User{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_seq",unique = true, nullable = false)
    private int userSeq;
	
	@Column(name = "user_id",unique = true, nullable = false, updatable=false)
	@NotEmpty(message="user id : Mandantory Field")
	private String userId;
	
	@Column(name = "user_name",nullable = false)
	@NotEmpty(message="user name : Mandantory Field")
	private String userName;
	
	@Column(name = "active",nullable = true)
	private boolean active;
	
	@Column(name = "email",nullable = false)
	private String email;
	
	@Column(name = "user_passwd",nullable = false)
	private String userPasswd;
	
	/*
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
	private Set<Repository> repositories = new HashSet<Repository>();
	*/
	
	public User(){
		
	}
	
	@Override
	public String toString(){
		return "[ User ]\n - userSeq : " + userSeq + "\n - userId : " + userId +
					"\n - userName : " + userName + "\n - active : " + active + "\n - email : " + email;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	/*
	public Set<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<Repository> repositories) {
		this.repositories = repositories;
	}
	*/
	
	
}
