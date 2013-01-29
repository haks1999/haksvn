package com.haks.haksvn.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="repositories")
public class Repository implements Serializable{

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

	
}
