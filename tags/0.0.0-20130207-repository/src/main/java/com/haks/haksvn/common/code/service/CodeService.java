package com.haks.haksvn.common.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.dao.CodeDao;
import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.common.code.model.CodeGroup;

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
	
	public List<CodeGroup> retrieveCodeGroupList(){
		List<Code> codeList = codeDao.retrieveCodeList();
		Map<String,List<Code>> codeGroupMap = new HashMap<String,List<Code>>();
		for( Code code : codeList ){
			String codeGroup = code.getCodeGroup();
			if(!codeGroupMap.containsKey(codeGroup)) codeGroupMap.put(codeGroup, new ArrayList<Code>());
			codeGroupMap.get(codeGroup).add(code);
		}
		
		List<CodeGroup> codeGroupList = new ArrayList<CodeGroup>();
		for( String codeGroupMapKey : codeGroupMap.keySet()) {
			CodeGroup codeGroup = new CodeGroup();
			codeGroup.setCodeGroup(codeGroupMapKey);
			codeGroup.setCodeList(codeGroupMap.get(codeGroupMapKey));
			codeGroupList.add(codeGroup);
		}
		
		return codeGroupList;
		
	}
}
