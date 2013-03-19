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
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.dao.TransferDao;
import com.haks.haksvn.transfer.model.Transfer;
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
		return transferDao.retrieveTransferList(paging);
	}
	
	public Transfer addTransfer(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		//transfer.setRequestDate(System.currentTimeMillis());
		transfer.setRequestUser(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId()));
		transfer.setTransferStateCode(codeService.retrieveCode(CodeUtils.getTransferKeepCodeId()));
		transfer.setTransferUser(null);
		return transferDao.addTransfer(transfer);
	}
	
	
	private boolean checkRepositoryAccessRight(int repositorySeq){
		if(repositoryService.retrieveAccesibleActiveRepositoryByRepositorySeq(repositorySeq) == null ){
			throw new HaksvnException("do not have the repository access right");
		}
		return true;
	}
	
	public Transfer retrieveTransferDetail(Transfer transfer){
		checkRepositoryAccessRight(transfer.getRepositorySeq());
		return transferDao.retrieveTransferByTransferSeq(transfer.getTransferSeq());
	}
}
