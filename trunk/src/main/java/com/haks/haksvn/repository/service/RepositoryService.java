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
	
	public List<Repository> retrieveActiveRepositoryList(){
		List<Repository> repositoryList = repositoryDao.retrieveActiveRepositoryList();
		return repositoryList;
		
	}
	
	public Repository retrieveRepositoryByRepositorySeq(int repositorySeq){
		Repository repository = new Repository();
		repository.setRepositorySeq(repositorySeq);
		Repository result = repositoryDao.retrieveRepositoryByRepositorySeq(repository);
		return result;
		
	}
	
	public Repository saveRepository(Repository repository){
		if( repository.getRepositorySeq() < 1 ){
			return repositoryDao.addRepository(repository);
		}else{
			// get repository in hibernate session 
			// 그냥 신규 repo obj 로 업뎃하믄 다른 객체로 인식해서 cascade 가 엉망이 됨
			// manytoone 등과 같은 relation 이 설정된 경우 주의해야 함
			Repository repositoryInHibernate = repositoryDao.retrieveRepositoryByRepositorySeq(repository);
			
			// exclude userList
			Repository.Builder.getBuilder(repositoryInHibernate)
				.active(repository.getActive())
				.authUserId(repository.getAuthUserId()).authUserPasswd(repository.getAuthUserPasswd())
				.repositoryLocation(repository.getRepositoryLocation()).repositoryName(repository.getRepositoryName())
				.tagsPath(repository.getTagsPath()).trunkPath(repository.getTrunkPath());
			
			return repositoryDao.updateRepository(repositoryInHibernate);
		}
		
	}
}
