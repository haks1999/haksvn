package com.haks.haksvn.common.system.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SystemPropertyTag extends SimpleTagSupport {

	private String key;
	
	
	@Override
	public void doTag() throws JspException ,IOException {
		
		JspWriter out = getJspContext().getOut();
		StringBuffer sb = new StringBuffer( System.getProperty(key));
	    out.println(sb.toString());
	}

	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	

}
