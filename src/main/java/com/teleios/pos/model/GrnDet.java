package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GrnDet implements Serializable {
	private static final long serialVersionUID = -8022084412826718448L;

	private Integer grnDetNumber;
	private Product product;
	private BigDecimal qty;
	private BigDecimal unitPrice;
	private boolean IsDiscount;
	private BigDecimal discount;
	private BigDecimal netPrice;
	private BigDecimal sellingPrice;
	private BigDecimal subTotal;
	private Date expireDate;
	private short state;
	private GrnHdr grnHdr;

	public GrnDet() {
		super();
	}

	public GrnDet(Integer grnDetNumber, Product product, BigDecimal qty, BigDecimal unitPrice, boolean isDiscount,
			BigDecimal discount, BigDecimal netPrice, BigDecimal sellingPrice, BigDecimal subTotal, Date expireDate,
			short state, GrnHdr grnHdr) {
		super();
		this.grnDetNumber = grnDetNumber;
		this.product = product;
		this.qty = qty;
		this.unitPrice = unitPrice;
		IsDiscount = isDiscount;
		this.discount = discount;
		this.sellingPrice = sellingPrice;
		this.netPrice = netPrice;
		this.subTotal = subTotal;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public boolean isIsDiscount() {
		return IsDiscount;
	}

	public void setIsDiscount(boolean isDiscount) {
		IsDiscount = isDiscount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
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

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

}
