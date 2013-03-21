package com.haks.haksvn.source.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SVNSource {

	private String title;
	private boolean isFolder;
	private boolean isLazy;
	
	private String name;
	private String path;
	private long date;
	private String author;
	private long size;
	private String formattedSize;
	private long revision;
	private boolean isTextMimeType;
	private String content;
	private SVNSourceLog log;
	private List<SVNSourceLog> olderLogs;
	private List<SVNSourceLog> newerLogs;
	
	public SVNSource(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ SVNSource ]\n - title : " + title + "\n - isFolder : " + isFolder + "\n - isTextMimeType : " + isTextMimeType +
					"\n - name : " + name + "\n - path : " + path + "\n - formattedSize : " + formattedSize + "\n - revision : " + revision;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	
	public boolean getIsLazy() {
		return isLazy;
	}

	public void setIsLazy(boolean isLazy) {
		this.isLazy = isLazy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getDate(){
		return date;
	}
	
	public void setDate(long date){
		this.date = date;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public long getSize(){
		return size;
	}
	
	public void setSize(long size){
		this.size = size;
	}

	public String getFormattedSize() {
		return formattedSize;
	}

	public void setFormattedSize(String formattedSize) {
		this.formattedSize = formattedSize;
	}
	
	public long getRevision(){
		return revision;
	}
	
	public void setRevision(long revision){
		this.revision = revision;
	}
	
	public boolean getIsTextMimeType(){
		return isTextMimeType;
	}
	
	public void setIsTextMimeType(boolean isTextMimeType){
		this.isTextMimeType = isTextMimeType;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<SVNSourceLog> getOlderLogs() {
		return olderLogs;
	}

	public void setOlderLogs(List<SVNSourceLog> olderLogs) {
		this.olderLogs = olderLogs;
	}
	
	public List<SVNSourceLog> getNewerLogs() {
		return newerLogs;
	}

	public void setNewerLogs(List<SVNSourceLog> newerLogs) {
		this.newerLogs = newerLogs;
	}
	
	public SVNSourceLog getLog(){
		return log;
	}

	public void setLog(SVNSourceLog log){
		this.log = log;
	}



	public static class Builder{
		
		private SVNSource svnSource;
		
		private Builder(SVNSource svnSource){
			this.svnSource = svnSource;
		}
		
		public static Builder getBuilder(SVNSource svnSource){
			return new Builder(svnSource);
		}
		
		public SVNSource build(){
			return svnSource;
		}
		
		public Builder title(String title){
			svnSource.setTitle(title);
			return this;
		}
		
		public Builder isFolder(boolean isFolder){
			svnSource.setIsFolder(isFolder);
			return this;
		}
		
		public Builder isLazy(boolean isLazy){
			svnSource.setIsLazy(isLazy);
			return this;
		}
		
		public Builder name(String name){
			svnSource.setName(name);
			return this;
		}
		
		public Builder date(long date){
			svnSource.setDate(date);
			return this;
		}
		
		public Builder author(String author){
			svnSource.setAuthor(author);
			return this;
		}
		
		public Builder path(String path){
			svnSource.setPath(path);
			return this;
		}
		
		public Builder size(long size){
			svnSource.setSize(size);
			return this;
		}
		
		public Builder formattedSize(String formattedSize){
			svnSource.setFormattedSize(formattedSize);
			return this;
		}
		
		public Builder revision(long revision){
			svnSource.setRevision(revision);
			return this;
		}
		
		public Builder textMimeType(boolean isTextMimeType){
			svnSource.setIsTextMimeType(isTextMimeType);
			return this;
		}
		
		public Builder content(String content){
			svnSource.setContent(content);
			return this;
		}
		
		public Builder olderLogs(List<SVNSourceLog> olderLogs){
			svnSource.setOlderLogs(olderLogs);
			return this;
		}
		
		public Builder newerLogs(List<SVNSourceLog> newerLogs){
			svnSource.setNewerLogs(newerLogs);
			return this;
		}
		
		public Builder log(SVNSourceLog log){
			svnSource.setLog(log);
			return this;
		}
		
	}
}
