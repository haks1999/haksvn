package com.haks.haksvn.transfer.service;

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
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.dao.TransferDao;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferSource;
import com.haks.haksvn.transfer.model.TransferStateAuth;
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
	private RepositoryService repositoryService;
	
	public Paging<List<Transfer>> retrieveTransferList(Paging<Transfer> paging){
		checkRepositoryAccessRight(paging.getModel().getRepositorySeq());
		return transferDao.retrieveTransferList(paging);
	}
	
	public Transfer retrieveLockedTransferBySource(String path){
		return transferDao.retrieveLockedTransferBySourcePath(path);
	}
	
	public Transfer saveTransfer(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		Transfer currentTransfer = transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
		if( currentTransfer == null ){
			return addTransfer(transfer);
		}else{
			Transfer.Builder.getBuilder(currentTransfer).description(transfer.getDescription());
			return updateTransfer(currentTransfer);
		}
	}
	
	private Transfer addTransfer(Transfer transfer){
		for( TransferSource transferSource: transfer.getSourceList() ){
			transferSource.setTransferSourceSeq(0);
			transferSource.setTransfer(transfer);
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
		return transferDao.updateTransfer(transfer);
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
	
	private boolean checkRepositoryAccessRight(int repositorySeq){
		Repository repository = repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq);
		if( repository == null || repositorySeq != repository.getRepositorySeq()){
			throw new HaksvnException("do not have the repository access right");
		}
		return true;
	}
}
