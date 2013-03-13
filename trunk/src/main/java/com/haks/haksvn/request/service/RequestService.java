package com.haks.haksvn.request.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.request.dao.RequestDao;
import com.haks.haksvn.request.model.Transfer;

@Transactional
@Service
public class RequestService {
	
	@Autowired
	private RequestDao dao;

	public List<Transfer> retrieveRequestList(){
		return dao.retrieveRequestList();
	}
}
