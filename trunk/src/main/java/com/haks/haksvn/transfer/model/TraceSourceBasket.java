package com.haks.haksvn.transfer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
			trunkInBoundList.add(TraceSourceConnect.Builder.getBuilder().destId(destTrunkElemId).srcId(srcTrunkElemId).isFlow(false).build());
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
			branchInBoundList.add(TraceSourceConnect.Builder.getBuilder().destId(destBranchElemId).srcId(srcBranchElemId).isFlow(!outBoundConnectBranchDestIdSet.contains(destBranchElemId)).build());
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
	
	public void markTraceSource(){
		HashMap<String, List<TraceSourceConnect>> connMap = new HashMap<String, List<TraceSourceConnect>>(0);
		HashMap<String, TraceSourceElement> elemMap = new HashMap<String, TraceSourceElement>(0);
		for( TraceSourceConnect conn : connectOutBoundList ){
			if(!connMap.containsKey(conn.getDestId())){
				connMap.put(conn.getDestId(),new ArrayList<TraceSourceConnect>(0));
			}
			connMap.get(conn.getDestId()).add(conn);
		}
		for( TraceSourceConnect conn : connectInBoundList ){
			if(!conn.getIsFlow()) continue;
			if(!connMap.containsKey(conn.getDestId())){
				connMap.put(conn.getDestId(),new ArrayList<TraceSourceConnect>(0));
			}
			connMap.get(conn.getDestId()).add(conn);
		}
		for( TraceSourceElement elem : trunkElemList ){
			elemMap.put(elem.getId(), elem);
		}
		for( TraceSourceElement elem : branchElemList ){
			elemMap.put(elem.getId(), elem);
		}
		for( TraceSourceElement elem : tagElemList ){
			elemMap.put(elem.getId(), elem);
		}
		TraceSourceElement startElem = branchElemList.get(branchElemList.size()-1);
		// tag 는 최초 transfer 와 최후 transfer 사이 날짜 기준으로 가져오므로 최초 transfer 이전 tag 에서 
		// restore 되는 경우 out of range 가 발생한다. 이런 경우 대상 제외
		for( int inx = branchElemList.size()-1 ; inx > -1 ; inx--){
			if( connMap.get(startElem.getId()).size() == 1 && !elemMap.containsKey( connMap.get(startElem.getId()).get(0).getSrcId())){
				connMap.get(startElem.getId()).get(0).setIsInRange(false);
				startElem = branchElemList.get(inx);
			}else{
				break;
			}
		}
		
		markLastedTagElementAndConnect(startElem, connMap, tagElemList);
		markLatestElementsAndConnects(startElem, elemMap, connMap);
		markOutOfRangeConnects(elemMap, connectOutBoundList);
		
	}
	
	private void markLastedTagElementAndConnect(TraceSourceElement startElem, HashMap<String, List<TraceSourceConnect>> connMap, List<TraceSourceElement> tagElemList){
		if( tagElemList.size() < 1 ) return;
		for( int inx = tagElemList.size()-1 ; inx > -1 ; inx--){
			if( connMap.containsKey(tagElemList.get(inx).getId())){
				for( TraceSourceConnect conn : connMap.get(tagElemList.get(inx).getId())){
					if( conn.getSrcId().equals(startElem.getId()) ){
						conn.setIsLatest(true);
						tagElemList.get(inx).setIsLatest(true);
						break;
					}
				}
			}
		}
	}
	
	private void markOutOfRangeConnects(HashMap<String, TraceSourceElement> elemMap, List<TraceSourceConnect> connList){
		for( TraceSourceConnect conn : connList ){
			if( !elemMap.containsKey(conn.getDestId()) || !elemMap.containsKey(conn.getSrcId()) ){
				conn.setIsInRange(false);
			}
		}
	}
	
	private void markLatestElementsAndConnects(TraceSourceElement startElem, HashMap<String, TraceSourceElement> elemMap, HashMap<String, List<TraceSourceConnect>> connMap){
		startElem.setIsLatest(true);
		List<TraceSourceConnect> connList = connMap.containsKey(startElem.getId())?connMap.get(startElem.getId()):new ArrayList<TraceSourceConnect>(0);
		for( TraceSourceConnect conn : connList ){
			conn.setIsLatest(true);
			// tag 는 최초 transfer 와 최후 transfer 사이 날짜 기준으로 가져오므로 최초 transfer 이전 tag 에서 
			// restore 되는 경우 out of range 가 발생한다. 이런 경우 대상 제외
			if(elemMap.containsKey(conn.getSrcId())){
				markLatestElementsAndConnects( elemMap.get(conn.getSrcId()), elemMap, connMap);
			}else{
				conn.setIsInRange(false);
			}
		}
	}
	
}
