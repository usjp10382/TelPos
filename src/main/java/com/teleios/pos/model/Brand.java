package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

public class Brand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3975426495450020283L;

	private Integer brandId;
	private String brandName;
	private String createBy;
	private Date createDate;
	private short state;

	public Brand() {
		super();

	}

	public Brand(Integer brandId, String brandName, String createBy, Date createDate, short state) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	@Override
	public String toString() {
		return "Brand [brandId=" + brandId + ", brandName=" + brandName + "]";
	}

}
