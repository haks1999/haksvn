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
//	,uniqueConstraints=
//		@UniqueConstraint(columnNames={"tag_name", "repository_seq"}))
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
	@Length(min=6, max=30)
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
	
	@Column(name = "dest_path")
	private String destPath;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="taggingType", referencedColumnName="code_id")
	private Code taggingTypeCode;
	
	// 연관 관계는 맺지 않는다. 불필요
	@Column(name = "repository_seq")
	private int repositorySeq;
	
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
	
	public String getDestPath(){
		return destPath;
	}
	
	public void setDestPath(String destPath){
		this.destPath = destPath;
	}


	public int getRepositorySeq() {
		return repositorySeq;
	}



	public void setRepositorySeq(int repositorySeq) {
		this.repositorySeq = repositorySeq;
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
		
		public Builder destPath(String destPath){
			tagging.setDestPath(destPath);
			return this;
		}
		
		public Builder repositorySeq(int repositorySeq){
			tagging.setRepositorySeq(repositorySeq);
			return this;
		}
		
		public Builder taggingTypeCode(Code taggingTypeCode){
			tagging.setTaggingTypeCode(taggingTypeCode);
			return this;
		}
		
	} 
	
	
	
}
