package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceTagging {

	private String srcPath;
	private String destPath;
	private String log;
	
	public SVNSourceTagging(){
		
	}
	
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	public static class Builder{
		
		private SVNSourceTagging svnSourceTagging;
		
		private Builder(SVNSourceTagging svnSourceTagging){
			this.svnSourceTagging = svnSourceTagging;
		}
		
		public static Builder getBuilder(SVNSourceTagging svnSourceTagging){
			return new Builder(svnSourceTagging);
		}
		
		public SVNSourceTagging build(){
			return svnSourceTagging;
		}
		
		public Builder srcPath(String srcPath){
			svnSourceTagging.setSrcPath(srcPath);
			return this;
		}
		
		public Builder destPath(String destPath){
			svnSourceTagging.setDestPath(destPath);
			return this;
		}
		
		public Builder log(String log){
			svnSourceTagging.setLog(log);
			return this;
		}
		
	}
	
}
