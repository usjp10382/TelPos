package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExpenditureList implements Serializable {
	private static final long serialVersionUID = 7932796517664203241L;

	private Integer expLstNumber;
	private Date expDate;
	private BigDecimal expValue;
	private Expenditure expenditure;
	private String createBy;
	private short expState;

	public ExpenditureList() {
		super();

	}

	public ExpenditureList(Integer expLstNumber, Date expDate, BigDecimal expValue, Expenditure expenditure,
			String createBy, short expState) {
		super();
		this.expLstNumber = expLstNumber;
		this.expDate = expDate;
		this.expValue = expValue;
		this.expenditure = expenditure;
		this.createBy = createBy;
		this.expState = expState;
	}

	/**
	 * @return the expLstNumber
	 */
	public Integer getExpLstNumber() {
		return expLstNumber;
	}

	/**
	 * @param expLstNumber the expLstNumber to set
	 */
	public void setExpLstNumber(Integer expLstNumber) {
		this.expLstNumber = expLstNumber;
	}

	/**
	 * @return the expDate
	 */
	public Date getExpDate() {
		return expDate;
	}

	/**
	 * @param expDate the expDate to set
	 */
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	/**
	 * @return the expValue
	 */
	public BigDecimal getExpValue() {
		return expValue;
	}

	/**
	 * @param expValue the expValue to set
	 */
	public void setExpValue(BigDecimal expValue) {
		this.expValue = expValue;
	}

	/**
	 * @return the expenditure
	 */
	public Expenditure getExpenditure() {
		return expenditure;
	}

	/**
	 * @param expenditure the expenditure to set
	 */
	public void setExpenditure(Expenditure expenditure) {
		this.expenditure = expenditure;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the expState
	 */
	public short getExpState() {
		return expState;
	}

	/**
	 * @param expState the expState to set
	 */
	public void setExpState(short expState) {
		this.expState = expState;
	}

}
