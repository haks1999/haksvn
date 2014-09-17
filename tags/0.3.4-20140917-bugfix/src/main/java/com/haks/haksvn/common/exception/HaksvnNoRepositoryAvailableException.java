package com.haks.haksvn.common.exception;

@SuppressWarnings("serial")
public class HaksvnNoRepositoryAvailableException extends RuntimeException{

	public HaksvnNoRepositoryAvailableException(){
		super();
	}
	
	public HaksvnNoRepositoryAvailableException(String message) {
		super(message);
	}
	
	public HaksvnNoRepositoryAvailableException(Throwable cause){
		super(cause);
	}
	
	public HaksvnNoRepositoryAvailableException(String message, Throwable cause){
		super(message, cause);
	}
}
