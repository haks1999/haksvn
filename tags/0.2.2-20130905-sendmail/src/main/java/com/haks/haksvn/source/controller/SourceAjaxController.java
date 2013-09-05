package com.haks.haksvn.source.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.NextPaging;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.model.SVNSourceLog;
import com.haks.haksvn.source.service.SourceService;
import com.haks.haksvn.source.util.SourceUtils;

@Controller
@RequestMapping(value="/source")
public class SourceAjaxController {
         
	
	@Autowired
	private SourceService sourceService;
	@Autowired
	private RepositoryService repositoryService;
    
	@RequestMapping(value="/browse/_r_/list", method=RequestMethod.GET ,params ={"repositoryKey","path"})
    public @ResponseBody List<SVNSource> listSVNSource(@RequestParam(value = "repositoryKey", required = true) String repositoryKey
    												,@RequestParam(value = "path", required = true) String path){
    	
		return sourceService.retrieveSVNSourceList(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), path);
    }
	
	// request source 검색 시 자동 완성을 위한 dir 검색
	@RequestMapping(value="/browse/_r_/search/dir", method=RequestMethod.POST ,params ={"repositoryKey","path"})
    public @ResponseBody List<SVNSource> searchSVNSourceDir(@RequestParam(value = "repositoryKey", required = true) String repositoryKey
    												,@RequestParam(value = "path", required = true) String path){
    	try{
    		return sourceService.retrieveSVNSourceDirList(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), path);
    	}catch(HaksvnException e){
    		//TODO warn 처리?
    		// 입력 조건을 받아서 검색하므로 svn not found 오류가 많이 생길듯
    		return new ArrayList<SVNSource>(0);
    	}
    }
	
	@RequestMapping(value="/changes/_r_/list")
    public @ResponseBody NextPaging<List<SVNSourceLog>> listSVNSourceLog(@RequestParam(value = "repositoryKey", required = true) String repositoryKey,
    										@RequestParam(value = "path", required = true) String path,
    										@ModelAttribute("paging") NextPaging<SVNSource> paging){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).build();
		paging.setModel(svnSource);
		return sourceService.retrieveSVNSourceLogList(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), paging);
    }
	
	@RequestMapping(value="/changes/_r_/diff", headers="Accept=application/json")
    public @ResponseBody SVNSourceDiff diffWithPrevious(@RequestParam(value = "repositoryKey", required = true) String repositoryKey,
    										@RequestParam(value = "path", required = true) String path,
    										@RequestParam(value = "rev", required = true) long rev){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(rev).build();
		SVNSourceDiff svnSourceDiff = sourceService.retrieveDiffByPrevious(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSource);
		svnSourceDiff.setDiffToHtml(SourceUtils.diffToHtml(svnSourceDiff.getDiff()));
		svnSourceDiff.setDiff("");
		return svnSourceDiff;
    }
	
	@RequestMapping(value="/changes/_r_/search")
    public @ResponseBody SVNSource searchChangeInfo( @RequestParam(value = "repositoryKey", required = true) String repositoryKey,  
    							@RequestParam(value = "path", required = true) String path,
    							@RequestParam(value = "rev", required = true) long revision ) {
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(revision).build();
		svnSource = sourceService.retrieveSVNSourceWithoutContent(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSource);
        return svnSource;
    }
	
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	/*
    	e.printStackTrace();
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
        */
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
    
}
