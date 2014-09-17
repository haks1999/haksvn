package com.haks.haksvn.source.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.haks.haksvn.user.model.User;

@Entity
@Table(name="review_request")
public class ReviewRequest{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "review_request_seq",unique = true, nullable = false)
    private int reviewRequestSeq;
	
	@Column(name = "request_date", nullable=false)
	private long requestDate;
	
	// 연관 관계는 맺지 않는다. 불필요
	@Column(name = "repository_key", nullable=false)
	private String repositoryKey;
	
	@Column(name="revision", nullable=false)
	private long revision;
	
	@ManyToMany(targetEntity = User.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "review_request_users", 
			joinColumns = { @JoinColumn(name = "review_request_seq", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_seq",nullable = false, updatable = false) },
			uniqueConstraints = { @UniqueConstraint(columnNames = {"review_request_seq","user_seq"})})
	private List<User> reviewers = new ArrayList<User>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="requestor", referencedColumnName="user_seq", updatable=false)
	private User requestor;// = new Code();
	
	public ReviewRequest(){
		
	}
	
	
	public int getReviewRequestSeq() {
		return reviewRequestSeq;
	}


	public void setReviewRequestSeq(int reviewRequestSeq) {
		this.reviewRequestSeq = reviewRequestSeq;
	}


	public long getRequestDate() {
		return requestDate;
	}


	public void setRequestDate(long requestDate) {
		this.requestDate = requestDate;
	}


	public String getRepositoryKey() {
		return repositoryKey;
	}


	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}

	public long getRevision(){
		return revision;
	}
	
	public void setRevision(long revision){
		this.revision = revision;
	}

	public List<User> getReviewers() {
		return reviewers;
	}


	public void setReviewers(List<User> reviewers) {
		this.reviewers = reviewers;
	}

	public User getRequestor(){
		return requestor;
	}
	
	public void setRequestor(User requestor){
		this.requestor = requestor;
	}

	public static class Builder{
		
		private ReviewRequest reviewRequest;
		
		private Builder(ReviewRequest reviewRequest){
			this.reviewRequest = reviewRequest;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewRequest());
		}
		
		public static Builder getBuilder(ReviewRequest reviewRequest){
			return new Builder(reviewRequest);
		}
		
		public ReviewRequest build(){
			return reviewRequest;
		}
		
		public Builder reviewRequestSeq(int reviewRequestSeq){
			reviewRequest.setReviewRequestSeq(reviewRequestSeq);
			return this;
		}
		
		public Builder requestDate(long requestDate){
			reviewRequest.setRequestDate(requestDate);
			return this;
		}
		
		public Builder repositoryKey(String repositoryKey){
			reviewRequest.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder revision(long revision){
			reviewRequest.setRevision(revision);
			return this;
		}
		
		public Builder reviewers(List<User> reviewers){
			reviewRequest.setReviewers(reviewers);
			return this;
		}
		
		public Builder requestor(User requestor){
			reviewRequest.setRequestor(requestor);
			return this;
		}
	}
	
}
