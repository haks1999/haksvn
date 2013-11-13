package com.haks.haksvn.source.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name="review_score")
public class ReviewScore {

	@EmbeddedId
    private ReviewId reviewId;
	
	@Column(name="score")
	@Range(min = -1, max = 1)
	private int score = 0;

	public ReviewId getReviewId() {
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
	
	public static class Builder{
		
		private ReviewScore reviewScore;
		
		private Builder(ReviewScore reviewScore){
			this.reviewScore = reviewScore;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewScore());
		}
		
		public static Builder getBuilder(ReviewScore reviewScore){
			return new Builder(reviewScore);
		}
		
		public ReviewScore build(){
			return reviewScore;
		}
		
		public Builder reviewId(ReviewId reviewId){
			reviewScore.setReviewId(reviewId);
			return this;
		}
		
		public Builder score(int score){
			reviewScore.setScore(score);
			return this;
		}
	} 
}
