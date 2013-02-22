package com.haks.haksvn.common.paging.model;

import org.springframework.stereotype.Component;

@Component
public class Paging {

	private int start = 0;
	private int limit = 20;
	
	public Paging(){
		
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public static class Builder{
		
		private Paging paging;
		
		private Builder(Paging paging){
			this.paging = paging;
		}
		
		public static Builder getBuilder(Paging paging){
			return new Builder(paging);
		}
		
		public Paging build(){
			return paging;
		}
		
		public Builder start(int start){
			paging.setStart(start);
			return this;
		}
		
		public Builder limit(int limit){
			paging.setLimit(limit);
			return this;
		}
		
	} 
}
