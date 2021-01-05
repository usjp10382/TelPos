package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1040479535446223502L;

	private Integer prdId;
	private String prdCode;
	private String prdName;
	private Brand brand;
	private Category category;
	private Uom uom;
	private Double minQtyLevel;
	private String rackDet;
	private String createBy;
	private Date createDate;
	private short state;

	public Product() {
		super();
	}

	public Product(Integer prdId, String prdCode, String prdName, Brand brand, Category category, Uom uom,
			Double minQtyLevel, String rackDet, String createBy, Date createDate, short state) {
		super();
		this.prdId = prdId;
		this.prdCode = prdCode;
		this.prdName = prdName;
		this.brand = brand;
		this.category = category;
		this.uom = uom;
		this.minQtyLevel = minQtyLevel;
		this.rackDet = rackDet;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}

	public Integer getPrdId() {
		return prdId;
	}

	public void setPrdId(Integer prdId) {
		this.prdId = prdId;
	}

	public String getPrdCode() {
		return prdCode;
	}

	public void setPrdCode(String prdCode) {
		this.prdCode = prdCode;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
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
		return "Product [prdCode=" + prdCode + ", prdName=" + prdName + "]";
	}

	public Double getMinQtyLevel() {
		return minQtyLevel;
	}

	public void setMinQtyLevel(Double minQtyLevel) {
		this.minQtyLevel = minQtyLevel;
	}

	public String getRackDet() {
		return rackDet;
	}

	public void setRackDet(String rackDet) {
		this.rackDet = rackDet;
	}

}
