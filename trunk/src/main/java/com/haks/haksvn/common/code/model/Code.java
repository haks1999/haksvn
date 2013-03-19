package com.haks.haksvn.common.code.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name="code")
@FilterDefs({@FilterDef(name="getCodeValue", 
				parameters = {@ParamDef( name="codeGroup", type="String" ),@ParamDef( name="codeName", type="String" )} ),
			@FilterDef(name="getCodeName", 
				parameters = {@ParamDef( name="codeGroup", type="String" ),@ParamDef( name="codeValue", type="String" )} )})
public class Code {

	@Id
	@Column(name = "code_id",unique = true, nullable = false)
    private String codeId;
	
	@Column(name = "code_group",nullable = false)
	private String codeGroup;
	
	@Column(name = "code_name",nullable = false)
	private String codeName;
	
	@Column(name = "code_value",nullable = false)
	private String codeValue;
	
	@Column(name = "code_order",nullable = false)
	private int codeOrder;
	
	/*
	@OneToMany(mappedBy=""fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users", joinColumns = { 
			@JoinColumn(name = "auth_type", referencedColumnName = "code_id", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "code_id", referencedColumnName = "auth_type", nullable = false) })
	@Column(name="user_seq")
	*/
	/*
	@OneToMany(mappedBy="code", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> userList = new ArrayList<User>();
	*/
	
	public Code(){
		
	}

	@Override
	public String toString(){
		return "[ Code ]\n - codeId : " + codeId + "\n - codeGroup : " + codeGroup +
					"\n - codeName : " + codeName + "\n - codeValue : " + codeValue + "\n - codeOrder : " + codeOrder;
	}
	
	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId){
		this.codeId = codeId;
	}

	public String getCodeGroup() {
		return codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public int getCodeOrder() {
		return codeOrder;
	}

	public void setCodeOrder(int codeOrder) {
		this.codeOrder = codeOrder;
	}

	/*
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	*/
	

	public static class Builder{
		
		private Code code;
		
		private Builder(Code code){
			this.code = code;
		}
		
		public static Builder getBuilder(Code code){
			return new Builder(code);
		}
		
		public Code build(){
			return code;
		}
		
		public Builder codeId(String codeId){
			code.setCodeId(codeId);
			return this;
		}
		
		public Builder codeGroup(String codeGroup){
			code.setCodeGroup( codeGroup );
			return this;
		}
		
		public Builder codeName(String codeName){
			code.setCodeName(codeName);
			return this;
		}
		
		public Builder codeValue(String codeValue){
			code.setCodeValue(codeValue);
			return this;
		}
		
		public Builder codeOrder(int codeOrder){
			code.setCodeOrder(codeOrder);
			return this;
		}
		
		
	} 
}
