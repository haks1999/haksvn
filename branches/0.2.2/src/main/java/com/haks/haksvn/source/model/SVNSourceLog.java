package com.haks.haksvn.source.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SVNSourceLog {

	private String author;
	private long revision;
	private long date;
	private String message;
	private List<SVNSourceLogChanged> changedList;
	private ReviewSummarySimple reviewSummarySimple;
	
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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<SVNSourceLogChanged> getChangedList(){
		return changedList;
	}
	
	public void setChangedList(List<SVNSourceLogChanged> changedList){
		this.changedList = changedList;
	}
	
	public ReviewSummarySimple getReviewSummarySimple(){
		return reviewSummarySimple;
	}
	
	public void setReviewSummarySimple(ReviewSummarySimple reviewSummarySimple){
		this.reviewSummarySimple = reviewSummarySimple;
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
		
		public Builder date(long date){
			svnSourceLog.setDate(date);
			return this;
		}
		
		public Builder message(String message){
			svnSourceLog.setMessage(message);
			return this;
		}
		
		public Builder changedList(List<SVNSourceLogChanged> changedList){
			svnSourceLog.setChangedList(changedList);
			return this;
		}
		
		public Builder reviewSummarySimple(ReviewSummarySimple reviewSummarySimple){
			svnSourceLog.setReviewSummarySimple(reviewSummarySimple);
			return this;
		}
		
	}
}
