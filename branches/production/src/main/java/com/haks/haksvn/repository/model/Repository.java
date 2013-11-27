package com.haks.haksvn.repository.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.user.model.User;

@Entity
@DynamicUpdate
//@SelectBeforeUpdate
@Table(name="repositories")
public class Repository{

	@Id
	@Column(name = "repository_key",unique = true, nullable = false)
	@Length(min=5, max=15)
	@Pattern(regexp="^[A-Z]+$")
    private String repositoryKey;
	
	@Column(name = "repository_location",nullable = false)
	@NotEmpty
	private String repositoryLocation;
	
	@Column(name = "repository_name",nullable = false)
	@NotEmpty
	@Length(min=5, max=50)
	private String repositoryName;
	
	@Column(name = "active",nullable = true)
	private String active;
	
	@Column(name = "svn_root")
	private String svnRoot;
	
	@Column(name = "svn_name")
	private String svnName;
	
	@Column(name = "trunk_path",nullable = false)
	@NotEmpty
	@Pattern(regexp="^/.+[^/]$")
	private String trunkPath;
	
	@Column(name = "tags_path",nullable = false)
	@NotEmpty
	@Pattern(regexp="^/.+[^/]$")
	private String tagsPath;
	
	@Column(name = "branches_path",nullable = false)
	@NotEmpty
	@Pattern(regexp="^/.+[^/]$")
	private String branchesPath;
	
	@Column(name = "auth_user_id",nullable = false)
	@NotEmpty
	private String authUserId;
	
	@Column(name = "auth_user_passwd",nullable = false)
	@NotEmpty
	private String authUserPasswd;
	
	@Column(name = "sync_user",nullable = false)
	private String syncUser = "common.boolean.yn.code.y";
	
	@Column(name = "connect_type")
	private String connectType;
	
	@Column(name = "server_ip")
	private String serverIp;
	
	@Column(name = "server_user_id")
	private String serverUserId;
	
	@Column(name = "server_user_passwd")
	private String serverUserPasswd;
	
	@Column(name = "authz_path")
	private String authzPath;
	
	@Column(name = "passwd_path")
	private String passwdPath;
	
	@Column(name = "passwd_type")
	private String passwdType;
	
	@Column(name = "authz_template", length=2000)
	private String authzTemplate;
	
	@Column(name = "repository_order")
	private int repositoryOrder;
	
	@ManyToMany(targetEntity = User.class, fetch=FetchType.EAGER)
	//@org.hibernate.annotations.Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
	//@Cascade(org.hibernate.annotations.CascadeType.REPLICATE)
	//@Cascade({org.hibernate.annotations.CascadeType.DETACH})
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "repositories_users", 
			joinColumns = { @JoinColumn(name = "repository_key", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_seq",nullable = false, updatable = false) },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"repository_key","user_seq"})})
	private List<User> userList = new ArrayList<User>();
	
	@Transient
	private boolean authUserPasswdEncrypted = false;
	
	public Repository(){
		
	}
	
	
	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
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

	public String getServerUserId() {
		return serverUserId;
	}

	public void setServerUserId(String serverUserId) {
		this.serverUserId = serverUserId;
	}

	public String getServerUserPasswd() {
		return serverUserPasswd;
	}

	public void setServerUserPasswd(String serverUserPasswd) {
		this.serverUserPasswd = serverUserPasswd;
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
	
	public boolean getAuthUserPasswdEncrypted(){
		return authUserPasswdEncrypted;
	}
	
	public void setAuthUserPasswdEncrypted(boolean authUserPasswdEncrypted){
		this.authUserPasswdEncrypted = authUserPasswdEncrypted;
	}
	
	public int getRepositoryOrder(){
		return repositoryOrder;
	}
	
	public void setRepositoryOrder(int repositoryOrder){
		this.repositoryOrder = repositoryOrder;
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
		
		public Builder repositoryKey(String repositoryKey){
			repository.setRepositoryKey(repositoryKey);
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
		
		public Builder svnRoot(String svnRoot){
			repository.setSvnRoot(svnRoot);
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
		
		public Builder serverUserId(String serverUserId){
			repository.setServerUserId(serverUserId);
			return this;
		}
		
		public Builder serverUserPasswd(String serverUserPasswd){
			repository.setServerUserPasswd(serverUserPasswd);
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
		
		public Builder repositoryOrder(int repositoryOrder){
			repository.setRepositoryOrder(repositoryOrder);
			return this;
		}
		
	}
	
}
