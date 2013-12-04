package com.haks.haksvn.transfer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.haks.haksvn.common.code.model.Code;
import com.haks.haksvn.user.model.User;

@Entity
@Table(name="tagging")
public class Tagging {
	
	public Tagging(){
		
	}
	
	@Id
	@GenericGenerator(name="taggingSeqGen" , strategy="increment")
	@GeneratedValue(generator="taggingSeqGen")
	@Column(name = "tagging_seq",unique = true, nullable = false)
	private int taggingSeq;
	
	@Column(name = "tag_name", nullable = false)
	@NotEmpty
	@Length(min=6, max=40)
	private String tagName = "";
	
	@Column(name = "description", nullable = false, length=500)
	@NotEmpty
	@Length(min=10, max=500)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="tagging_user_id", nullable = false, referencedColumnName="user_id")
	private User taggingUser;
	
	@Column(name = "tagging_date", nullable = false)
	private long taggingDate;
	
	@Column(name = "src_path")
	private String srcPath;
	
	@Column(name = "src_revision")
	private long srcRevision;
	
	@Column(name = "dest_path")
	private String destPath;
	
	@Column(name = "dest_revision")
	private long destRevision;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="taggingType", referencedColumnName="code_id")
	private Code taggingTypeCode;
	
	// 연관 관계는 맺지 않는다. 불필요
	@Column(name = "repository_key")
	private String repositoryKey;
	
	public int getTaggingSeq() {
		return taggingSeq;
	}



	public void setTaggingSeq(int taggingSeq) {
		this.taggingSeq = taggingSeq;
	}


	public String getTagName(){
		return tagName.toUpperCase();
	}
	
	public void setTagName(String tagName){
		this.tagName = tagName.toUpperCase();
	}

	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public User getTaggingUser() {
		return taggingUser;
	}



	public void setTaggingUser(User taggingUser) {
		this.taggingUser = taggingUser;
	}



	public long getTaggingDate() {
		return taggingDate;
	}



	public void setTaggingDate(long taggingDate) {
		this.taggingDate = taggingDate;
	}



	public String getSrcPath() {
		return srcPath;
	}


	public void setSrcPath(String srcPath){
		this.srcPath = srcPath;
	}
	
	public long getSrcRevision(){
		return srcRevision;
	}
	
	public void setSrcRevision(long srcRevision){
		this.srcRevision = srcRevision;
	}
	
	public String getDestPath(){
		return destPath;
	}
	
	public void setDestPath(String destPath){
		this.destPath = destPath;
	}
	
	public long getDestRevision(){
		return destRevision;
	}
	
	public void setDestRevision(long destRevision){
		this.destRevision = destRevision;
	}


	public String getRepositoryKey() {
		return repositoryKey;
	}



	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}
	
	public Code getTaggingTypeCode(){
		return taggingTypeCode;
	}
	
	public void setTaggingTypeCode(Code taggingTypeCode){
		this.taggingTypeCode = taggingTypeCode;
	}



	public static class Builder{
		
		private Tagging tagging;
		
		private Builder(Tagging tagging){
			this.tagging = tagging;
		}
		
		public static Builder getBuilder(){
			return getBuilder(new Tagging());
		}
		
		public static Builder getBuilder(Tagging transfer){
			return new Builder(transfer);
		}
		
		public Tagging build(){
			return tagging;
		}
		
		public Builder taggingSeq(int taggingSeq){
			tagging.setTaggingSeq(taggingSeq);
			return this;
		}
		
		public Builder tagName(String tagName){
			tagging.setTagName(tagName);
			return this;
		}
		
		public Builder description(String description){
			tagging.setDescription(description);
			return this;
		}
		
		public Builder taggingUser(User user){
			tagging.setTaggingUser(user);
			return this;
		}
		
		public Builder taggingDate(long taggingDate){
			tagging.setTaggingDate(taggingDate);
			return this;
		}
		
		public Builder srcPath(String srcPath){
			tagging.setSrcPath(srcPath);
			return this;
		}
		
		public Builder srcRevision(long srcRevision){
			tagging.setSrcRevision(srcRevision);
			return this;
		}
		
		public Builder destPath(String destPath){
			tagging.setDestPath(destPath);
			return this;
		}
		
		public Builder destRevision(long destRevision){
			tagging.setDestRevision(destRevision);
			return this;
		}
		
		public Builder repositoryKey(String repositoryKey){
			tagging.setRepositoryKey(repositoryKey);
			return this;
		}
		
		public Builder taggingTypeCode(Code taggingTypeCode){
			tagging.setTaggingTypeCode(taggingTypeCode);
			return this;
		}
		
	} 
	
	
	
}
