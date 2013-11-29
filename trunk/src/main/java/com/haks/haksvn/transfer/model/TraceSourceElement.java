package com.haks.haksvn.transfer.model;

import org.springframework.stereotype.Component;

@Component
public class TraceSourceElement {
	
	private String id;
	private long revision = -1;
	private String name = "";
	private boolean isTrunk = false;
	private boolean isBranch = false;
	private boolean isTag = false;
	
	public TraceSourceElement(){
		
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public long getRevision(){
		return revision;
	}
	
	public void setRevision(long revision){
		this.revision = revision;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean getIsTrunk(){
		return isTrunk;
	}
	
	public void setIsTrunk(boolean isTrunk){
		this.isTrunk = isTrunk;
	}
	
	public boolean isBranch(){
		return isBranch;
	}
	
	public void setIsBranch(boolean isBranch){
		this.isBranch = isBranch;
	}
	
	public boolean isTag(){
		return isTag;
	}
	
	public void setIsTag(boolean isTag){
		this.isTag = isTag;
	}
	
	public static class Builder{
		
		private TraceSourceElement traceSourceElement;
		
		private Builder(TraceSourceElement traceSourceElement){
			this.traceSourceElement = traceSourceElement;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new TraceSourceElement());
		}
		
		public static Builder getBuilder(TraceSourceElement traceSourceElement){
			return new Builder(traceSourceElement);
		}
		
		public TraceSourceElement build(){
			return traceSourceElement;
		}
		
		public Builder id(String id){
			traceSourceElement.setId(id);
			return this;
		}
		
		public Builder revision(long revision){
			traceSourceElement.setRevision(revision);
			return this;
		}
		
		public Builder name(String name){
			traceSourceElement.setName(name);
			return this;
		}
		
		public Builder isTrunk(boolean isTrunk){
			traceSourceElement.setIsTrunk(isTrunk);
			return this;
		}
		
		public Builder isBranch(boolean isBranch){
			traceSourceElement.setIsBranch(isBranch);
			return this;
		}
		
		public Builder isTag(boolean isTag){
			traceSourceElement.setIsTag(isTag);
			return this;
		}
	} 
	
}
