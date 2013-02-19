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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.common.crypto.MD5Crypt;
import com.haks.haksvn.user.model.User;

@Entity
@DynamicUpdate
//@SelectBeforeUpdate
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
	
	@Column(name = "svn_root")
	private String svnRoot;
	
	//@Column(name = "svn_name",nullable = true)
	@Column(name = "svn_name")
	private String svnName;
	
	@Column(name = "trunk_path",nullable = false)
	@NotEmpty(message="trunk path : Mandantory Field")
	private String trunkPath;
	
	@Column(name = "tags_path",nullable = false)
	@NotEmpty(message="tags path : Mandantory Field")
	private String tagsPath;
	
	@Column(name = "branches_path",nullable = false)
	@NotEmpty(message="branches path : Mandantory Field")
	private String branchesPath;
	
	@Column(name = "auth_user_id",nullable = false)
	@NotEmpty(message="user id : Mandantory Field")
	private String authUserId;
	
	@Column(name = "auth_user_passwd",nullable = false)
	@NotEmpty(message="user password : Mandantory Field")
	private String authUserPasswd;
	
	@Column(name = "sync_user",nullable = false)
	private String syncUser = "common.boolean.yn.code.y";
	
	@Column(name = "connect_type")
	private String connectType;
	
	@Column(name = "server_ip")
	private String serverIp;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_passwd")
	private String userPasswd;
	
	@Column(name = "authz_path")
	private String authzPath;
	
	@Column(name = "passwd_path")
	private String passwdPath;
	
	@Column(name = "passwd_type")
	private String passwdType;
	
	@Column(name = "authz_template", length=2000)
	private String authzTemplate;
	
	@ManyToMany(targetEntity = User.class, fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	//@Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
	//@Cascade({org.hibernate.annotations.CascadeType.DETACH})
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "repositories_users", 
			joinColumns = { @JoinColumn(name = "repository_seq", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_seq",nullable = false, updatable = false) },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"repository_seq","user_seq"})})
	private List<User> userList = new ArrayList<User>();
	
	/*
	@OneToOne(targetEntity = RepositoryServer.class,fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(value={org.hibernate.annotations.CascadeType.DELETE})
	@JoinTable(name = "repository_server", 
		joinColumns = { @JoinColumn(name = "repository_seq", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "repository_seq",nullable = false, updatable = false) })
	private RepositoryServer repositoryServer;
	*/
	
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
					"\n - repositoryName : " + repositoryName + "\n - active : " + active  + repositorySeq + "\n - connectType : " + connectType + "\n - syncUser : " + syncUser +
					"\n - serverIp : " + serverIp + "\n - authzPath : " + authzPath  + "\n - passwdPath : " + passwdPath + "\n - passwdType : " + passwdType ;
	}
	
	//TODO 
	// util 비슷한 아래 메써드를 따로 빼야 하는가
	public String encryptPasswd(String passwd){
		if( "svn.passwd.type.code.md5-apache".equals(passwdType) ) return MD5Crypt.apacheCrypt(passwd);
		return passwd;
	}
	
	public String getPasswdFileDelimeter() {
		if( "svn.passwd.type.code.md5-apache".equals(passwdType) ) return ":";
		return "=";
	}
	
	public boolean usingSyncUser(){
		return "common.boolean.yn.code.y".equals(syncUser);
	}
	
	public boolean usingLocalConnect(){
		return "server.connect.type.code.local".equals(connectType);
	}
	
	public void formatAuthzTemplate(){
		if( !usingSyncUser() ){
			authzTemplate = null;
			return;
		}
		authzTemplate = authzTemplate.replaceAll("\r\n", "%n").replaceAll("\n","%n");
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
	
	public String getSvnRoot(){
		return svnRoot;
	}
	
	public void setSvnRoot(String svnRoot){
		this.svnRoot = svnRoot;
	}
	
	public String getSvnName(){
		return svnName;
	}
	
	public void setSvnName(String svnName){
		this.svnName = svnName;
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
	
	public String getBranchesPath() {
		return branchesPath;
	}

	public void setBranchesPath(String branchesPath) {
		this.branchesPath = branchesPath;
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
	
	public String getSyncUser(){
		return syncUser;
	}
	
	
	
	public void setSyncUser(String syncUser){
		this.syncUser = syncUser;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUsers(List<User> userList) {
		this.userList = userList;
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
	
	public String getAuthzTemplate(){
		return authzTemplate;
	}
	
	public void setAuthzTemplate(String authzTemplate){
		this.authzTemplate = authzTemplate;
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
		
		public Builder repositorySeq(int repositorySeq){
			repository.setRepositorySeq(repositorySeq);
			return this;
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
		
		public Builder svnName(String svnName){
			repository.setSvnName(svnName);
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
		
		public Builder branchesPath(String branchesPath){
			repository.setBranchesPath(branchesPath);
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
		
		public Builder syncUser(String syncUser){
			repository.setSyncUser(syncUser);
			return this;
		}
		
		public Builder userList(List<User> userList){
			repository.setUsers(userList);
			return this;
		}
		
		public Builder connectType(String connectType){
			repository.setConnectType(connectType);
			return this;
		}
		
		public Builder serverIp(String serverIp){
			repository.setServerIp(serverIp);
			return this;
		}
		
		public Builder userId(String userId){
			repository.setUserId(userId);
			return this;
		}
		
		public Builder userPasswd(String userPasswd){
			repository.setUserPasswd(userPasswd);
			return this;
		}
		
		public Builder authzPath(String authzPath){
			repository.setAuthzPath(authzPath);
			return this;
		}
		
		public Builder passwdPath(String passwdPath){
			repository.setPasswdPath(passwdPath);
			return this;
		}
		
		public Builder passwdType(String passwdType){
			repository.setPasswdType(passwdType);
			return this;
		}
		
		public Builder authzTemplate(String authzTemplate){
			repository.setAuthzTemplate(authzTemplate);
			return this;
		}
		
	}
	
}
