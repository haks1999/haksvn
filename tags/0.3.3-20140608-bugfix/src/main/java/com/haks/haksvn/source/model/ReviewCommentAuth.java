package com.haks.haksvn.source.model;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

public class ReviewCommentAuth {

	private boolean isDeletable;
	private String loginUserAuthCodeId;
	private String reviewerId;
	private String loginUserId;
	
	public ReviewCommentAuth(){
		
	}
	
	public boolean getIsDeletable() {
		return isDeletable;
	}

	public void setIsDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}
	
	public String getLoginUserAuthCodeId() {
		return loginUserAuthCodeId;
	}

	public void setLoginUserAuthCodeId(String loginUserAuthCodeId) {
		this.loginUserAuthCodeId = loginUserAuthCodeId;
	}
	
	public String getReviewerId(){
		return reviewerId;
	}
	
	public void setReviewerId(String reviewerId){
		this.reviewerId = reviewerId;
	}
	
	public String getLoginUserId(){
		return loginUserId;
	}
	
	public void setLoginUserId(String loginUserId){
		this.loginUserId = loginUserId;
	}

	public static class Builder{
		
		private ReviewCommentAuth reviewCommentAuth;
		
		private Builder(ReviewCommentAuth reviewCommentAuth){
			this.reviewCommentAuth = reviewCommentAuth;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewCommentAuth());
		}
		
		public static Builder getBuilder(ReviewCommentAuth reviewCommentAuth){
			return new Builder(reviewCommentAuth);
		}
		
		public Builder reviewerId(String reviewerId){
			reviewCommentAuth.setReviewerId(reviewerId);
			return this;
		}
		
		public ReviewCommentAuth build(){
			reviewCommentAuth.setLoginUserAuthCodeId(ContextHolder.getLoginUser().getAuthTypeCodeId());
			reviewCommentAuth.setLoginUserId(ContextHolder.getLoginUser().getUserId());
			String loginUserAuthCode = reviewCommentAuth.getLoginUserAuthCodeId();
			String loginUserId = reviewCommentAuth.getLoginUserId();
			String reviewerId = reviewCommentAuth.getReviewerId();
			reviewCommentAuth.setIsDeletable(CodeUtils.isSystemAdmin(loginUserAuthCode) || loginUserId.equals(reviewerId));
			return reviewCommentAuth;
		}
		
		
	} 
}
