package com.haks.haksvn.common.security.tag;

import javax.servlet.jsp.tagext.BodyTagSupport;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.security.util.ContextHolder;

@SuppressWarnings("serial")
public class AccessControlTag extends BodyTagSupport {

	private boolean reviewer = false;
	private boolean commiter = false;
	private boolean admin = false;
	
	@Override
	public int doAfterBody(){
		if( !(reviewer || commiter || admin) ) return EVAL_PAGE;
		try{
			String authTypeCodeId = ContextHolder.getLoginUser().getAuthType();
			if( (reviewer && CodeUtils.isReviewer(authTypeCodeId))
					|| (commiter && CodeUtils.isCommiter(authTypeCodeId)) 
					|| (admin && CodeUtils.isSystemAdmin(authTypeCodeId))){
				getPreviousOut().print(getBodyContent().getString());
				return EVAL_PAGE;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public boolean getReviewer(){
		return reviewer;
	}
	
	public void setReviewer(boolean reviewer){
		this.reviewer = reviewer;
	}
	
	public boolean getCommiter(){
		return commiter;
	}
	
	public void setCommiter(boolean commiter){
		this.commiter = commiter;
	}
	
	public boolean getAdmin(){
		return admin;
	}
	
	public void setAdmin(boolean admin){
		this.admin = admin;
	}

}
