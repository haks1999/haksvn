package com.haks.haksvn.source.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.haks.haksvn.repository.model.Repository;
import com.haks.haksvn.repository.model.Repository.Builder;
import com.haks.haksvn.user.model.User;

@Component
public class SVNSource {

	private String title;
	private boolean isFolder;
	private boolean isLazy;
	
	private String name;
	private String path;
	private String author;
	private long revision;
	private String date;
	private String commitMessage;
	private long size;
	
	public SVNSource(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ SVNSource ]\n - title : " + title + "\n - isFolder : " + isFolder +
					"\n - name : " + name + "\n - path : " + path + "\n - author : " + author + "\n - size : " + size +
					"\n - revision : " + revision + "\n - date : " + date + "\n - commitMessage : " + commitMessage;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
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
		
		public Builder path(String path){
			svnSource.setPath(path);
			return this;
		}
		
		public Builder author(String author){
			svnSource.setAuthor(author);
			return this;
		}
		
		public Builder revision(long revision){
			svnSource.setRevision(revision);
			return this;
		}
		
		public Builder date(String date){
			svnSource.setDate(date);
			return this;
		}
		
		public Builder commitMessage(String commitMessage){
			svnSource.setCommitMessage(commitMessage);
			return this;
		}
		
		public Builder size(long size){
			svnSource.setSize(size);
			return this;
		}
		
	}
}
