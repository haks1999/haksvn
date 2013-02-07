package com.haks.haksvn.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@DynamicUpdate
@Table(name="repository_server")
public class RepositoryServer {
	
    @Id
	@Column(name = "repository_seq",unique = true, nullable = false)
    private int repositorySeq;
	
	@Column(name = "connect_type",nullable = false)
	@NotEmpty(message="connect type : Mandantory Field")
	private String connectType;
	
	@Column(name = "server_ip")
	private String serverIp;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_passwd")
	private String userPasswd;
	
	@Column(name = "authz_path",nullable = false)
	@NotEmpty(message="authz path : Mandantory Field")
	private String authzPath;
	
	@Column(name = "passwd_path",nullable = false)
	@NotEmpty(message="passwd path : Mandantory Field")
	private String passwdPath;
	
	@Column(name = "passwd_type",nullable = false)
	private String passwdType;

	public int getRepositorySeq() {
		return repositorySeq;
	}

	public void setRepositorySeq(int repositorySeq) {
		this.repositorySeq = repositorySeq;
	}

	public String getConnectType() {
		return connectType;
	}

	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getAuthzPath() {
		return authzPath;
	}

	public void setAuthzPath(String authzPath) {
		this.authzPath = authzPath;
	}

	public String getPasswdPath() {
		return passwdPath;
	}

	public void setPasswdPath(String passwdPath) {
		this.passwdPath = passwdPath;
	}
	
	public String getPasswdType(){
		return passwdType;
	}
	
	public void setPasswdType(String passwdType){
		this.passwdType = passwdType;
	}
	
	
}
