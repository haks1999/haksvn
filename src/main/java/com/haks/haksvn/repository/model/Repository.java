package com.haks.haksvn.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="repositories")
public class Repository{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "repository_seq",unique = true, nullable = false)
    private int repositorySeq;
	
	@Column(name = "repository_location",nullable = false)
	@NotEmpty(message="repositoy location : Mandantory Field")
	private String repositoryLocation;
	
	@Column(name = "repository_name",nullable = false)
	@NotEmpty(message="repositoy name : Mandantory Field")
	private String repositoryName;
	
	@Column(name = "repository_status",nullable = true)
	private String repositoryStatus;
	
	@Column(name = "trunk_path",nullable = false)
	@NotEmpty(message="trunk path : Mandantory Field")
	private String trunkPath;
	
	@Column(name = "tags_path",nullable = false)
	@NotEmpty(message="tags path : Mandantory Field")
	private String tagsPath;
	
	@Column(name = "auth_user_id",nullable = false)
	@NotEmpty(message="user id : Mandantory Field")
	private String authUserId;
	
	@Column(name = "auth_user_passwd",nullable = false)
	@NotEmpty(message="user password : Mandantory Field")
	private String authUserPasswd;
	
	public Repository(){
		
	}
	
	@Override
	public String toString(){
		return "[ Repository ]\n - repositorySeq : " + repositorySeq + "\n - repositoryLocation : " + repositoryLocation +
					"\n - repositoryName : " + repositoryName + "\n - repositoryStatus : " + repositoryStatus;
	}

	public int getRepositorySeq() {
		return repositorySeq;
	}

	public void setRepositorySeq(int repositorySeq) {
		this.repositorySeq = repositorySeq;
	}

	public String getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getRepositoryStatus() {
		return repositoryStatus;
	}

	public void setRepositoryStatus(String repositoryStatus) {
		this.repositoryStatus = repositoryStatus;
	}

	public String getTrunkPath() {
		return trunkPath;
	}

	public void setTrunkPath(String trunkPath) {
		this.trunkPath = trunkPath;
	}

	public String getTagsPath() {
		return tagsPath;
	}

	public void setTagsPath(String tagsPath) {
		this.tagsPath = tagsPath;
	}

	public String getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(String authUserId) {
		this.authUserId = authUserId;
	}

	public String getAuthUserPasswd() {
		return authUserPasswd;
	}

	public void setAuthUserPasswd(String authUserPasswd) {
		this.authUserPasswd = authUserPasswd;
	}

	
	
}
