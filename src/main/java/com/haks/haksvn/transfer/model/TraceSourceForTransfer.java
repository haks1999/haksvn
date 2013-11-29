package com.haks.haksvn.transfer.model;

import org.springframework.stereotype.Component;

@Component
public class TraceSourceForTransfer {

	private int transferSeq;
	private int transferGroupSeq;
	private int transferSourceSeq;
	private long transferDate;
	private long trunkRevision = -1;
	private long branchesRevision = -1;
	
	public TraceSourceForTransfer(){
		
	}

	public int getTransferSeq() {
		return transferSeq;
	}

	public void setTransferSeq(int transferSeq) {
		this.transferSeq = transferSeq;
	}
	
	public int getTransferGroupSeq(){
		return transferGroupSeq;
	}
	
	public void setTransferGroupSeq(int transferGroupSeq){
		this.transferGroupSeq = transferGroupSeq;
	}

	public int getTransferSourceSeq() {
		return transferSourceSeq;
	}

	public void setTransferSourceSeq(int transferSourceSeq) {
		this.transferSourceSeq = transferSourceSeq;
	}

	public long getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(long transferDate) {
		this.transferDate = transferDate;
	}

	public long getTrunkRevision() {
		return trunkRevision;
	}

	public void setTrunkRevision(long trunkRevision) {
		this.trunkRevision = trunkRevision;
	}

	public long getBranchesRevision() {
		return branchesRevision;
	}

	public void setBranchesRevision(long branchesRevision) {
		this.branchesRevision = branchesRevision;
	}
	
	public static class Builder{
		
		private TraceSourceForTransfer traceSource;
		
		private Builder(TraceSourceForTransfer traceSource){
			this.traceSource = traceSource;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TraceSourceForTransfer());
		}
		
		public static Builder getBuilder(TraceSourceForTransfer traceSource){
			return new Builder(traceSource);
		}
		
		public TraceSourceForTransfer build(){
			return traceSource;
		}
		
		public Builder transferSeq(int transferSeq){
			traceSource.setTransferSeq(transferSeq);
			return this;
		}
		
		public Builder tranferGroupSeq(int transferGroupSeq){
			traceSource.setTransferGroupSeq(transferGroupSeq);
			return this;
		}
		
		public Builder transferSourceSeq(int transferSourceSeq){
			traceSource.setTransferSourceSeq(transferSourceSeq);
			return this;
		}
		
		public Builder transferDate(long transferDate){
			traceSource.setTransferDate(transferDate);
			return this;
		}
		
		public Builder trunkRevision(long trunkRevision){
			traceSource.setTrunkRevision(trunkRevision);
			return this;
		}
		
		public Builder branchesRevision(long branchesRevision){
			traceSource.setBranchesRevision(branchesRevision);
			return this;
		}
		
	} 
	
}
