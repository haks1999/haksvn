package com.haks.haksvn.transfer.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.transfer.service.TraceSourceService;

@Controller
@RequestMapping(value="/transfer")
public class TraceSourceAjaxController {
	
	@Autowired
	private TraceSourceService traceSourceService;
    
	
	
	// transfer source 검색 시 자동 완성을 위한 path 검색
	@RequestMapping(value="/traceSource/list/search/path", method=RequestMethod.POST ,params ={"repositoryKey","path"})
    public @ResponseBody List<String> searchTransferSourcePathList(@RequestParam(value = "repositoryKey", required = true) String repositoryKey
    												,@RequestParam(value = "path", required = true) String path){
    	List<String> transferSourcePathList = traceSourceService.retrieveTransferSourceListByPath(repositoryKey, path);
    	return transferSourcePathList;
    }
	
	
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
    
}
