package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceTransfer {

	private String relativePath;
	private long revision;
	private boolean isToDelete;
	
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public long getRevision(){
		return revision;
	}
	
	public void setRevision(long revision){
		this.revision = revision;
	}

	public boolean getIsToDelete() {
		return isToDelete;
	}

	public void setIsToDelete(boolean isToDelete) {
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
		
		public Builder revision(long revision){
			svnSourceTransfer.setRevision(revision);
			return this;
		}
		
		public Builder isToDelete(boolean isToDelete){
			svnSourceTransfer.setIsToDelete(isToDelete);
			return this;
		}
		
	}
}
