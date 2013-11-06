package com.haks.haksvn.general.model;

import org.springframework.stereotype.Component;

@Component
public class MailTemplate {

	private String repositoryKey;
	private String subject;
	private String text;
	
	public void setRepositoryKey(String repositoryKey){
		this.repositoryKey = repositoryKey;
	}
	
	public String getRepositoryKey(){
		return repositoryKey;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public static class Builder{
		
		private MailTemplate mailTemplate;
		
		private Builder(MailTemplate mailTemplate){
			this.mailTemplate = mailTemplate;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new MailTemplate());
		}
		
		public static Builder getBuilder(MailTemplate mailTemplate){
			return new Builder(mailTemplate);
		}
		
		public MailTemplate build(){
			return mailTemplate;
		}
		
		public Builder repositoryKey(String repositoryKey){
			mailTemplate.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder subject(String subject){
			mailTemplate.setSubject(subject);
			return this;
		}
		
		public Builder text(String text){
			mailTemplate.setText(text);
			return this;
		}
		
	}
	
}
