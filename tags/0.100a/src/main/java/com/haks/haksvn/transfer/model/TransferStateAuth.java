package com.haks.haksvn.transfer.model;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

public class TransferStateAuth {

	private boolean isEditable;
	private boolean isRequestable;	// == isDeletable
	private boolean isDeletable;		// == isRequestable
	private boolean isApprovable;		// == isRejectable
	private boolean isRejectable;		// == isApprovable
	private boolean isRequestCancelable;
	private boolean isComplete;
	
	private String stateCodeId;
	private String requestUserId;
	private String loginUserId;
	private String loginUserAuthCodeId;
	
	public TransferStateAuth(){
		
	}
	
	public void setIsEditable(boolean isEditable){
		this.isEditable = isEditable;
	}
	
	public boolean getIsEditable(){
		return isEditable;
	}
	
	public void setIsRequestable(boolean isRequestable){
		this.isRequestable = isRequestable;
	}
	
	public boolean getIsRequestable(){
		return isRequestable;
	}
	
	public void setIsDeletable(boolean isDeletable){
		this.isDeletable = isDeletable;
	}
	
	public boolean getIsDeletable(){
		return isDeletable;
	}
	
	public void setIsApprovable(boolean isApprovable){
		this.isApprovable = isApprovable;
	}
	
	public boolean getIsApprovable(){
		return isApprovable;
	}
	
	public void setIsRejectable(boolean isRejectable){
		this.isRejectable = isRejectable;
	}
	
	public boolean getIsRejectable(){
		return isRejectable;
	}
	
	public void setIsRequestCancelable(boolean isRequestCancelable){
		this.isRequestCancelable = isRequestCancelable;
	}
	
	public boolean getIsRequestCancelable(){
		return isRequestCancelable;
	}
	
	public void setIsComplete(boolean isComplete){
		this.isComplete = isComplete;
	}
	
	public boolean getIsComplete(){
		return isComplete;
	}
	
	
	public String getStateCodeId() {
		return stateCodeId;
	}

	public void setStateCodeId(String stateCodeId) {
		this.stateCodeId = stateCodeId;
	}

	public String getRequestUserId() {
		return requestUserId;
	}

	public void setRequestUserId(String requestUserId) {
		this.requestUserId = requestUserId;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getLoginUserAuthCodeId() {
		return loginUserAuthCodeId;
	}

	public void setLoginUserAuthCodeId(String loginUserAuthCodeId) {
		this.loginUserAuthCodeId = loginUserAuthCodeId;
	}



	public static class Builder{
		
		private TransferStateAuth transferStateAuth;
		
		private Builder(TransferStateAuth transferStateAuth){
			this.transferStateAuth = transferStateAuth;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TransferStateAuth());
		}
		
		public static Builder getBuilder(TransferStateAuth transferStateAuth){
			return new Builder(transferStateAuth);
		}
		
		public Builder transfer(Transfer transfer){
			transferStateAuth.setRequestUserId(transfer.getRequestUser()==null?"":transfer.getRequestUser().getUserId());
			transferStateAuth.setStateCodeId(transfer.getTransferStateCode()==null?"":transfer.getTransferStateCode().getCodeId());
			return this;
		}
		
		public TransferStateAuth build(){
			transferStateAuth.setLoginUserAuthCodeId(ContextHolder.getLoginUser().getAuthTypeCodeId());
			transferStateAuth.setLoginUserId(ContextHolder.getLoginUser().getUserId());
			
			String requestUserId = transferStateAuth.getRequestUserId();
			String stateCode = transferStateAuth.getStateCodeId();
			String loginUserId = transferStateAuth.getLoginUserId();
			String loginUserAuthCode = transferStateAuth.getLoginUserAuthCodeId();
			
			transferStateAuth.setIsEditable(CodeUtils.isEditableState(stateCode) && (requestUserId.equals(loginUserId)||requestUserId.length() < 1));
			transferStateAuth.setIsRequestable( CodeUtils.isRequestableState(stateCode) && requestUserId.equals(loginUserId));
			transferStateAuth.setIsDeletable(transferStateAuth.getIsRequestable());
			transferStateAuth.setIsApprovable(CodeUtils.isApprovableState(stateCode) && (CodeUtils.isReviewer(loginUserAuthCode) || CodeUtils.isSystemAdmin(loginUserAuthCode)));
			transferStateAuth.setIsRejectable(transferStateAuth.getIsApprovable());
			transferStateAuth.setIsRequestCancelable( CodeUtils.isRequestCancelableState(stateCode) && requestUserId.equals(loginUserId) );
			transferStateAuth.setIsComplete( CodeUtils.isCompleteState(stateCode));
			
			return transferStateAuth;
		}
		
		
	} 
}