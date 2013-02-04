package com.haks.haksvn.repository.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.user.model.User;

@Entity
@DynamicUpdate
@SelectBeforeUpdate
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
	
	@Column(name = "active",nullable = true)
	private String active;
	
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
	
	@OneToMany(fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	//@Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
	//@Cascade({org.hibernate.annotations.CascadeType.DETACH})
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "repositories_users", joinColumns = { 
			@JoinColumn(name = "repository_seq", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_seq", 
					nullable = false, updatable = false) })
	private List<User> userList = new ArrayList<User>();
	
	public Repository(){
		
	}
	
	
	/*
	public Repository (Repository repository){
		repositorySeq = repository.getRepositorySeq();
		repositoryLocation = repository.getRepositoryLocation();
		repositoryName = repository.getRepositoryName();
		active = repository.getActive();
		trunkPath = repository.getTrunkPath();
		tagsPath = repository.getTagsPath();
		authUserId = repository.getAuthUserId();
		authUserPasswd = repository.getAuthUserPasswd();
		userList = repository.getUserList();
		return this;
	}
	*/
	
	@Override
	public String toString(){
		return "[ Repository ]\n - repositorySeq : " + repositorySeq + "\n - repositoryLocation : " + repositoryLocation +
					"\n - repositoryName : " + repositoryName + "\n - active : " + active;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
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

	public List<User> getUserList() {
		return userList;
	}

	public void setUsers(List<User> userList) {
		this.userList = userList;
	}
	
	
	
	public static class Builder{
		
		private Repository repository;
		
		private Builder(Repository repository){
			this.repository = repository;
		}
		
		public static Builder getBuilder(Repository repository){
			return new Builder(repository);
		}
		
		public Repository build(){
			return repository;
		}
		
		
		public Builder repositoryLocation(String repositoryLocation){
			repository.setRepositoryLocation(repositoryLocation);
			return this;
		}
		
		public Builder repositoryName(String repositoryName){
			repository.setRepositoryName(repositoryName);
			return this;
		}
		
		public Builder active(String active){
			repository.setActive(active);
			return this;
		}
		
		public Builder trunkPath(String trunkPath){
			repository.setTrunkPath(trunkPath);
			return this;
		}
		
		public Builder tagsPath(String tagsPath){
			repository.setTagsPath(tagsPath);
			return this;
		}
		
		public Builder authUserId(String authUserId){
			repository.setAuthUserId(authUserId);
			return this;
		}
		
		public Builder authUserPasswd(String authUserPasswd){
			repository.setAuthUserPasswd(authUserPasswd);
			return this;
		}
		
		public Builder userList(List<User> userList){
			repository.setUsers(userList);
			return this;
		}
		
		
	}
	
}