package com.haks.haksvn.common.code.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "code_seq",unique = true, nullable = false)
    private int codeSeq;
	
	@Column(name = "code_group",nullable = false)
	private String codeGroup;
	
	@Column(name = "code_name",nullable = false)
	private String codeName;
	
	@Column(name = "code_value",nullable = false)
	private String codeValue;
	
	@Column(name = "code_order",nullable = false)
	private int codeOrder;
	
	public Code(){
		
	}

	public int getCodeSeq() {
		return codeSeq;
	}

	public void setCodeSeq(int codeSeq) {
		this.codeSeq = codeSeq;
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

}
