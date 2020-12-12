package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

public class Expenditure implements Serializable {

	/**
	 * @ author - Dilan Tharaka
	 * @ Date - 2020.12.01
	 */
	private static final long serialVersionUID = 2949751676835713205L;

	private int expId;
	private String dec;
	private String createBy;
	private Date createDate;
	private short expState;
	
	
	
	public Expenditure() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Expenditure(int expId, String dec, String createBy, Date createDate, short expState) {
		super();
		this.expId = expId;
		this.dec = dec;
		this.createBy = createBy;
		this.createDate = createDate;
		this.expState = expState;
	}



	public int getExpId() {
		return expId;
	}



	public void setExpId(int expId) {
		this.expId = expId;
	}



	public String getDec() {
		return dec;
	}



	public void setDec(String dec) {
		this.dec = dec;
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



	public short getExpState() {
		return expState;
	}



	public void setExpState(short expState) {
		this.expState = expState;
	}



	@Override
	public String toString() {
		return "Expenditure [expId=" + expId + ", dec=" + dec + "]";
	}
	
	
	
}
