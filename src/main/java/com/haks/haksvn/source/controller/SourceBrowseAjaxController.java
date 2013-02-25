package com.haks.haksvn.source.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.message.model.DefaultMessage;
import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.service.SourceService;

@Controller
@RequestMapping(value="/source/browse")
public class SourceBrowseAjaxController {
         
	
	@Autowired
	private SourceService sourceService;
    
	@RequestMapping(value="/list", method=RequestMethod.GET ,params ={"repositorySeq","path"})
    public @ResponseBody List<SVNSource> listSVNSource(@RequestParam(value = "repositorySeq", required = true) String repositorySeq
    												,@RequestParam(value = "path", required = true) String path){
    	
		return sourceService.retrieveSVNSourceList(repositorySeq, path);
    }
	/*
	@RequestMapping(value="/list", method=RequestMethod.GET ,params ={"repositorySeq","path"})
    public @ResponseBody Map<String,List<SVNSource>> listSVNSource(@RequestParam(value = "repositorySeq", required = true) String repositorySeq,
    												@RequestParam(value = "path", required = true) String path	){
    	
		List<SVNSource> svnSourceList = sourceService.retrieveSVNSourceList(repositorySeq, path);
		Map<String,List<SVNSource>> map = new HashMap<String,List<SVNSource>>();
		List<SVNSource> svnSourceFolderList = new ArrayList<SVNSource>(0);
		List<SVNSource> svnSourceFileList = new ArrayList<SVNSource>(0);
		for( SVNSource svnSource : svnSourceList ){
			if( svnSource.getIsFolder() ){
				svnSourceFolderList.add(svnSource);
			}else{
				svnSourceFileList.add(svnSource);
			}
		}
		map.put("folders", svnSourceFolderList);
		map.put("files", svnSourceFileList);
		return map;
    }
    */
	
	
	@RequestMapping(value="/detail")
    public @ResponseBody SVNSource getSVNSource(@ModelAttribute("svnSource") SVNSource svnSource,
    										@RequestParam(value = "repositorySeq", required = true) String repositorySeq){
    	
		svnSource = sourceService.retrieveSVNSource(repositorySeq, svnSource);
		svnSource.setContent(svnSource.getContent().replaceAll("<","&lt;"));	// for syntaxhighligher
		return svnSource;
    }
	
	
	@ExceptionHandler(HaksvnException.class)
    public @ResponseBody ResultMessage haksvnExceptionHandler(Exception e) {
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Exception occcurs...")
    public @ResponseBody ResultMessage exceptionHandler(Exception e) {
    	e.printStackTrace();
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
}
