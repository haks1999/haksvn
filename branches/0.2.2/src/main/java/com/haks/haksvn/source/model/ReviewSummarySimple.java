package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class ReviewSummarySimple {
	
	private boolean isReviewed = false;
	
	private int totalScore;
	
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
	
	public static class Builder{
		
		private ReviewSummarySimple reviewSummarySimple;
		
		private Builder(ReviewSummarySimple reviewSummarySimple){
			this.reviewSummarySimple = reviewSummarySimple;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewSummarySimple());
		}
		
		public static Builder getBuilder(ReviewSummarySimple reviewSummarySimple){
			return new Builder(reviewSummarySimple);
		}
		
		public ReviewSummarySimple build(){
			return reviewSummarySimple;
		}
		
		public Builder isReviewed(boolean isReviewed){
			reviewSummarySimple.setIsReviewed(isReviewed);
			return this;
		}
		
		public Builder totalScore(int totalScore){
			reviewSummarySimple.setTotalScore(totalScore);
			return this;
		}
	} 
}
