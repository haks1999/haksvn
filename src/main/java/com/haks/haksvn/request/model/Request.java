package com.haks.haksvn.request.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transfer")
public class Request {
	
	public Request(){}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "transfer_seq",unique = true, nullable = false)
	private int transferSeq;
	@Column(name = "request_type",unique = true, nullable = false)
	private String requestType;
	@Column(name = "request_result",unique = true, nullable = false)
	private String requestResult;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "request_date")
	private Date requestDate;
	@Column(name = "transfer_date")
	private Date transferDate;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTransferSeq() {
		return transferSeq;
	}
	public void setTransferSeq(int transferSeq) {
		this.transferSeq = transferSeq;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	
	
}
