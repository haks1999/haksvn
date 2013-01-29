package com.haks.haksvn.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.repository.dao.RepositoryDao;
import com.haks.haksvn.repository.model.Repository;

@Service
@Transactional
public class RepositoryService {

	
	@Autowired
	private RepositoryDao repositoryDao;
	
	
	
	public List<Repository> retrieveRepositoryList(){
		
		List<Repository> repositoryList = repositoryDao.retrieveRepositoryList();
		
		return repositoryList;
		
	}
	
	public Repository saveRepository(Repository repository){
		return repositoryDao.saveRepository(repository);
	}
}
