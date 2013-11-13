package com.haks.haksvn.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.dao.TransferDao;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TraceSourceService {

	@Autowired
	private TransferDao transferDao;
	@Autowired
	private RepositoryService repositoryService;
	
	
	public List<String> retrieveTransferSourceListByPath(String repositoryKey, String path){
		repositoryService.checkRepositoryAccessRight(repositoryKey);
		return transferDao.retrieveTransferSourcePathListByPath(repositoryKey, path, 20);
	}
	
}
