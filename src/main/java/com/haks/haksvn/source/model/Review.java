package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class Review {
	
	private ReviewId reviewId;

	private int score;
	
	private String comment;
	
	//private List<ReviewComment> reviewCommentList;
	
	//private List<ReviewScore> reviewScorePositiveList;
	
	//private List<ReviewScore> reviewScoreNeutralList;
	
	//private List<ReviewScore> reviewScoreNegativeList;

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

	/*
	public List<ReviewComment> getReviewCommentList() {
		return reviewCommentList;
	}

	public void setReviewCommentList(List<ReviewComment> reviewCommentList) {
		this.reviewCommentList = reviewCommentList;
	}

	public List<ReviewScore> getReviewScorePositiveList() {
		return reviewScorePositiveList;
	}

	public void setReviewScorePositiveList(List<ReviewScore> reviewScorePositiveList) {
		this.reviewScorePositiveList = reviewScorePositiveList;
	}

	public List<ReviewScore> getReviewScoreNeutralList() {
		return reviewScoreNeutralList;
	}

	public void setReviewScoreNeutralList(List<ReviewScore> reviewScoreNeutralList) {
		this.reviewScoreNeutralList = reviewScoreNeutralList;
	}

	public List<ReviewScore> getReviewScoreNegativeList() {
		return reviewScoreNegativeList;
	}

	public void setReviewScoreNegativeList(List<ReviewScore> reviewScoreNegativeList) {
		this.reviewScoreNegativeList = reviewScoreNegativeList;
	}
	*/
	
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
		
		public Builder score(int score){
			review.setScore(score);
			return this;
		}
		
		public Builder comment(String comment){
			review.setComment(comment);
			return this;
		}
		
		/*
		public Builder reviewCommentList(List<ReviewComment> reviewCommentList){
			review.setReviewCommentList(reviewCommentList);
			return this;
		}
		
		public Builder reviewScorePositiveList(List<ReviewScore> reviewScorePositiveList){
			review.setReviewScorePositiveList(reviewScorePositiveList);
			return this;
		}
		
		public Builder reviewScoreNeutralList(List<ReviewScore> reviewScoreNeutralList){
			review.setReviewScoreNeutralList(reviewScoreNeutralList);
			return this;
		}
		
		public Builder reviewScoreNegativeList(List<ReviewScore> reviewScoreNegativeList){
			review.setReviewScoreNegativeList(reviewScoreNegativeList);
			return this;
		}
		*/
	} 
}
