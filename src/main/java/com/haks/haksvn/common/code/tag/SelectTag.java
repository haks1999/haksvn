package com.haks.haksvn.common.code.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.code.service.CodeService;

public class SelectTag extends SimpleTagSupport {

	private String name;
	private String codeGroup;
	private String selectedValue;
	private boolean disabled;
	private String cssClass;
	private static CodeService codeService;
	
	private synchronized void injectCodeService(){
		// selecttag 자체를 spring form:select로 대체할 수 있을까
		
		// jsp 내 삽입되는 코드인가? spring bean 으로 정상 인식이 되었으나 spring에서 인식을 하지 못 하는건지
		// bean 으로도 등록이 되지 않는 건지 @component 선언 후 @autowired로 service 바인딩이 안 됨
		PageContext pageContext = (PageContext) getJspContext();  
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ServletContext servletContext =request.getSession().getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		codeService = (CodeService)wac.getBean("codeService");
	}
	
	@Override
	public void doTag() throws JspException ,IOException {
		
		if( codeService == null ) injectCodeService();
		
		List<Code> codeList = codeService.retrieveCodeListByCodeGroup(codeGroup);
		
		boolean selected = selectedValue != null && selectedValue.length() > 0;
		JspWriter out = getJspContext().getOut();
		StringBuffer sb = new StringBuffer("<select ")
			.append(" name=\"")
			.append(name)
			.append("\"");
		if(cssClass != null ){
			sb.append(" class=\"")
				.append(cssClass)
				.append("\"");
		}
		if(disabled){
			sb.append(" disabled");
		}
		
		sb.append( ">");
		for( Code code : codeList ){
			sb.append("<option value=\"")
				.append(code.getCodeId())
				.append("\"");
			if( selected && code.getCodeId().equals(selectedValue)){
				sb.append(" selected");
			}
			sb.append(">")
				.append(code.getCodeName())
				.append("</option>");
		}
		sb.append("</select>");
	    out.println(sb.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeGroup() {
		return codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	

}
