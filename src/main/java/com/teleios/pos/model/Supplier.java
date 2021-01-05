package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

public class Supplier implements Serializable {
	private static final long serialVersionUID = -8186120681785745754L;

	private Integer supplierId;
	private String supplierName;
	private String address;
	private String contactNumber;
	private String fixedNumber;
	private String email;
	private Date createDate;
	private String createBy;
	private short suppState;

	public Supplier() {
		super();
	}

	public Supplier(Integer supplierId, String supplierName, String address, String contactNumber, String fixedNumber,
			String email, Date createDate, String createBy, short suppState) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.address = address;
		this.contactNumber = contactNumber;
		this.fixedNumber = fixedNumber;
		this.email = email;
		this.createDate = createDate;
		this.createBy = createBy;
		this.suppState = suppState;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getFixedNumber() {
		return fixedNumber;
	}

	public void setFixedNumber(String fixedNumber) {
		this.fixedNumber = fixedNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public short getSuppState() {
		return suppState;
	}

	public void setSuppState(short suppState) {
		this.suppState = suppState;
	}

}
