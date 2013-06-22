package com.haks.haksvn.general.model;


public class CommitLogTemplate {

	private int repositorySeq;
	private String template;
	
	public void setRepositorySeq(int repositorySeq){
		this.repositorySeq = repositorySeq;
	}
	
	public int getRepositorySeq(){
		return repositorySeq;
	}
	
	public void setTemplate(String template){
		this.template = template;
	}
	
	public String getTemplate(){
		return template;
	}
	
	public static class Builder{
		
		private CommitLogTemplate commitLogTemplate;
		
		private Builder(CommitLogTemplate commitLogTemplate){
			this.commitLogTemplate = commitLogTemplate;
		}
		
		public static Builder getBuilder(CommitLogTemplate commitLogTemplate){
			return new Builder(commitLogTemplate);
		}
		
		public CommitLogTemplate build(){
			return commitLogTemplate;
		}
		
		public Builder repositorySeq(int repositorySeq){
			commitLogTemplate.setRepositorySeq(repositorySeq);
			return this;
		}
		
		public Builder template(String template){
			commitLogTemplate.setTemplate(template);
			return this;
		}
		
	}
	
}
