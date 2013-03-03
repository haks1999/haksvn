package com.haks.haksvn.source.model;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceLog {

	private String author;
	private long revision;
	private String date;
	private String message;
	
	public SVNSourceLog(){
		
	}
	
	@Override
	public String toString(){
		return "\n[ SVNSource ]\n - author : " + author + 
					"\n - revision : " + revision + "\n - date : " + date + "\n - message : " + message;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class Builder{
		
		private SVNSourceLog svnSourceLog;
		
		private Builder(SVNSourceLog svnSourceLog){
			this.svnSourceLog = svnSourceLog;
		}
		
		public static Builder getBuilder(SVNSourceLog svnSourceLog){
			return new Builder(svnSourceLog);
		}
		
		public SVNSourceLog build(){
			return svnSourceLog;
		}
		
		public Builder author(String author){
			svnSourceLog.setAuthor(author);
			return this;
		}
		
		public Builder revision(long revision){
			svnSourceLog.setRevision(revision);
			return this;
		}
		
		public Builder date(String date){
			svnSourceLog.setDate(date);
			return this;
		}
		
		public Builder message(String message){
			svnSourceLog.setMessage(message);
			return this;
		}
		
	}
}
