package com.haks.haksvn.source.model;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

public class ReviewAuth {

	private boolean isCreatable;	// == isUpdatable
	private String loginUserAuthCodeId;
	
	public ReviewAuth(){
		
	}
	
	public boolean getIsCreatable() {
		return isCreatable;
	}

	public void setIsCreatable(boolean isCreatable) {
		this.isCreatable = isCreatable;
	}
	
	public String getLoginUserAuthCodeId() {
		return loginUserAuthCodeId;
	}

	public void setLoginUserAuthCodeId(String loginUserAuthCodeId) {
		this.loginUserAuthCodeId = loginUserAuthCodeId;
	}

	public static class Builder{
		
		private ReviewAuth reviewAuth;
		
		private Builder(ReviewAuth reviewAuth){
			this.reviewAuth = reviewAuth;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new ReviewAuth());
		}
		
		public static Builder getBuilder(ReviewAuth reviewAuth){
			return new Builder(reviewAuth);
		}
		
		public ReviewAuth build(){
			reviewAuth.setLoginUserAuthCodeId(ContextHolder.getLoginUser().getAuthTypeCodeId());
			String loginUserAuthCode = reviewAuth.getLoginUserAuthCodeId();
			reviewAuth.setIsCreatable(CodeUtils.isReviewer(loginUserAuthCode) || CodeUtils.isSystemAdmin(loginUserAuthCode));
			return reviewAuth;
		}
		
		
	} 
}
