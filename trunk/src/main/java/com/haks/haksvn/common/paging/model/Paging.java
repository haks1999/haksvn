package com.haks.haksvn.common.paging.model;


public class Paging<T> {

	private int start = 0;
	private int limit = 20;
	private int total = -1;
	private T model;
	
	public Paging(){
		
	}
	
	public Paging(T model){
		this.model = model;
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
	
	
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
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
		
		public Builder start(int start){
			paging.setStart(start);
			return this;
		}
		
		public Builder limit(int limit){
			paging.setLimit(limit);
			return this;
		}
		
		public Builder total(int total){
			paging.setTotal(total);
			return this;
		}
		
	} 
}
