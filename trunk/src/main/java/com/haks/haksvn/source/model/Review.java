package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class Review {
	
	private ReviewId reviewId;

	private int score;
	
	private String comment;
	
	public ReviewId getReviewId(){
		return reviewId;
	}
	
	public void setReviewId(ReviewId reviewId){
		this.reviewId = reviewId;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public String getComment(){
		return comment;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}

	public static class Builder{
		
		private Review review;
		
		private Builder(Review review){
			this.review = review;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new Review());
		}
		
		public static Builder getBuilder(Review review){
			return new Builder(review);
		}
		
		public Review build(){
			return review;
		}
		
		public Builder reviewId(ReviewId reviewId){
			review.setReviewId(reviewId);
			return this;
		}
		
		public Builder score(int score){
			review.setScore(score);
			return this;
		}
		
		public Builder comment(String comment){
			review.setComment(comment);
			return this;
		}
	} 
}
