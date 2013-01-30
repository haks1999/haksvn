package com.haks.haksvn.common.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.dao.CodeDao;
import com.haks.haksvn.common.code.model.Code;

@Service
@Transactional
public class CodeService {

	
	@Autowired
	private CodeDao codeDao;
	
	public List<Code> retrieveCodeListByCodeGroup(String codeGroup){
		Code code = new Code();
		code.setCodeGroup(codeGroup);
		List<Code> result = codeDao.retrieveCodeListByCodeGroup(code);
		return result;
		
	}
}
