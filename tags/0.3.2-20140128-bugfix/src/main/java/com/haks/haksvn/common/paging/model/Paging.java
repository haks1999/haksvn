package com.haks.haksvn.common.paging.model;


public class Paging<T> {

	private long start = 0;
	private long limit = 20;
	private long total = -1;
	private T model;
	
	public Paging(){
		
	}
	
	public Paging(T model){
		this.model = model;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}
	
	
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}



	public static class Builder{
		
		private Paging<?> paging;
		
		private Builder(Paging<?> paging){
			this.paging = paging;
		}
		
		public static Builder getBuilder(Paging<?> paging){
			return new Builder(paging);
		}
		
		public Paging<?> build(){
			return paging;
		}
		
		public Builder start(long start){
			paging.setStart(start);
			return this;
		}
		
		public Builder limit(long limit){
			paging.setLimit(limit);
			return this;
		}
		
		public Builder total(long total){
			paging.setTotal(total);
			return this;
		}
		
	} 
}
