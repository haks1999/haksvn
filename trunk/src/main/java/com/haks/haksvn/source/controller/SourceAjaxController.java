package com.haks.haksvn.source.controller;

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

import com.haks.haksvn.common.paging.model.Paging;
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
    
	@RequestMapping(value="/browse/list", method=RequestMethod.GET ,params ={"repositorySeq","path"})
    public @ResponseBody List<SVNSource> listSVNSource(@RequestParam(value = "repositorySeq", required = true) int repositorySeq
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
	/*
	@RequestMapping(value="/browse/detail")
    public @ResponseBody SVNSource getSVNSource(@ModelAttribute("svnSource") SVNSource svnSource,
    										@RequestParam(value = "repositorySeq", required = true) String repositorySeq){
    	
		svnSource = sourceService.retrieveSVNSource(repositorySeq, svnSource);
		svnSource.setContent(svnSource.getContent().replaceAll("<","&lt;"));	// for syntaxhighligher
		return svnSource;
    }
    */
	
	@RequestMapping(value="/changes/list")
    public @ResponseBody Paging<List<SVNSourceLog>> listSVNSourceLog(@RequestParam(value = "repositorySeq", required = true) int repositorySeq,
    										@RequestParam(value = "path", required = true) String path,
    										@ModelAttribute("paging") Paging<SVNSource> paging){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).build();
		paging.setModel(svnSource);
		return sourceService.retrieveSVNSourceLogList(repositorySeq, paging);
    }
	
	@RequestMapping(value="/changes/diff", headers="Accept=application/json")
    public @ResponseBody SVNSourceDiff diffWithPrevious(@RequestParam(value = "repositorySeq", required = true) int repositorySeq,
    										@RequestParam(value = "path", required = true) String path,
    										@RequestParam(value = "rev", required = true) long rev){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(rev).build();
		SVNSourceDiff svnSourceDiff = sourceService.retrieveDiffByPrevious(repositorySeq, svnSource);
		svnSourceDiff.setDiffToHtml(SourceUtils.diffToHtml(svnSourceDiff.getDiff()));
		svnSourceDiff.setDiff("");
		return svnSourceDiff;
    }
	
	
	/*
	@ExceptionHandler(HaksvnException.class)
	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Exception occcurs...")
    public @ResponseBody ResultMessage haksvnExceptionHandler(Exception e) {
    	ResultMessage message = new ResultMessage(e.getMessage());
    	message.setSuccess(false);
    	message.setType(DefaultMessage.TYPE.ERROR);
        return message;
    }
    */
    
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
