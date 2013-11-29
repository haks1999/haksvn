package com.haks.haksvn.transfer.model;

import org.springframework.stereotype.Component;

@Component
public class TraceSourceConnect {
	
	private String srcId;
	private String destId;
	private int transferSeq;
	private int transferGroupSeq;
	private int taggingSeq;
	
	public TraceSourceConnect(){
		
	}
	
	
	public String getSrcId() {
		return srcId;
	}


	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}


	public String getDestId() {
		return destId;
	}


	public void setDestId(String destId) {
		this.destId = destId;
	}


	public int getTransferSeq() {
		return transferSeq;
	}


	public void setTransferSeq(int transferSeq) {
		this.transferSeq = transferSeq;
	}


	public int getTransferGroupSeq() {
		return transferGroupSeq;
	}


	public void setTransferGroupSeq(int transferGroupSeq) {
		this.transferGroupSeq = transferGroupSeq;
	}


	public int getTaggingSeq() {
		return taggingSeq;
	}


	public void setTaggingSeq(int taggingSeq) {
		this.taggingSeq = taggingSeq;
	}


	public static class Builder{
		
		private TraceSourceConnect traceSourceConnect;
		
		private Builder(TraceSourceConnect traceSourceConnect){
			this.traceSourceConnect = traceSourceConnect;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TraceSourceConnect());
		}
		
		public static Builder getBuilder(TraceSourceConnect traceSourceConnect){
			return new Builder(traceSourceConnect);
		}
		
		public TraceSourceConnect build(){
			return traceSourceConnect;
		}
		
		public Builder srcId(String srcId){
			traceSourceConnect.setSrcId(srcId);
			return this;
		}
		
		public Builder destId(String destId){
			traceSourceConnect.setDestId(destId);
			return this;
		}
		
		public Builder transferSeq(int transferSeq){
			traceSourceConnect.setTransferSeq(transferSeq);
			return this;
		}
		
		public Builder transferGroupSeq(int transferGroupSeq){
			traceSourceConnect.setTransferGroupSeq(transferGroupSeq);
			return this;
		}
		
		public Builder taggingSeq(int taggingSeq){
			traceSourceConnect.setTaggingSeq(taggingSeq);
			return this;
		}
	} 
	
}
