package com.haks.haksvn.common.exception;

@SuppressWarnings("serial")
public class HaksvnException extends RuntimeException{

	public HaksvnException(){
		super();
	}
	
	public HaksvnException(String message) {
		super(message);
	}
	
	public HaksvnException(Throwable cause){
		super(cause);
	}
	
	public HaksvnException(String message, Throwable cause){
		super(message, cause);
	}

}
