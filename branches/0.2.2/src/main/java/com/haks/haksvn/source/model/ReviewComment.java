package com.haks.haksvn.source.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.haks.haksvn.user.model.User;

@Entity
@Table(name="review_comment")
public class ReviewComment {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "review_comment_seq",unique = true, nullable = false)
	private int reviewCommentSeq; 
	
	@Column(name="repository_key")
	private String repositoryKey;
	
	@Column(name="revision")
	private long revision;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="reviewer", referencedColumnName="user_id")
	private User reviewer;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="comment_date")
	private long commentDate;
	
	@Transient
	private ReviewCommentAuth reviewCommentAuth;

	public int getReviewCommentSeq() {
		return reviewCommentSeq;
	}

	public void setReviewCommentSeq(int reviewCommentSeq){
		this.reviewCommentSeq = reviewCommentSeq;
	}
	
	public String getRepositoryKey(){
		return repositoryKey;
	}
	
	public void setRepositoryKey(String repositoryKey){
		this.repositoryKey = repositoryKey;
	}
	
	public long getRevision(){
		return revision;
	}
	
	public void setRevision(long revision){
		this.revision = revision;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(long commentDate) {
		this.commentDate = commentDate;
	}
	
	public ReviewCommentAuth  getReviewCommentAuth(){
		return reviewCommentAuth;
	}
	
	public void setReviewCommentAuth(ReviewCommentAuth reviewCommentAuth){
		this.reviewCommentAuth = reviewCommentAuth;
	}
	
	public static class Builder{
		
		private ReviewComment reviewComment;
		
		private Builder(ReviewComment reviewComment){
			this.reviewComment = reviewComment;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewComment());
		}
		
		public static Builder getBuilder(ReviewComment reviewComment){
			return new Builder(reviewComment);
		}
		
		public ReviewComment build(){
			return reviewComment;
		}
		
		public Builder reviewCommentSeq(int reviewCommentSeq){
			reviewComment.setReviewCommentSeq(reviewCommentSeq);
			return this;
		}
		
		public Builder repositoryKey(String repositoryKey){
			reviewComment.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder revision(long revision){
			reviewComment.setRevision(revision);
			return this;
		}
		
		public Builder reviewer(User reviewer){
			reviewComment.setReviewer( reviewer );
			return this;
		}
		
		public Builder comment(String comment){
			reviewComment.setComment(comment);
			return this;
		}
		
		public Builder commentDate(long commentDate){
			reviewComment.setCommentDate(commentDate);
			return this;
		}
		
		public Builder reviewCommentAuth(){
			reviewComment.setReviewCommentAuth(ReviewCommentAuth.Builder.getBuilder().reviewerId(reviewComment.getReviewer().getUserId()).build());
			return this;
		}
	} 
	
}
