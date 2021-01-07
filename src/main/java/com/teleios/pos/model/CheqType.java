package com.teleios.pos.model;

import java.io.Serializable;
import java.sql.Date;

public class CheqType implements Serializable {
	private static final long serialVersionUID = 4977940665483381120L;

	private short cheqTypeNumber;
	private String name;
	private String createBy;
	private Date createDate;
	private short cheqTypeState;

	public CheqType() {
		super();
	}

	public CheqType(short cheqTypeNumber, String name, String createBy, Date createDate, short cheqTypeState) {
		super();
		this.cheqTypeNumber = cheqTypeNumber;
		this.name = name;
		this.createBy = createBy;
		this.createDate = createDate;
		this.cheqTypeState = cheqTypeState;
	}

	public short getCheqTypeNumber() {
		return cheqTypeNumber;
	}

	public void setCheqTypeNumber(short cheqTypeNumber) {
		this.cheqTypeNumber = cheqTypeNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public short getCheqTypeState() {
		return cheqTypeState;
	}

	public void setCheqTypeState(short cheqTypeState) {
		this.cheqTypeState = cheqTypeState;
	}

}
