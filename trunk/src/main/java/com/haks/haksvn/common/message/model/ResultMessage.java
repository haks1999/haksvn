package com.haks.haksvn.common.message.model;

public class ResultMessage extends DefaultMessage{

	private boolean success = true;
	
	public ResultMessage(){
		super();
	}
	
	public ResultMessage(String text){
		super(text);
	}
	
	
	@Override
	public String toString(){
		return "[ ResultMessage ]\n - type : " + getType() + "\n - text : " + getText() + "\n - success : " + isSuccess();
	}



	public boolean isSuccess() {
		return success;
	}



	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}
