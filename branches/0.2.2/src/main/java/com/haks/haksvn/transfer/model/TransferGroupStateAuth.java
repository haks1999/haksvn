package com.haks.haksvn.transfer.model;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

public class TransferGroupStateAuth {

	private boolean isEditable;
	
	private String stateCodeId;
	private String loginUserAuthCodeId;
	
	public TransferGroupStateAuth(){
		
	}
	
	public void setIsEditable(boolean isEditable){
		this.isEditable = isEditable;
	}
	
	public boolean getIsEditable(){
		return isEditable;
	}
	
	public String getStateCodeId() {
		return stateCodeId;
	}

	public void setStateCodeId(String stateCodeId) {
		this.stateCodeId = stateCodeId;
	}

	public String getLoginUserAuthCodeId() {
		return loginUserAuthCodeId;
	}

	public void setLoginUserAuthCodeId(String loginUserAuthCodeId) {
		this.loginUserAuthCodeId = loginUserAuthCodeId;
	}



	public static class Builder{
		
		private TransferGroupStateAuth transferGroupStateAuth;
		
		private Builder(TransferGroupStateAuth transferGroupStateAuth){
			this.transferGroupStateAuth = transferGroupStateAuth;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TransferGroupStateAuth());
		}
		
		public static Builder getBuilder(TransferGroupStateAuth transferGroupStateAuth){
			return new Builder(transferGroupStateAuth);
		}
		
		public Builder transferGroup(TransferGroup transferGroup){
			transferGroupStateAuth.setStateCodeId(transferGroup.getTransferGroupStateCode()==null?"":transferGroup.getTransferGroupStateCode().getCodeId());
			return this;
		}
		
		public TransferGroupStateAuth build(){
			transferGroupStateAuth.setLoginUserAuthCodeId(ContextHolder.getLoginUser().getAuthTypeCodeId());
			
			String stateCode = transferGroupStateAuth.getStateCodeId();
			String loginUserAuthCode = transferGroupStateAuth.getLoginUserAuthCodeId();
			
			transferGroupStateAuth.setIsEditable(CodeUtils.isEditableTransferGroupState(stateCode) 
					&& (CodeUtils.isReviewer(loginUserAuthCode) || CodeUtils.isSystemAdmin(loginUserAuthCode)));
			return transferGroupStateAuth;
		}
		
		
	} 
}
