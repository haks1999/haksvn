package com.haks.haksvn.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="repositories")
public class Repository {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "repository_seq",unique = true, nullable = false)
    private int repositorySeq;
	
	@Column(name = "repository_location",nullable = false)
	private String repositoryLocation;
	
	@Column(name = "repository_name",nullable = false)
	private String repositoryName;
	
	@Column(name = "repository_status",nullable = true)
	private String repositoryStatus;
	
	public Repository(){
		
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

	
}
