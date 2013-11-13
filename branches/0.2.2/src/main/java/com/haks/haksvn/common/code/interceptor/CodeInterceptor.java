package com.haks.haksvn.common.code.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.haks.haksvn.common.code.model.CodeGroup;
import com.haks.haksvn.common.code.service.CodeService;

public class CodeInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private CodeService codeService;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception ex)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String path = request.getRequestURI();
		return (path.indexOf("/resources/") < 0 && (path.indexOf(".json") < 0)); 
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {

		if(mv == null || mv.getViewName() == null || "".equals(mv.getViewName())) return;
		
		List<CodeGroup> codeGroupList = codeService.retrieveCodeGroupList();
		for( CodeGroup codeGroup : codeGroupList ){
			mv.addObject( codeGroup.getCodeGroup() , codeGroup.getCodeList() );
		}
	}

}
