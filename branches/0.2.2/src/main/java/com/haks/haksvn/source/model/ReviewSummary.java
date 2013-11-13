package com.haks.haksvn.source.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ReviewSummary {
	
	private boolean isReviewed = false;
	
	private int totalScore;
	
	private List<ReviewComment> reviewCommentList;
	
	private List<ReviewScore> reviewScorePositiveList;
	
	private List<ReviewScore> reviewScoreNeutralList;
	
	private List<ReviewScore> reviewScoreNegativeList;

	public boolean getIsReviewed(){
		return isReviewed;
	}
	
	public void setIsReviewed(boolean isReviewed){
		this.isReviewed = isReviewed;
	}
	
	public int getTotalScore(){
		return totalScore;
	}
	
	public void setTotalScore(int totalScore){
		this.totalScore = totalScore;
	}
	
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
	
	public static class Builder{
		
		private ReviewSummary reviewSummary;
		
		private Builder(ReviewSummary reviewSummary){
			this.reviewSummary = reviewSummary;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewSummary());
		}
		
		public static Builder getBuilder(ReviewSummary reviewSummary){
			return new Builder(reviewSummary);
		}
		
		public ReviewSummary build(){
			return reviewSummary;
		}
		
		public Builder isReviewed(boolean isReviewed){
			reviewSummary.setIsReviewed(isReviewed);
			return this;
		}
		
		public Builder totalScore(int totalScore){
			reviewSummary.setTotalScore(totalScore);
			return this;
		}
		
		public Builder reviewCommentList(List<ReviewComment> reviewCommentList){
			reviewSummary.setReviewCommentList(reviewCommentList);
			return this;
		}
		
		public Builder reviewScorePositiveList(List<ReviewScore> reviewScorePositiveList){
			reviewSummary.setReviewScorePositiveList(reviewScorePositiveList);
			return this;
		}
		
		public Builder reviewScoreNeutralList(List<ReviewScore> reviewScoreNeutralList){
			reviewSummary.setReviewScoreNeutralList(reviewScoreNeutralList);
			return this;
		}
		
		public Builder reviewScoreNegativeList(List<ReviewScore> reviewScoreNegativeList){
			reviewSummary.setReviewScoreNegativeList(reviewScoreNegativeList);
			return this;
		}
	} 
}
