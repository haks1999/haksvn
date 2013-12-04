package com.haks.haksvn.transfer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.code.util.CodeUtils;
import com.haks.haksvn.repository.service.RepositoryService;
import com.haks.haksvn.transfer.dao.TraceSourceDao;
import com.haks.haksvn.transfer.model.Tagging;
import com.haks.haksvn.transfer.model.TraceSourceBasket;
import com.haks.haksvn.transfer.model.TraceSourceConnect;
import com.haks.haksvn.transfer.model.TraceSourceElement;
import com.haks.haksvn.transfer.model.TraceSourceForTransfer;

@Service
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class TraceSourceService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaggingService taggingService;
	@Autowired
	private TraceSourceDao traceSourceDao;
	
	
	public List<String> retrieveTransferSourceListByPath(String repositoryKey, String path){
		repositoryService.checkRepositoryAccessRight(repositoryKey);
		return traceSourceDao.retrieveTransferSourcePathListByPath(repositoryKey, path, 20);
	}
	
	public TraceSourceBasket retrieveTraceSourceListByPath(String repositoryKey, String path, int limit){
		repositoryService.checkRepositoryAccessRight(repositoryKey);
		List<TraceSourceForTransfer> traceSourceForTransferList = traceSourceDao.retrieveTraceSourceListByPath(repositoryKey, path, limit );
		
		Map<String,TraceSourceElement> trunkElemMap = new HashMap<String,TraceSourceElement>(0);
		Map<String,TraceSourceElement> branchElemMap = new HashMap<String,TraceSourceElement>(0);
		Map<String,TraceSourceElement> tagElemMap = new HashMap<String,TraceSourceElement>(0);
		List<TraceSourceConnect> connectList = new ArrayList<TraceSourceConnect>(0);
		for( TraceSourceForTransfer traceSource : traceSourceForTransferList ){
			String trunkElemId = "trunk"+traceSource.getTrunkRevision();
			String branchElemId = "branch"+traceSource.getBranchesRevision();
			if( !trunkElemMap.containsKey(trunkElemId) ){
				trunkElemMap.put(trunkElemId, TraceSourceElement.Builder.getBuilder().id(trunkElemId).isTrunk(true).revision(traceSource.getTrunkRevision()).build());
			}
			if( !branchElemMap.containsKey(branchElemId)){
				branchElemMap.put(branchElemId, TraceSourceElement.Builder.getBuilder().id(branchElemId).isBranch(true).revision(traceSource.getBranchesRevision()).build());
			}
			connectList.add(TraceSourceConnect.Builder.getBuilder().destId(branchElemId)
					.srcId(trunkElemId).transferGroupSeq(traceSource.getTransferGroupSeq()).transferSeq(traceSource.getTransferSeq()).build());
		}
		
		if( traceSourceForTransferList != null && traceSourceForTransferList.size() > 0 ){
			List<Tagging> taggingList = taggingService.retrieveTaggingListByTaggingDate(repositoryKey, traceSourceForTransferList.get(traceSourceForTransferList.size()-1).getTransferDate(), System.currentTimeMillis());
			for( Tagging tagging : taggingList ){
				boolean isTagged = CodeUtils.isTaggingCreateType(tagging.getTaggingTypeCode().getCodeId());
				String branchElemId = "branch" + (isTagged?tagging.getSrcRevision():tagging.getDestRevision());
				String tagElemId = "tag"+ (isTagged?tagging.getDestRevision():tagging.getSrcRevision());
				
				if( !branchElemMap.containsKey(branchElemId) ){
					branchElemMap.put(branchElemId, TraceSourceElement.Builder.getBuilder().id(branchElemId).isBranch(true).revision((isTagged?tagging.getSrcRevision():tagging.getDestRevision())).build());
				}
				if( isTagged && !tagElemMap.containsKey(tagElemId)){	// restore와 create는 어차피 동일 tag 바라봄. create 타입만 해도 됨
					tagElemMap.put(tagElemId, TraceSourceElement.Builder.getBuilder().id(tagElemId).isTag(true).revision((isTagged?tagging.getDestRevision():tagging.getSrcRevision())).name(tagging.getTagName()).build());
				}
				connectList.add(TraceSourceConnect.Builder.getBuilder().destId(isTagged?tagElemId:branchElemId)
						.srcId(isTagged?branchElemId:tagElemId).taggingSeq(tagging.getTaggingSeq()).build());
			}
		}
		
		TraceSourceBasket basket = new TraceSourceBasket();
		basket.setTrunkElemList(new ArrayList<TraceSourceElement>(trunkElemMap.values()));
		basket.setBranchElemList(new ArrayList<TraceSourceElement>(branchElemMap.values()));
		basket.setTagElemList(new ArrayList<TraceSourceElement>(tagElemMap.values()));
		basket.setOutBoundConnectList(connectList);
		basket.sortElementsAndGenerateInBoundConnections();
		basket.markTraceSource();
		
		return basket;
	}
	
}
