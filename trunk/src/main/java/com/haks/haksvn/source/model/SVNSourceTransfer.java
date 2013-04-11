package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceTransfer {

	private String relativePath;
	private String fromRootPath;
	private String toRootPath;
	private String log;
	private boolean isToDelete;
	
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getFromRootPath() {
		return fromRootPath;
	}

	public void setFromRootPath(String fromRootPath) {
		this.fromRootPath = fromRootPath;
	}

	public String getToRootPath() {
		return toRootPath;
	}

	public void setToRootPath(String toRootPath) {
		this.toRootPath = toRootPath;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public boolean isToDelete() {
		return isToDelete;
	}

	public void getIsToDelete(boolean isToDelete) {
		this.isToDelete = isToDelete;
	}


	public static class Builder{
		
		private SVNSourceTransfer svnSourceTransfer;
		
		private Builder(SVNSourceTransfer svnSourceTransfer){
			this.svnSourceTransfer = svnSourceTransfer;
		}
		
		public static Builder getBuilder(SVNSourceTransfer svnSourceTransfer){
			return new Builder(svnSourceTransfer);
		}
		
		public SVNSourceTransfer build(){
			return svnSourceTransfer;
		}
		
		public Builder relativePath(String relativePath){
			svnSourceTransfer.setRelativePath(relativePath);
			return this;
		}
		
		public Builder fromRootPath(String fromRootPath){
			svnSourceTransfer.setFromRootPath(fromRootPath);
			return this;
		}
		
		
		
	}
}
