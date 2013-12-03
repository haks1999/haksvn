package com.haks.haksvn.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.service.CodeService;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.general.service.GeneralService;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.source.model.SVNSourceTagging;
import com.haks.haksvn.transfer.dao.TaggingDao;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.model.TaggingAuth;
import com.haks.haksvn.transfer.util.TransferUtils;
import com.haks.haksvn.user.service.UserService;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TaggingService {

	@Autowired
	private TaggingDao taggingDao;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private SVNRepositoryService svnRepositoryService; 
	@Autowired
	private GeneralService generalService;
	
	public Paging<List<Tagging>> retrieveTaggingList(Paging<Tagging> paging){
		repositoryService.checkRepositoryAccessRight(paging.getModel().getRepositorySeq());
		return taggingDao.retrieveTaggingList(paging);
	}
	
	public Tagging retrieveTagging(Tagging tagging){
		repositoryService.checkRepositoryAccessRight(tagging.getRepositorySeq());
		return taggingDao.retrieveTaggingByTaggingSeq(tagging.getTaggingSeq());
	}
	
	public Tagging createTagging(Tagging tagging){
		Repository repository = repositoryService.checkRepositoryAccessRight(tagging.getRepositorySeq());
		if( !TaggingAuth.Builder.getBuilder().tagging(tagging).build().getIsCreatable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		
		Tagging.Builder.getBuilder(tagging).taggingUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()))
			.destPath(repository.getTagsPath() + "/" + tagging.getTagName())
			.srcPath(repository.getBranchesPath()).taggingDate(System.currentTimeMillis()).taggingTypeCode(codeService.retrieveCode(CodeUtils.getTaggingCreateCodeId()));
		tagging = taggingDao.addTagging(tagging);
		
		SVNSourceTagging svnSourceTagging = SVNSourceTagging.Builder.getBuilder()
				.srcPath(tagging.getSrcPath()).destPath(tagging.getDestPath())
				.log(TransferUtils.createTaggingCommitLog(tagging, generalService.retrieveCommitLogTemplate(tagging.getRepositorySeq(), CodeUtils.getLogTemplateTaggingCodeId()).getTemplate())).build();
		
		svnRepositoryService.tagging(repository, svnSourceTagging);
		return tagging;
	}
	
	public Tagging restoreTagging(Tagging tagging){
		Tagging srcTagging = retrieveTagging(tagging);
		Repository repository = repositoryService.checkRepositoryAccessRight(srcTagging.getRepositorySeq());
		if( !TaggingAuth.Builder.getBuilder().tagging(srcTagging).build().getIsRestorable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		Tagging destTagging = Tagging.Builder.getBuilder().taggingUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()))
			.taggingSeq(0).destPath(repository.getBranchesPath()).srcPath(srcTagging.getDestPath())
			.taggingDate(System.currentTimeMillis()).repositorySeq(tagging.getRepositorySeq())
			.tagName("[Restore]" + srcTagging.getTagName() + "(" + System.currentTimeMillis() + ")")
			.description("Restore From [" + srcTagging.getTagName() + "(tagging-" + srcTagging.getTaggingSeq() + ")] by Haksvn")
			.taggingTypeCode(codeService.retrieveCode(CodeUtils.getTaggingRestoreCodeId())).build();
		destTagging = taggingDao.addTagging(destTagging);
		
		SVNSourceTagging svnSourceTagging = SVNSourceTagging.Builder.getBuilder()
				.srcPath(destTagging.getSrcPath()).destPath(destTagging.getDestPath())
				.log(TransferUtils.createTaggingCommitLog(destTagging, generalService.retrieveCommitLogTemplate(srcTagging.getRepositorySeq(), CodeUtils.getLogTemplateTaggingCodeId()).getTemplate())).build();
		svnRepositoryService.tagging(repository, svnSourceTagging);
		return destTagging;
	}
	
	public List<Tagging> retrieveTaggingListByTagName(Tagging tagging){
		return taggingDao.retrieveTaggingListByTagName(tagging);
	}
	
	public Tagging retrieveLatestSyncTagging(Tagging tagging){
		return taggingDao.retrieveLatestSyncTagging(tagging);
	}
	
}
