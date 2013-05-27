package com.haks.haksvn.transfer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.code.service.CodeService;
import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.general.service.GeneralService;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.service.SVNRepositoryService;
import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.source.model.SVNSourceTransfer;
import com.haks.haksvn.transfer.dao.TransferDao;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferSource;
import com.haks.haksvn.transfer.model.TransferStateAuth;
import com.haks.haksvn.transfer.util.TransferUtils;
import com.haks.haksvn.user.service.UserService;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TransferService {

	@Autowired
	private TransferDao transferDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private GeneralService generalService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private SVNRepositoryService svnRepositoryService;
	
	public Paging<List<Transfer>> retrieveTransferList(Paging<Transfer> paging){
		checkRepositoryAccessRight(paging.getModel().getRepositorySeq());
		return transferDao.retrieveTransferList(paging);
	}
	
	public TransferSource checkRequestableTransferSource(TransferSource transferSource){
		Repository repository = checkRepositoryAccessRight(transferSource.getTransfer().getRepositorySeq());
		
		TransferSource transferSourceLocked = transferDao.retrieveLockedTransferSource(transferSource.getPath(), repository.getRepositorySeq());
		if( transferSourceLocked != null ){
			transferSourceLocked.setIsLocked(true);
			return transferSourceLocked;
		}
		
		String transferSourceTypeCode = transferSource.getTransferSourceTypeCode().getCodeId();
		boolean toDelete = CodeUtils.isTransferSourceTypeDelete(transferSourceTypeCode);
		if(!toDelete){
			boolean isExistSource = svnRepositoryService.isExistingSource(repository, RepositoryUtils.getBranchesPath(repository,transferSource.getPath()), -1);
			transferSourceTypeCode = isExistSource?CodeUtils.getTransferSourceTypeModifyCodeId():CodeUtils.getTransferSourceTypeAddCodeId();
		}
		transferSource.setTransferSourceTypeCode(codeService.retrieveCode(transferSourceTypeCode));
		return transferSource;
	}
	
	public Transfer saveTransfer(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		Transfer currentTransfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		if( currentTransfer == null ){
			return addTransfer(transfer);
		}else{
			List<TransferSource> transferSourceToDeleteList = new ArrayList<TransferSource>(0);
			for( TransferSource transferSource : transfer.getSourceList()){
				if( transferSource.getTransferSourceSeq() < 1 ){
					transferSource.setTransfer(transfer);
					currentTransfer.getSourceList().add(transferSource);
					continue;
				}
				for( TransferSource curTransferSource : currentTransfer.getSourceList()){
					if( transferSource.getTransferSourceSeq() == curTransferSource.getTransferSourceSeq() ){
						if( transferSource.getDeleted() ){
							transferSourceToDeleteList.add(curTransferSource);
							curTransferSource.setTransfer(null);
						}else{
							curTransferSource.setRevision(transferSource.getRevision());
						}
					}
				}
			}
			for( TransferSource transferSource : transferSourceToDeleteList ){
				currentTransfer.getSourceList().remove(transferSource);
			}
			currentTransfer.setDescription(transfer.getDescription());
			currentTransfer.setTransferTypeCode(transfer.getTransferTypeCode());
			return updateTransfer(currentTransfer);
		}
	}
	
	private Transfer addTransfer(Transfer transfer){
		for( TransferSource transferSource: transfer.getSourceList() ){
			transferSource.setTransfer(transfer);
			transferSource = checkRequestableTransferSource(transferSource);
			if( transferSource.getIsLocked() ) throw new HaksvnException("[" + transferSource.getPath() + "] is locked by [" + transferSource.getTransfer().getTransferSeq() + "]");
			transferSource.setTransferSourceSeq(0);
		}
		Transfer.Builder.getBuilder(transfer).requestUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()))
			.transferStateCode(codeService.retrieveCode(CodeUtils.getTransferKeepCodeId()))
			.requestDate(0).transferUser(null).transferDate(0).transferSeq(0);
		return transferDao.addTransfer(transfer);
	}
	
	private Transfer updateTransfer(Transfer transfer){
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsEditable()){
			throw new HaksvnException("Insufficient privileges.");
		}
		for( TransferSource transferSource: transfer.getSourceList() ){
			if( transferSource.getTransferSourceSeq() < 1 ){	// 추가인 경우 
				// 신규 Transfer 가 아닌 연관된 Transfer를 세팅하면 다른 Service method 호출 시 insert, update 가 일어난다.
				// 이유를 알 수 없음. 단순 select method 임에도 불구하고...
				TransferSource transferSourceLocked = transferDao.retrieveLockedTransferSource(transferSource.getPath(), transfer.getRepositorySeq());
				if( transferSourceLocked != null && transferSourceLocked.getTransfer().getTransferSeq() != transfer.getTransferSeq()){
					throw new HaksvnException("[" + transferSource.getPath() + "] is locked by [" + transferSource.getTransfer().getTransferSeq() + "]");
				}
			}
			if(!transferSource.getDeleted()) transferSource.setTransfer(transfer);
		}
		transfer = transferDao.updateTransfer(transfer);
		return transfer;
	}
	
	public Transfer retrieveTransferDetail(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		return transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
	}
	
	public List<TransferSource> retrieveTransferSourceList(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		return transferDao.retrieveTransferSourceList(transfer.getTransferSeq());
	}
	
	public Transfer deleteTransfer(Transfer transfer){
		transfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsDeletable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		return transferDao.deleteTransfer(transfer);
	}
	
	public Transfer requestTransfer(Transfer transfer){
		transfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsRequestable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		transfer.setTransferStateCode(Code.Builder.getBuilder().codeId(CodeUtils.getTransferRequestCodeId()).build());
		transfer.setRequestDate(System.currentTimeMillis());
		return transferDao.updateTransfer(transfer);
	}
	
	public Transfer requestCancelTransfer(Transfer transfer){
		transfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsRequestCancelable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		transfer.setTransferStateCode(Code.Builder.getBuilder().codeId(CodeUtils.getTransferKeepCodeId()).build());
		transfer.setRequestDate(0);
		return transferDao.updateTransfer(transfer);
	}
	
	public Transfer completeTransfer(Transfer transfer){
		transfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		Repository repository = checkRepositoryAccessRight(transfer.getRepositorySeq());
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsApprovable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		transfer.setTransferStateCode(Code.Builder.getBuilder().codeId(CodeUtils.getTransferCompleteCodeId()).build());
		transfer.setTransferDate(System.currentTimeMillis());
		transfer.setTransferUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()));
		transfer = transferDao.updateTransfer(transfer);
		List<SVNSourceTransfer> svnSourceTransferList = new ArrayList<SVNSourceTransfer>(0);
		for( TransferSource transferSource : transfer.getSourceList() ){
			boolean isToDeleted = CodeUtils.isTransferSourceTypeDelete(transferSource.getTransferSourceTypeCode().getCodeId());
			String relPath = transferSource.getPath().replaceFirst(isToDeleted?repository.getBranchesPath():repository.getTrunkPath(), "");
			svnSourceTransferList.add(SVNSourceTransfer.Builder.getBuilder(new SVNSourceTransfer()).revision(transferSource.getRevision())
					.isToDelete(isToDeleted).relativePath(relPath).build());
		}
		svnRepositoryService.transfer(repository, svnSourceTransferList, TransferUtils.createTransferCommitLog(transfer, generalService.retrieveCommitLogTemplate(transfer.getRepositorySeq(), CodeUtils.getLogTemplateRequestCodeId()).getTemplate()));
		//svnRepositoryService.transfer(repository, svnSourceTransferList, transfer.getDescription());
		return transfer;
	}
	
	public Transfer rejectTransfer(Transfer transfer){
		transfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		if( !TransferStateAuth.Builder.getBuilder().transfer(transfer).build().getIsRejectable() ){
			throw new HaksvnException("Insufficient privileges.");
		}
		transfer.setTransferStateCode(Code.Builder.getBuilder().codeId(CodeUtils.getTransferRejectCodeId()).build());
		transfer.setTransferDate(System.currentTimeMillis());
		transfer.setTransferUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()));
		return transferDao.updateTransfer(transfer);
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
