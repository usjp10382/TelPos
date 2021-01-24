package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer customerId;
	private String firstName;
	private String lastName;
	private String address;
	private String mobileNumber;
	private BigDecimal fwBalance;
	private String createBy;
	private Date createDate;
	private Short customerState;

	public Customer() {
		super();
	}

	public Customer(Integer customerId, String firstName, String lastName, String address, String mobileNumber,
			BigDecimal fwBalance, String createBy, Date createDate, Short customerState) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.mobileNumber = mobileNumber;
		this.fwBalance = fwBalance;
		this.createBy = createBy;
		this.createDate = createDate;
		this.customerState = customerState;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		/*
		 * String[] mob = mobileNumber.split("-"); String correctNumber = null;
		 * 
		 * if (mob.length > 0) correctNumber = mobileNumber; else correctNumber = mob[0]
		 * + mob[1];
		 */

		this.mobileNumber = mobileNumber;
	}

	public BigDecimal getFwBalance() {
		return fwBalance;
	}

	public void setFwBalance(BigDecimal fwBalance) {
		this.fwBalance = fwBalance;
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

	public Short getCustomerState() {
		return customerState;
	}

	public void setCustomerState(Short customerState) {
		this.customerState = customerState;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + "-" + getMobileNumber();
	}

}
