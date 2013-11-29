package com.haks.haksvn.transfer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class TraceSourceBasket {
	
	private List<TraceSourceElement> trunkElemList = new ArrayList<TraceSourceElement>(0);
	private List<TraceSourceElement> branchElemList = new ArrayList<TraceSourceElement>(0);
	private List<TraceSourceElement> tagElemList = new ArrayList<TraceSourceElement>(0);
	private List<TraceSourceConnect> connectOutBoundList = new ArrayList<TraceSourceConnect>(0);
	private List<TraceSourceConnect> connectInBoundList = new ArrayList<TraceSourceConnect>(0);
	
	public List<TraceSourceElement> getTrunkElemList() {
		return trunkElemList;
	}
	public void setTrunkElemList(List<TraceSourceElement> trunkElemList) {
		this.trunkElemList = trunkElemList;
	}
	
	public List<TraceSourceElement> getBranchElemList() {
		return branchElemList;
	}
	public void setBranchElemList(List<TraceSourceElement> branchElemList) {
		this.branchElemList = branchElemList;
	}
	public List<TraceSourceElement> getTagElemList() {
		return tagElemList;
	}
	public void setTagElemList(List<TraceSourceElement> tagElemList) {
		this.tagElemList = tagElemList;
	}
	public List<TraceSourceConnect> getOutBoundConnectList() {
		return connectOutBoundList;
	}
	public void setOutBoundConnectList(List<TraceSourceConnect> connectOutBoundList) {
		this.connectOutBoundList = connectOutBoundList;
	}
	
	public List<TraceSourceConnect> getInBoundConnectList() {
		return connectInBoundList;
	}
	public void setInBoundConnectList(List<TraceSourceConnect> connectInBoundList) {
		this.connectInBoundList = connectInBoundList;
	}
	
	public List<TraceSourceConnect> sortElementsAndGenerateInBoundConnections(){
		sortElements();
		connectInBoundList.addAll(generateTrunkInBoundConnectList(trunkElemList));
		connectInBoundList.addAll(generateBranchInBoundConnectList(branchElemList,connectOutBoundList ));
		return connectInBoundList;
	}
	
	private List<TraceSourceConnect> generateTrunkInBoundConnectList(List<TraceSourceElement> trunkElemList){
		List<TraceSourceConnect> trunkInBoundList = new ArrayList<TraceSourceConnect>(0);
		if( trunkElemList.size() < 2 ) return trunkInBoundList;
		String destTrunkElemId = trunkElemList.get(trunkElemList.size()-1).getId();
		for( int inx = trunkElemList.size()-2; inx > -1; inx --){
			String srcTrunkElemId = trunkElemList.get(inx).getId();
			trunkInBoundList.add(TraceSourceConnect.Builder.getBuilder().destId(destTrunkElemId).srcId(srcTrunkElemId).build());
			destTrunkElemId = srcTrunkElemId;
		}
		return trunkInBoundList;
	}
	
	private List<TraceSourceConnect> generateBranchInBoundConnectList(List<TraceSourceElement> branchElemList, List<TraceSourceConnect> connectOutBoundList){
		List<TraceSourceConnect> branchInBoundList = new ArrayList<TraceSourceConnect>(0);
		if( branchElemList.size() < 2 ) return branchInBoundList;
		Set<String> outBoundConnectBranchDestIdSet = new HashSet<String>(0);
		for( TraceSourceConnect conn: connectOutBoundList){
			// startswith branch 체크
			if( conn.getDestId().startsWith("b") ) outBoundConnectBranchDestIdSet.add(conn.getDestId());
		}
		String destBranchElemId = branchElemList.get(branchElemList.size()-1).getId();
		for( int inx = branchElemList.size()-2; inx > -1; inx --){
			String srcBranchElemId = branchElemList.get(inx).getId();
			if( !outBoundConnectBranchDestIdSet.contains(destBranchElemId)){
				branchInBoundList.add(TraceSourceConnect.Builder.getBuilder().destId(destBranchElemId).srcId(srcBranchElemId).build());
			}
			destBranchElemId = srcBranchElemId;
		}
		return branchInBoundList;
	}
	
	private void sortElements(){
		Collections.sort(trunkElemList, new Comparator<TraceSourceElement>(){
			 public int compare(TraceSourceElement ts1, TraceSourceElement ts2) {
				 return (int)ts1.getRevision() - (int)ts2.getRevision();
			 }
		});
		Collections.sort(branchElemList, new Comparator<TraceSourceElement>(){
			 public int compare(TraceSourceElement ts1, TraceSourceElement ts2) {
				 return (int)ts1.getRevision() - (int)ts2.getRevision();
			 }
		});
		Collections.sort(tagElemList, new Comparator<TraceSourceElement>(){
			 public int compare(TraceSourceElement ts1, TraceSourceElement ts2) {
				 return (int)ts1.getRevision() - (int)ts2.getRevision();
			 }
		});
	}
	
}
