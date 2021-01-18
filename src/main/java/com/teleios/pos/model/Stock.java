package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Stock implements Serializable {
	private static final long serialVersionUID = -2929892885103412512L;

	private Integer stockId;
	private Integer batchNumber;
	private Integer grnNumber;
	private Product product;
	private BigDecimal purchasePrice;
	private BigDecimal sellingPrice;
	private BigDecimal addDiscount;
	private Date expDate;
	private BigDecimal qty;
	private BigDecimal lineValu;
	private short state;

	public Stock() {
		super();
	}

	public Stock(Integer stockId, Integer batchNumber, Integer grnNumber, Product product, BigDecimal purchasePrice,
			BigDecimal sellingPrice, BigDecimal addDiscount, Date expDate, BigDecimal qty, BigDecimal lineValu,
			short state) {
		super();
		this.stockId = stockId;
		this.batchNumber = batchNumber;
		this.grnNumber = grnNumber;
		this.product = product;
		this.purchasePrice = purchasePrice;
		this.sellingPrice = sellingPrice;
		this.addDiscount = addDiscount;
		this.expDate = expDate;
		this.qty = qty;
		this.lineValu = lineValu;
		this.state = state;
	}

	/**
	 * @return the stockId
	 */
	public Integer getStockId() {
		return stockId;
	}

	/**
	 * @param stockId the stockId to set
	 */
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	/**
	 * @return the batchNumber
	 */
	public Integer getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the grnNumber
	 */
	public Integer getGrnNumber() {
		return grnNumber;
	}

	/**
	 * @param grnNumber the grnNumber to set
	 */
	public void setGrnNumber(Integer grnNumber) {
		this.grnNumber = grnNumber;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the purchasePrice
	 */
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the sellingPrice
	 */
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	/**
	 * @param sellingPrice the sellingPrice to set
	 */
	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	/**
	 * @return the addDiscount
	 */
	public BigDecimal getAddDiscount() {
		return addDiscount;
	}

	/**
	 * @param addDiscount the addDiscount to set
	 */
	public void setAddDiscount(BigDecimal addDiscount) {
		this.addDiscount = addDiscount;
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
	 * @return the qty
	 */
	public BigDecimal getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	/**
	 * @return the lineValu
	 */
	public BigDecimal getLineValu() {
		return lineValu;
	}

	/**
	 * @param lineValu the lineValu to set
	 */
	public void setLineValu(BigDecimal lineValu) {
		this.lineValu = lineValu;
	}

	/**
	 * @return the state
	 */
	public short getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(short state) {
		this.state = state;
	}

}
