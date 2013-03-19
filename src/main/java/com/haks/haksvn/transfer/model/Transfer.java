package com.haks.haksvn.transfer.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.user.model.User;

@Entity
@Table(name="transfer")
public class Transfer {
	
	public Transfer(){
		
	}
	
	@Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
	@GenericGenerator(name="transferSeqGen" , strategy="increment")
	@GeneratedValue(generator="transferSeqGen")
	@Column(name = "transfer_seq",unique = true, nullable = false)
	private int transferSeq;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_type", referencedColumnName="code_id")
	private Code transferTypeCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_state", referencedColumnName="code_id")
	private Code transferStateCode;
	
	@Column(name = "description", nullable = false, length=2000)
	@NotEmpty(message="description : Mandantory Field")
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="request_user_id", referencedColumnName="user_id")
	private User requestUser;
	
	@Column(name = "request_date", nullable = false)
	private long requestDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_user_id", referencedColumnName="user_id", nullable=true)
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
	
	public static class Builder{
		
		private Transfer transfer;
		
		private Builder(Transfer transfer){
			this.transfer = transfer;
		}
		
		public static Builder getBuilder(Transfer transfer){
			return new Builder(transfer);
		}
		
		public Transfer build(){
			return transfer;
		}
		
		public Builder transferSeq(int transferSeq){
			transfer.setTransferSeq(transferSeq);
			return this;
		}
		
		public Builder transferTypeCode(Code code){
			transfer.setTransferTypeCode( code );
			return this;
		}
		
		public Builder transferStateCode(Code code){
			transfer.setTransferStateCode( code );
			return this;
		}
		
		public Builder description(String description){
			transfer.setDescription(description);
			return this;
		}
		
		public Builder requestUser(User user){
			transfer.setRequestUser(user);
			return this;
		}
		
		public Builder requestDate(long requestDate){
			transfer.setRequestDate(requestDate);
			return this;
		}
		
		public Builder trasnferUser(User user){
			transfer.setTransferUser(user);
			return this;
		}
		
		public Builder transferDate(long transferDate){
			transfer.setTransferDate(transferDate);
			return this;
		}
		
		public Builder repositorySeq(int repositorySeq){
			transfer.setRepositorySeq(repositorySeq);
			return this;
		}
		
		public Builder sourceList(List<TransferSource> sourceList){
			transfer.setSourceList(sourceList);
			return this;
		}
		
	} 
	
	
	
}
