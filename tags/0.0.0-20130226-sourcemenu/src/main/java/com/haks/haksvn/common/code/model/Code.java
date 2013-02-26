package com.haks.haksvn.common.code.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

import com.haks.haksvn.user.model.User;

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
	

}
