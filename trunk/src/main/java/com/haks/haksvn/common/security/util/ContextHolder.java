package com.haks.haksvn.common.security.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.haks.haksvn.common.security.model.LoginUser;

@Component
public class ContextHolder {
	
	private static final String SESSION_KEY_LOGIN_USER = "_session_key_login_user_";
	
	public static void addLoginUser(LoginUser loginUser){
		RequestContextHolder.currentRequestAttributes().setAttribute(SESSION_KEY_LOGIN_USER, loginUser, RequestAttributes.SCOPE_SESSION);
	}

	public static LoginUser getLoginUser(){
		try{
			return (LoginUser)RequestContextHolder.currentRequestAttributes().getAttribute(SESSION_KEY_LOGIN_USER, RequestAttributes.SCOPE_SESSION);
		}catch(IllegalStateException e){
			return null;
		}
	}
	
	public static void deleteLoginUser(){
		RequestContextHolder.currentRequestAttributes().removeAttribute(SESSION_KEY_LOGIN_USER, RequestAttributes.SCOPE_SESSION);
	}
	
	public static boolean isLoggedIn(){
		return getLoginUser() != null;
	}
}
