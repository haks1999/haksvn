package com.haks.haksvn.source.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.service.SourceService;

@Controller
@RequestMapping(value="/source")
public class SourceController {
         

	@Autowired
    private RepositoryService repositoryService;
	@Autowired
    private SourceService sourceService;
    
	@RequestMapping(value="/browse", method=RequestMethod.GET)
    public ModelAndView forwardSourceBrowsePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/source/browse/" + repositoryList.get(0).getRepositorySeq(), true));
    	}else{
    		return new ModelAndView("/source/sourceBrowse");
    	}
    }
	
	// .jsp 등의 확장자는 여기서 처리하지 못하여 url rewrite로 . 을 %2E 로 변경해준다 
	// 그러기 위하여 url 중간에 /r/ 을 넣어 rewrite 완료 여부를 구분한다.
	// 여기서 %2E 를 다시 . 으로 변경
	// /resources/ 이거는 이유를 모르겠지만 다 스태틱으로 넘기는듯... 일단 이것도 url rewrite
	@RequestMapping(value={"/browse/r/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", "");
        return "/source/sourceBrowse";
    }
	
	@RequestMapping(value={"/browse/r/{repositorySeq}/**"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							HttpServletRequest request,
    							@PathVariable int repositorySeq) {
		String path = reverseUrlRewrite(request,"/source/browse/r", repositorySeq);
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", path);
        return "/source/sourceBrowse";
    }
	
	
	
	@RequestMapping(value={"/browse/r/{repositorySeq}/**"}, method=RequestMethod.GET,params ={"rev"})
    public String forwardSourceDetailPage( ModelMap model,
    							HttpServletRequest request,
    							@RequestParam(value = "rev", required = true) long revision,
    							@PathVariable int repositorySeq) {
		String path = reverseUrlRewrite(request,"/source/browse/r", repositorySeq);
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(revision).build();
		svnSource = sourceService.retrieveSVNSource(repositorySeq, svnSource);
		//svnSource.setContent(svnSource.getContent().replaceAll("<","&lt;"));	// for syntaxhighligher
		model.addAttribute("svnSource", svnSource);
		model.addAttribute("repositorySeq", repositorySeq );
        return "/source/sourceDetail";
    }
	
	@RequestMapping(value="/changes", method=RequestMethod.GET)
    public ModelAndView forwardSourceChangePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/source/changes/" + repositoryList.get(0).getRepositorySeq(), true));
    	}else{
    		return new ModelAndView("/source/listChange");
    	}
    }
	
	@RequestMapping(value={"/changes/r/{repositorySeq}"}, method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model,
    							@PathVariable int repositorySeq) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", "");
        return "/source/listChange";
    }
	
	@RequestMapping(value={"/changes/r/{repositorySeq}/**"}, method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model,
    							HttpServletRequest request,
    							@PathVariable int repositorySeq) {
		String path = reverseUrlRewrite(request,"/source/changes/r", repositorySeq);
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositorySeq", repositorySeq );
    	model.addAttribute("path", path);
        return "/source/listChange";
    }
	
	/*
	@RequestMapping(value="/changes/{repositorySeq}")
    public String forwardSourceChangePage(@PathVariable int repositorySeq,
    										@ModelAttribute("paging") Paging<SVNSource> paging){
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path("").build();
		paging.setModel(svnSource);
		return sourceService.retrieveSVNSourceLogList(repositorySeq, paging);
    }
 */
	
	private String reverseUrlRewrite(HttpServletRequest request, String mapping, int repositorySeq){
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		path = path.replaceFirst(mapping + "/" + String.valueOf(repositorySeq),"").replaceAll("%2E", ".").replaceAll("_resources_","resources");
		if(path.startsWith("/")) path=path.replaceFirst("/","");
		return path;
	}
	
 
}
