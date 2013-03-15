package com.haks.haksvn.transfer.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.user.model.User;

@Entity
@Table(name="transfer")
public class Transfer {
	
	public Transfer(){
		
	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "transfer_seq",unique = true, nullable = false)
	private int transferSeq;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_type", referencedColumnName="code_id")
	private Code transferTypeCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_state", referencedColumnName="code_id")
	private Code transferStateCode;
	
	@Column(name = "description", nullable = false, length=2000)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="request_user_id", referencedColumnName="user_id")
	private User requestUser;
	
	@Column(name = "request_date", nullable = false)
	private long requestDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_user_id", referencedColumnName="user_id")
	private User transferUser;
	
	@Column(name = "transfer_date")
	private long transferDate;
	
	// 연관 관계는 맺지 않는다. 불필요
	@Column(name = "repository_seq")
	private int repositorySeq;
	
	@OneToMany(mappedBy="transfer")
	private List<TransferSource> sourceList;
	

	public int getTransferSeq() {
		return transferSeq;
	}

	public void setTransferSeq(int transferSeq) {
		this.transferSeq = transferSeq;
	}

	public Code getTransferTypeCode() {
		return transferTypeCode;
	}

	public void setTransferTypeCode(Code transferTypeCode) {
		this.transferTypeCode = transferTypeCode;
	}

	public Code getTransferStateCode() {
		return transferStateCode;
	}

	public void setTransferStateCode(Code transferStateCode) {
		this.transferStateCode = transferStateCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(User requestUser) {
		this.requestUser = requestUser;
	}

	public long getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(long requestDate) {
		this.requestDate = requestDate;
	}

	public User getTransferUser() {
		return transferUser;
	}

	public void setTransferUser(User transferUser) {
		this.transferUser = transferUser;
	}

	public long getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(long transferDate) {
		this.transferDate = transferDate;
	}
	
	public int getRepositorySeq(){
		return repositorySeq;
	}
	
	public void setRepositorySeq(int repositorySeq){
		this.repositorySeq = repositorySeq;
	}

	public List<TransferSource> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<TransferSource> sourceList) {
		this.sourceList = sourceList;
	}
	
	
	
	
	
}
