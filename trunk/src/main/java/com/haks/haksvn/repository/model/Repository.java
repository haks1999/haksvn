package com.haks.haksvn.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.MetaValue;

import com.haks.haksvn.common.code.model.Code;

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
	
	@Column(name = "repository_state",nullable = false, insertable = false, updatable = false)
	private String repositoryState;
	
	/**
	@ManyToMany(cascade = CascadeType.ALL )
	@JoinTable(name = "code", joinColumns = { @JoinColumn(name = "code_value") })
	//@Column(name = "repository_state",nullable = false, insertable = false, updatable = false)
	@Filter(name="getCodeName", condition="code_group=repository.state")
	**/
	@Any( metaColumn = @Column( name = "repository_state", insertable = false, updatable = false ), fetch=FetchType.EAGER )
    @AnyMetaDef( 
        idType = "string", 
        metaType = "string", 
        metaValues = {
            @MetaValue( value = "C", targetEntity = Code.class ),
        } )
    @JoinColumn( name = "code_value" )
	@Filter(name="getCodeName", condition="code_group=repository.state")
	private Code code;
	
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

	public String getRepositoryState() {
		return repositoryState;
	}

	public void setRepositoryState(String repositoryState) {
		this.repositoryState = repositoryState;
	}
	
	public Code getCode(){
		return code;
	}
	
	public void setCode( Code code ){
		this.code = code;
	}

	
}
