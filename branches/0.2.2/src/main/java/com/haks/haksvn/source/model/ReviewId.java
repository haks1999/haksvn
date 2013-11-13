package com.haks.haksvn.source.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.haks.haksvn.user.model.User;

@SuppressWarnings("serial")
@Embeddable
public class ReviewId implements Serializable{

	@Column(name="repository_key")
	private String repositoryKey;
	
	@Column(name="revision")
	private long revision;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="reviewer", referencedColumnName="user_id", insertable=false, updatable=false)
	private User reviewer;

	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}
	
	public User getReviewer(){
		return reviewer;
	}
	
	public void setReviewer(User reviewer){
		this.reviewer = reviewer;
	}
	
	public static class Builder{
		
		private ReviewId reviewId;
		
		private Builder(ReviewId reviewId){
			this.reviewId = reviewId;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewId());
		}
		
		public static Builder getBuilder(ReviewId reviewId){
			return new Builder(reviewId);
		}
		
		public ReviewId build(){
			return reviewId;
		}
		
		public Builder repositoryKey(String repositoryKey){
			reviewId.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder revision(long revision){
			reviewId.setRevision( revision );
			return this;
		}
		
		public Builder reviewer(User reviewer){
			reviewId.setReviewer(reviewer);
			return this;
		}
	} 
	
}
