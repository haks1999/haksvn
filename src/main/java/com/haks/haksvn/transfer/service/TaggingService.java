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
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.dao.TaggingDao;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.model.TaggingAuth;
import com.haks.haksvn.transfer.model.Transfer;
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
	
	public Paging<List<Tagging>> retrieveTaggingList(Paging<Tagging> paging){
		checkRepositoryAccessRight(paging.getModel().getRepositorySeq());
		return taggingDao.retrieveTaggingList(paging);
	}
	
	public Tagging retrieveTaggingDetail(Tagging tagging){
		checkRepositoryAccessRight(tagging.getRepositorySeq());
		return taggingDao.retrieveTaggingByTaggingSeq(tagging.getTaggingSeq());
	}
	
	public Tagging createTagging(Tagging tagging){
		Repository repository = checkRepositoryAccessRight(tagging.getRepositorySeq());
		if( !TaggingAuth.Builder.getBuilder().tagging(tagging).build().getIsCreatable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		
		//TODO svn 연결 
		Tagging.Builder.getBuilder(tagging).taggingUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()))
			.destPath(repository.getTagsPath() + "/" + tagging.getTagName())
			.srcPath(repository.getBranchesPath()).taggingDate(System.currentTimeMillis()).taggingTypeCode(codeService.retrieveCode(CodeUtils.getTaggingCreateCodeId()));
		
		return taggingDao.addTagging(tagging);
	}
	
	@Transactional(readOnly=true)
	private Repository checkRepositoryAccessRight(int repositorySeq){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		if( repository == null || repositorySeq != repository.getRepositorySeq()){
			throw new HaksvnException("do not have the repository access right");
		}
		return repository;
	}
}
