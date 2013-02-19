package com.haks.haksvn.user.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.repository.model.Repository;

@Entity
@Table(name="users")
@FilterDef(name="authAuthTypeCodeFilter", parameters = {
        @ParamDef(name = "codeGroup", type = "string")
        })
public class User{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_seq",unique = true, nullable = false)
    private int userSeq;
	
	@Column(name = "user_id",unique = true, nullable = false)
	@NotEmpty(message="user id : Mandantory Field")
	private String userId;
	
	@Column(name = "user_name",nullable = false)
	@NotEmpty(message="user name : Mandantory Field")
	private String userName;
	
	@Column(name = "active",nullable = true)
	private String active;
	
	@Column(name = "email",nullable = false)
	private String email;
	
	@Column(name = "user_passwd",nullable = false)
	private String userPasswd;
	
	@Column(name = "auth_type",nullable = false)
	private String authType;
	
	//@ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
	/*
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "repositories_users", joinColumns = { 
			@JoinColumn(name = "user_seq", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "repository_seq", 
					nullable = false, updatable = false) })
	private Set<Repository> repositories = new HashSet<Repository>();
	*/
	
	/*
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "code", joinColumns = { 
			@JoinColumn(name = "code_value", referencedColumnName = "auth_type", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "auth_type", referencedColumnName = "code_value", nullable = false) })
	@WhereJoinTable(clause = "code_group='user_auth_type_code'")
	*/
	/*
	@Transient
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinFormula(value="SELECT c.* FROM code c WHERE c.code_group='user_auth_type_code'")
	*/
	//@Formula(value="SELECT c.* FROM code c WHERE c.code_group='user_auth_type_code'")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="auth_type", referencedColumnName="code_id", insertable=false, updatable=false)
	//@JoinTable(name = "code", joinColumns = { @JoinColumn(name = "code_id", referencedColumnName = "auth_type", nullable = false) })
	//@WhereJoinTable(clause = "code_group='user_auth_type_code'")
	private Code authTypeCode;// = new Code();
	
	
	@ManyToMany(mappedBy="userList", fetch=FetchType.EAGER)
	//@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Repository> repositoryList;
	/*
	@ManyToMany(fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "repositories_users", joinColumns = { 
			@JoinColumn(name = "user_seq", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "repository_seq", 
					nullable = false, updatable = false) })
					*/
	/*
	@ManyToMany(fetch=FetchType.EAGER, mappedBy = "userList")
	@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Repository> repositoryList = new ArrayList<Repository>();
	*/
	public User(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ User ]\n - userSeq : " + userSeq + "\n - userId : " + userId +
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
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

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public Code getAuthTypeCode() {
		return authTypeCode;
	}

	public void setAuthTypeCode(Code authTypeCode) {
		this.authTypeCode = authTypeCode;
	}
	
	@JsonIgnore
	public List<Repository> getRepositoryList() {
		return repositoryList;
	}

	public void setRepositoryList(List<Repository> repositoryList) {
		this.repositoryList = repositoryList;
	}
	
	/*
	public Set<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<Repository> repositories) {
		this.repositories = repositories;
	}
	*/
	
	



	public static class Builder{
		
		private User user;
		
		private Builder(User user){
			this.user = user;
		}
		
		public static Builder getBuilder(User user){
			return new Builder(user);
		}
		
		public User build(){
			return user;
		}
		
		public Builder userSeq(int userSeq){
			user.setUserSeq(userSeq);
			return this;
		}
		
		public Builder userId(String userId){
			user.setUserId(userId);
			return this;
		}
		
		public Builder userName(String userName){
			user.setUserName(userName);
			return this;
		}
		
		public Builder active(String active){
			user.setActive(active);
			return this;
		}
		
		public Builder email(String email){
			user.setEmail(email);
			return this;
		}
		
		public Builder userPasswd(String userPasswd){
			user.setUserPasswd(userPasswd);
			return this;
		}
		
		public Builder authType(String authType){
			user.setAuthType(authType);
			return this;
		}
		
		public Builder authTypeCode(Code authTypeCode){
			user.setAuthTypeCode(authTypeCode);
			return this;
		}
	}
	
}
