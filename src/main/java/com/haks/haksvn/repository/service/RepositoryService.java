package com.haks.haksvn.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haks.haksvn.repository.dao.RepositoryDao;
import com.haks.haksvn.repository.model.Repository;

@Service
public class RepositoryService {

	
	@Autowired
	private RepositoryDao repositoryDao;
	
	
	public List<Repository> retrieveMenuList(){
		
		List<Repository> repositoryList = repositoryDao.retrieveRepositoryList();
		
		return repositoryList;
		
	}
}
