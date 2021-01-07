package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

public class PaymentType implements Serializable {
	private static final long serialVersionUID = -2434799257854123335L;

	private short payTypeId;
	private String desc;
	private String createBy;
	private Date createDate;
	private short state;

	public PaymentType() {
		super();
	}

	public PaymentType(short payTypeId, String desc, String createBy, Date createDate, short state) {
		super();
		this.payTypeId = payTypeId;
		this.desc = desc;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}

	public short getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(short payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

}
