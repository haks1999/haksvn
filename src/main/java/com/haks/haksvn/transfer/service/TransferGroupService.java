package com.haks.haksvn.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.common.exception.HaksvnException;
import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.repository.util.RepositoryUtils;
import com.haks.haksvn.transfer.dao.TransferGroupDao;
import com.haks.haksvn.transfer.model.Transfer;
import com.haks.haksvn.transfer.model.TransferGroup;
import com.haks.haksvn.transfer.model.TransferSource;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TransferGroupService {

	@Autowired
	private TransferGroupDao transferGroupDao;
	@Autowired
	private TransferService transferService;
	@Autowired
	private RepositoryService repositoryService;
	
	public Paging<List<TransferGroup>> retrieveTransferGroupList(Paging<TransferGroup> paging){
		repositoryService.checkRepositoryAccessRight(paging.getModel().getRepositorySeq());
		return transferGroupDao.retrieveTransferGroupList(paging);
	}
	
	/*
	private TransferGroup addTransferGroup(TransferGroup transferGroup){
		for( Transfer transfer : transferGroup.getTransferList() ){
			checkTransferableTransfer(transfer);
		}
		for( TransferSource transferSource: transfer.getSourceList() ){
			transferSource.setTransfer(transfer);
			transferSource = checkRequestableTransferSource(transferSource);
			if( transferSource.getIsLocked() ) throw new HaksvnException("[" + transferSource.getPath() + "] is locked by [" + transferSource.getTransfer().getTransferSeq() + "]");
			transferSource.setTransferSourceSeq(0);
		}
		Transfer.Builder.getBuilder(transfer).requestUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()))
			.transferStateCode(codeService.retrieveCode(CodeUtils.getTransferKeepCodeId()))
			.requestDate(0).approveUser(null).approveDate(0).transferSeq(0);
		return transferDao.addTransfer(transfer);
	}
	
	public Transfer checkTransferableTransfer(Transfer transfer){
		transfer = transferService.retrieveTransferDetail(transfer);
		if( !CodeUtils.isTransferablState(transfer.getTransferStateCode().getCodeId()) ){
			throw new HaksvnException("[req-" + transfer.getTransferSeq() + "] not in transferable state.");
		}
		return transfer;
	}
	*/
	
}
