package com.haks.haksvn.transfer.model;

import java.util.List;

import javax.persistence.CascadeType;
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.user.model.User;

@Entity
@Table(name="transfer_group")
public class TransferGroup {
	
	public TransferGroup(){
		
	}
	
	@Id
	@GenericGenerator(name="transferGroupSeqGen" , strategy="increment")
	@GeneratedValue(generator="transferGroupSeqGen")
	@Column(name = "transfer_group_seq",unique = true, nullable = false)
	private int transferGroupSeq;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_group_type", referencedColumnName="code_id")
	private Code transferGroupTypeCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_group_state", referencedColumnName="code_id")
	private Code transferGroupStateCode;
	
	@Column(name = "title", nullable = false)
	@NotEmpty
	@Length(min=10, max=50)
	private String title;
	
	@Column(name = "description", nullable = false)
	@NotEmpty
	@Length(min=10, max=2000)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="transfer_user_id", referencedColumnName="user_id", nullable=true)
	private User transferUser;
	
	@Column(name = "transfer_date")
	private long transferDate;
	
	// 연관 관계는 맺지 않는다. 불필요
	@Column(name = "repository_seq")
	private int repositorySeq;
	
	@OneToMany(cascade = { CascadeType.PERSIST }, mappedBy = "transferGroup")
	private List<Transfer> transferList;
	
	public int getTransferGroupSeq() {
		return transferGroupSeq;
	}

	public void setTransferGroupSeq(int transferGroupSeq) {
		this.transferGroupSeq = transferGroupSeq;
	}

	public Code getTransferGroupTypeCode() {
		return transferGroupTypeCode;
	}

	public void setTransferGroupTypeCode(Code transferGroupTypeCode) {
		this.transferGroupTypeCode = transferGroupTypeCode;
	}

	public Code getTransferGroupStateCode() {
		return transferGroupStateCode;
	}

	public void setTransferGroupStateCode(Code transferGroupStateCode) {
		this.transferGroupStateCode = transferGroupStateCode;
	}

	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title =title;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getRepositorySeq() {
		return repositorySeq;
	}

	public void setRepositorySeq(int repositorySeq) {
		this.repositorySeq = repositorySeq;
	}

	public List<Transfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<Transfer> transferList) {
		this.transferList = transferList;
	}

	public static class Builder{
		
		private TransferGroup transferGroup;
		
		private Builder(TransferGroup transferGroup){
			this.transferGroup = transferGroup;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TransferGroup());
		}
		
		public static Builder getBuilder(TransferGroup transferGroup){
			return new Builder(transferGroup);
		}
		
		public TransferGroup build(){
			return transferGroup;
		}
		
		public Builder transferGroupSeq(int transferGroupSeq){
			transferGroup.setTransferGroupSeq(transferGroupSeq);
			return this;
		}
		
		public Builder transferGroupTypeCode(Code code){
			transferGroup.setTransferGroupTypeCode( code );
			return this;
		}
		
		public Builder transferGroupStateCode(Code code){
			transferGroup.setTransferGroupStateCode( code );
			return this;
		}
		
		public Builder title(String title){
			transferGroup.setTitle(title);
			return this;
		}
		
		public Builder description(String description){
			transferGroup.setDescription(description);
			return this;
		}
		
		public Builder transferUser(User user){
			transferGroup.setTransferUser(user);
			return this;
		}
		
		public Builder transferDate(long transferDate){
			transferGroup.setTransferDate(transferDate);
			return this;
		}
		
		public Builder repositorySeq(int repositorySeq){
			transferGroup.setRepositorySeq(repositorySeq);
			return this;
		}
		
		public Builder transferList(List<Transfer> transferList){
			transferGroup.setTransferList(transferList);
			return this;
		}
		
	} 
	
	
	
}
