package com.haks.haksvn.common.paging.model;


public class NextPaging<T> {

	private long start = 0;
	private long end = 0;
	private long limit = 20;
	private long direction = 0;	//0:after, -1:before
	private boolean hasNext = false;
	private boolean hasPrev = false;
	private T model;
	
	public NextPaging(){
		
	}
	
	public NextPaging(T model){
		this.model = model;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}
	
	public long getEnd(){
		return end;
	}
	
	public void setEnd(long end){
		this.end = end;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}
	
	public long getDirection() {
		return direction;
	}

	public void setDirection(long direction) {
		this.direction = direction;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}
	
	public boolean getHasNext(){
		return hasNext;
	}
	
	public void setHasNext(boolean hasNext){
		this.hasNext = hasNext;
	}
	
	public boolean getHasPrev(){
		return hasPrev;
	}
	
	public void setHasPrev(boolean hasPrev){
		this.hasPrev = hasPrev;
	}



	public static class Builder{
		
		private NextPaging<?> paging;
		
		private Builder(NextPaging<?> paging){
			this.paging = paging;
		}
		
		public static Builder getBuilder(NextPaging<?> paging){
			return new Builder(paging);
		}
		
		public NextPaging<?> build(){
			return paging;
		}
		
		public Builder start(long start){
			paging.setStart(start);
			return this;
		}
		
		public Builder end(long end){
			paging.setEnd(end);
			return this;
		}
		
		public Builder limit(long limit){
			paging.setLimit(limit);
			return this;
		}
		
		public Builder direction(long direction){
			paging.setDirection(direction);
			return this;
		}
		
		public Builder hasNext(boolean hasNext){
			paging.setHasNext(hasNext);
			return this;
		}
		
		public Builder hasPrev(boolean hasPrev){
			paging.setHasPrev(hasPrev);
			return this;
		}
		
	} 
}
