package com.haks.haksvn.transfer.model;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

public class TaggingAuth {

	private boolean isCreatable;
	private boolean isRestorable;
	
	private String typeCodeId;
	private String loginUserAuthCodeId;
	
	public TaggingAuth(){
		
	}
	
	public boolean getIsCreatable() {
		return isCreatable;
	}

	public void setIsCreatable(boolean isCreatable) {
		this.isCreatable = isCreatable;
	}

	public boolean getIsRestorable() {
		return isRestorable;
	}

	public void setIsRestorable(boolean isRestorable) {
		this.isRestorable = isRestorable;
	}

	public String getTypeCodeId() {
		return typeCodeId;
	}

	public void setTypeCodeId(String typeCodeId) {
		this.typeCodeId = typeCodeId;
	}

	public String getLoginUserAuthCodeId() {
		return loginUserAuthCodeId;
	}

	public void setLoginUserAuthCodeId(String loginUserAuthCodeId) {
		this.loginUserAuthCodeId = loginUserAuthCodeId;
	}

	public static class Builder{
		
		private TaggingAuth taggingAuth;
		
		private Builder(TaggingAuth taggingAuth){
			this.taggingAuth = taggingAuth;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TaggingAuth());
		}
		
		public static Builder getBuilder(TaggingAuth taggingAuth){
			return new Builder(taggingAuth);
		}
		
		public Builder tagging(Tagging tagging){
			taggingAuth.setTypeCodeId(tagging.getTaggingTypeCode()==null?"":tagging.getTaggingTypeCode().getCodeId());
			return this;
		}
		
		public TaggingAuth build(){
			taggingAuth.setLoginUserAuthCodeId(ContextHolder.getLoginUser().getAuthTypeCodeId());
			
			String typeCodeId = taggingAuth.getTypeCodeId();
			String loginUserAuthCode = taggingAuth.getLoginUserAuthCodeId();
			
			taggingAuth.setIsCreatable(  typeCodeId.length() < 1 && (CodeUtils.isReviewer(loginUserAuthCode) || CodeUtils.isSystemAdmin(loginUserAuthCode)));
			taggingAuth.setIsRestorable( CodeUtils.isTaggingCreateType(typeCodeId) && (CodeUtils.isReviewer(loginUserAuthCode) || CodeUtils.isSystemAdmin(loginUserAuthCode)));
			
			return taggingAuth;
		}
		
		
	} 
}
