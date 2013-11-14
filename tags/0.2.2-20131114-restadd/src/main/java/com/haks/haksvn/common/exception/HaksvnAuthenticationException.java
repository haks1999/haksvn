package com.haks.haksvn.common.exception;

@SuppressWarnings("serial")
public class HaksvnAuthenticationException extends Exception{

	public HaksvnAuthenticationException(){
		super();
	}
	
	public HaksvnAuthenticationException(String message) {
		super(message);
	}
	
	public HaksvnAuthenticationException(Throwable cause){
		super(cause);
	}
	
	public HaksvnAuthenticationException(String message, Throwable cause){
		super(message, cause);
	}
}
