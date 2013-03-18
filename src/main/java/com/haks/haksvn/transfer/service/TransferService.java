package com.haks.haksvn.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.paging.model.Paging;
import com.haks.haksvn.transfer.dao.TransferDao;
import com.haks.haksvn.transfer.model.Transfer;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TransferService {

	@Autowired
	private TransferDao transferDao;
	
	public Paging<List<Transfer>> retrieveTransferList(Paging<Transfer> paging){
		return transferDao.retrieveTransferList(paging);
	}
	
}
