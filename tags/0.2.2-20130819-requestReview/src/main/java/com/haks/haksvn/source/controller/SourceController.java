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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.source.model.ReviewAuth;
import com.haks.haksvn.source.model.SVNSource;
import com.haks.haksvn.source.model.SVNSourceDiff;
import com.haks.haksvn.source.service.ReviewService;
import com.haks.haksvn.source.service.SourceService;
import com.haks.haksvn.source.util.SourceUrlRewriteUtils;
import com.haks.haksvn.source.util.SourceUtils;

@Controller
@RequestMapping(value="/source")
public class SourceController {
         

	@Autowired
    private RepositoryService repositoryService;
	@Autowired
    private SourceService sourceService;
	@Autowired
	private ReviewService reviewService;
    
	@RequestMapping(value="/browse", method=RequestMethod.GET)
    public ModelAndView forwardSourceBrowsePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/source/browse/" + repositoryList.get(0).getRepositoryKey(), true));
    	}else{
    		return new ModelAndView("/source/sourceBrowse");
    	}
    }
	
	// .jsp 등의 확장자는 여기서 처리하지 못하여 url rewrite로 . 을 %2E 로 변경해준다 
	// 그러기 위하여 url 중간에 /_r_/ 을 넣어 rewrite 완료 여부를 구분한다.
	// 여기서 %2E 를 다시 . 으로 변경
	// /resources/ 이거는 이유를 모르겠지만 다 스태틱으로 넘기는듯... 일단 이것도 url rewrite
	// WEB-INF/configuration/urlrewrite/urlrewrite.xml
	@RequestMapping(value={"/browse/_r_/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							@PathVariable String repositoryKey) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	model.addAttribute("path", "");
        return "/source/sourceBrowse";
    }
	
	@RequestMapping(value={"/browse/_r_/{repositoryKey}/**"}, method=RequestMethod.GET)
    public String forwardSourceBrowsePage( ModelMap model,
    							HttpServletRequest request,
    							@PathVariable String repositoryKey) {
		String path = SourceUrlRewriteUtils.reverseUrlRewrite(request,"/source/browse/_r_", repositoryKey);
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	model.addAttribute("path", path);
        return "/source/sourceBrowse";
    }
	
	
	
	@RequestMapping(value={"/browse/_r_/{repositoryKey}/**"}, method=RequestMethod.GET,params ={"rev"})
    public String forwardSourceDetailPage( ModelMap model,
    							HttpServletRequest request,
    							@RequestParam(value = "rev", required = true) long revision,
    							@PathVariable String repositoryKey) {
		String path = SourceUrlRewriteUtils.reverseUrlRewrite(request,"/source/browse/_r_", repositoryKey);
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(revision).build();
		svnSource = sourceService.retrieveSVNSource(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSource);
		//svnSource.setContent(svnSource.getContent().replaceAll("<","&lt;"));	// for syntaxhighligher
		model.addAttribute("svnSource", svnSource);
		model.addAttribute("repositoryKey", repositoryKey );
        return "/source/sourceDetail";
    }
	
	@RequestMapping(value="/changes", method=RequestMethod.GET)
    public ModelAndView forwardSourceChangePage( ModelMap model ) {
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	if( repositoryList.size() > 0 ){
    		return new ModelAndView(new RedirectView("/source/changes/" + repositoryList.get(0).getRepositoryKey(), true));
    	}else{
    		return new ModelAndView("/source/listChange");
    	}
    }
	
	@RequestMapping(value={"/changes/_r_/{repositoryKey}"}, method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model,
    							@PathVariable String repositoryKey) {
		
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path("/").revision(-1).isFolder(true).build();
		model.addAttribute("svnSource", svnSource );
		
        List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	model.addAttribute("path", "");
    	
    	Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey);
    	List<SVNSource> svnSourceList = sourceService.retrieveSVNSourceList(repository, "");
    	model.addAttribute("lowerSvnSourceList", svnSourceList);
        return "/source/listChange";
    }
	
	@RequestMapping(value={"/changes/_r_/{repositoryKey}/**"}, method=RequestMethod.GET)
    public String forwardSourceChangePage( ModelMap model,
    							HttpServletRequest request,
    							@PathVariable String repositoryKey) {
		String path = SourceUrlRewriteUtils.reverseUrlRewrite(request,"/source/changes/_r_", repositoryKey);
        Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey);
        SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(-1).build();
        svnSource = sourceService.retrieveSVNSourceWithoutContentAndLogs(repository, svnSource);
		model.addAttribute("svnSource", svnSource);
		
		List<Repository> repositoryList = repositoryService.retrieveAccesibleActiveRepositoryList();
    	model.addAttribute("repositoryList", repositoryList );
    	model.addAttribute("repositoryKey", repositoryKey );
    	model.addAttribute("path", svnSource.getPath());
    	
    	if( svnSource.getIsFolder() ){
    		List<SVNSource> svnSourceList = sourceService.retrieveSVNSourceList(repository, svnSource.getPath());
        	model.addAttribute("lowerSvnSourceList", svnSourceList);
    	}
        return "/source/listChange";
    }
	
	@RequestMapping(value={"/changes/_r_/{repositoryKey}"}, method=RequestMethod.GET,params ={"rev"})
    public String forwardChangeDetailPage( ModelMap model,
    							@RequestParam(value = "rev", required = true) long revision,
    							@PathVariable String repositoryKey) {
		String path = "";
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(revision).build();
		svnSource = sourceService.retrieveSVNSourceWithoutContent(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSource);
		model.addAttribute("svnSource", svnSource);
		model.addAttribute("repositoryKey", repositoryKey );
		model.addAttribute("path", svnSource.getPath());
		model.addAttribute("review", reviewService.retrieveYourReview(repositoryKey, revision));
		model.addAttribute("reviewAuth", ReviewAuth.Builder.getBuilder().build());
		model.addAttribute("reviewSummary", reviewService.retrieveReviewSummary(repositoryKey, revision));
        return "/source/changeDetail";
    }
	
	@RequestMapping(value={"/changes/_r_/{repositoryKey}/**"}, method=RequestMethod.GET,params ={"rev"})
    public String forwardChangeDetailPage( ModelMap model,
    							HttpServletRequest request,
    							@RequestParam(value = "rev", required = true) long revision,
    							@PathVariable String repositoryKey) {
		String path = SourceUrlRewriteUtils.reverseUrlRewrite(request,"/source/changes/_r_", repositoryKey);
		SVNSource svnSource = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(revision).build();
		svnSource = sourceService.retrieveSVNSourceWithoutContent(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSource);
		model.addAttribute("svnSource", svnSource);
		model.addAttribute("repositoryKey", repositoryKey );
		model.addAttribute("path", svnSource.getPath());
        return "/source/changeDetail";
    }
	
	@RequestMapping(value="/changes/_r_/diff")
    public String forwardDiffDetailPage(ModelMap model,
    										@RequestParam(value = "repositoryKey", required = true) String repositoryKey,
    										@RequestParam(value = "srcPath", required = false, defaultValue="") String srcPath,
    										@RequestParam(value = "path", required = true) String path,
    										@RequestParam(value = "srcRev", required = true) long srcRev,
    										@RequestParam(value = "trgRev", required = true) long trgRev){
		boolean diffWithPrevious = srcPath.length() < 1 && srcRev < 0;
		srcPath = srcPath.length() > 0 ? srcPath:path;
		SVNSource svnSourceSrc = SVNSource.Builder.getBuilder(new SVNSource()).path(path).revision(srcRev).build();
		SVNSource svnSourceTrg = SVNSource.Builder.getBuilder(new SVNSource()).path(srcPath).revision(trgRev).build();
		SVNSourceDiff svnSourceDiff = new SVNSourceDiff();
		if( diffWithPrevious ){
			svnSourceDiff = sourceService.retrieveDiffWithContentsByPrevious(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSourceTrg);
		}else{
			svnSourceDiff = sourceService.retrieveDiffWithContentsByRevisions(repositoryService.retrieveAccesibleActiveRepositoryByRepositoryKey(repositoryKey), svnSourceSrc, svnSourceTrg);
		}
		if( svnSourceDiff.getIsNewContent() || svnSourceDiff.getIsDeletedContent() ){
			svnSourceDiff.setDiffToHtml(SourceUtils.diffToHtml(svnSourceDiff.getDiff()));
		}else{
			svnSourceDiff.setDiffToHtml(SourceUtils.diffToSideBySideHtml(svnSourceDiff.getDiff(), svnSourceDiff.getSrc().getContent(), svnSourceDiff.getTrg().getContent()));
		}
		
		svnSourceDiff.setDiff("");
		model.addAttribute("svnSourceDiff", svnSourceDiff);
		model.addAttribute("svnSourceSrc", svnSourceDiff.getSrc() );
		model.addAttribute("svnSourceTrg", svnSourceDiff.getTrg());
		model.addAttribute("repositoryKey", repositoryKey);
		
		return "/source/diffDetail";
    }
	
}
