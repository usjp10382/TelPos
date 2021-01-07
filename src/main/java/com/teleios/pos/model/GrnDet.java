package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class GrnDet implements Serializable {
	private static final long serialVersionUID = -8022084412826718448L;

	private Integer grnDetNumber;
	private Product product;
	private BigDecimal qty;
	private Double unitPrice;
	private boolean IsDiscount;
	private Float discount;
	private Double sellingPrice;
	private Date expireDate;
	private short state;
	private GrnHdr grnHdr;

	public GrnDet() {
		super();
	}

	public GrnDet(Integer grnDetNumber, Product product, BigDecimal qty, Double unitPrice, boolean isDiscount,
			Float discount, Double sellingPrice, Date expireDate, short state, GrnHdr grnHdr) {
		super();
		this.grnDetNumber = grnDetNumber;
		this.product = product;
		this.qty = qty;
		this.unitPrice = unitPrice;
		IsDiscount = isDiscount;
		this.discount = discount;
		this.sellingPrice = sellingPrice;
		this.expireDate = expireDate;
		this.state = state;
		this.grnHdr = grnHdr;
	}

	public Integer getGrnDetNumber() {
		return grnDetNumber;
	}

	public void setGrnDetNumber(Integer grnDetNumber) {
		this.grnDetNumber = grnDetNumber;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public boolean isIsDiscount() {
		return IsDiscount;
	}

	public void setIsDiscount(boolean isDiscount) {
		IsDiscount = isDiscount;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public GrnHdr getGrnHdr() {
		return grnHdr;
	}

	public void setGrnHdr(GrnHdr grnHdr) {
		this.grnHdr = grnHdr;
	}

}
