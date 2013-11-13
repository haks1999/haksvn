package com.haks.haksvn.common.message.model;

public class DefaultMessage {

	public class TYPE{
		public static final String INFO = "info";
		public static final String ERROR = "error";
		public static final String SUCCESS = "success";
	}
	
	
	private String type = TYPE.SUCCESS;
	private String text = "";
	
	public DefaultMessage(){
		
	}
	
	public DefaultMessage(String text){
		this.text = text;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString(){
		return "[ DefaultMessage ]\n - type : " + getType() + "\n - text : " + getText();
	}
	
}
