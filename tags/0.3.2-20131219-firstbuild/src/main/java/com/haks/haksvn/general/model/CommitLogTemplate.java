package com.haks.haksvn.general.model;


public class CommitLogTemplate {

	private String repositoryKey;
	private String template;
	
	public void setRepositoryKey(String repositoryKey){
		this.repositoryKey = repositoryKey;
	}
	
	public String getRepositoryKey(){
		return repositoryKey;
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
		
		public Builder repositoryKey(String repositoryKey){
			commitLogTemplate.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder template(String template){
			commitLogTemplate.setTemplate(template);
			return this;
		}
		
	}
	
}
