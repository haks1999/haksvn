package com.haks.haksvn.common.menu.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.menu.model.Menu;
import com.haks.haksvn.common.menu.service.MenuService;
import com.haks.haksvn.common.security.util.ContextHolder;

@SuppressWarnings("rawtypes")
@Component
public class MenuAuthorityInterceptor extends HandlerInterceptorAdapter implements ApplicationListener{

	@Autowired
	private MenuService menuService;
	
	private static final ConcurrentHashMap<String, Set<String>> MENU_URL_AUTH_MAPPING = new ConcurrentHashMap<String, Set<String>>();

	@Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
        	List<Menu> menuList = menuService.retrieveMenuList();
        	for( Menu menu : menuList ){
        		if( menu.getMenuLevel() > 1 ){
        			Set<String> authSet = new HashSet<String>(3);
        			for( Code code : menu.getMenuAuthorityList()){
        				authSet.add(code.getCodeId());
        			}
        			MENU_URL_AUTH_MAPPING.put(menu.getMenuUrl(), authSet);
        		}
        	}
        }
    }
	
	private boolean hasAuth(String url, String authCode ){
		if( !MENU_URL_AUTH_MAPPING.containsKey(url)) addAuthMapping(url);
		if( !MENU_URL_AUTH_MAPPING.containsKey(url)) throw new HaksvnException("do not have permission to access this url - " + url);
		if( !MENU_URL_AUTH_MAPPING.get(url).contains(authCode)) throw new HaksvnException("do not have permission to access this url - " + url);
		return true;
	}
	
	private void addAuthMapping(String url){
		String authUrl = "";
		Set<String> authSet = null;
		for( Map.Entry<String, Set<String>> authMapping: MENU_URL_AUTH_MAPPING.entrySet() ){
			if( url.startsWith(authMapping.getKey()) && authUrl.length() < url.length() ){
				authUrl = url;
				authSet = authMapping.getValue();
			}
		}
		if( authSet != null ) MENU_URL_AUTH_MAPPING.put(authUrl, authSet);
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// login check is processed by AuthenticationInterceptor
		if( !ContextHolder.isLoggedIn() ) return true;
		String url = request.getRequestURI().substring(request.getContextPath().length());
		return hasAuth(url, ContextHolder.getLoginUser().getAuthType());
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
							HttpServletResponse response, Object handler, ModelAndView mv)
											throws Exception {
		
	}
	
	class AuthUrlMap{
		String url;
		boolean systemAdmin;
		boolean reviwer;
		boolean commiter;
	}

}
