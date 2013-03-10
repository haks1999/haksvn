package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNLogEntryPath;

@Component
public class SVNSourceLogChanged {

	private String path;
	private char type;
	private String typeName;
	
	public SVNSourceLogChanged(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ SVNSourceLogChanged ]\n - path : " + path + 
					"\n - type : " + type;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
		this.typeName = type == 'A'?"Added":(type=='D'?"Deleted":(type=='M'?"Modified":"Replaced"));
	}
	
	public String getTypeName(){
		return typeName;
	}
	
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public static class Builder{
		
		private SVNSourceLogChanged svnSourceLogChanged;
		
		private Builder(SVNSourceLogChanged svnSourceLogChanged){
			this.svnSourceLogChanged = svnSourceLogChanged;
		}
		
		public static Builder getBuilder(SVNSourceLogChanged svnSourceLogChanged){
			return new Builder(svnSourceLogChanged);
		}
		
		public SVNSourceLogChanged build(){
			return svnSourceLogChanged;
		}
		
		public Builder path(String path){
			svnSourceLogChanged.setPath(path);
			return this;
		}
		
		public Builder type(char type){
			svnSourceLogChanged.setType(type);
			return this;
		}
	}
}
