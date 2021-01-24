/**
 * 
 */
package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class InvDet implements Serializable {
	private static final long serialVersionUID = 1221926204674463398L;

	private Integer invDetNumber;
	private Integer batchNumber;
	private Stock stockItem;
	private BigDecimal purchacePrice;
	private BigDecimal sellingPrice;
	private BigDecimal qty;
	private BigDecimal discount;
	private BigDecimal cusAdva;
	private BigDecimal amount;
	private InvHdr invHdr;

	public InvDet() {
		super();
	}

	public InvDet(Integer invDetNumber, Integer batchNumber, Stock stockItem, BigDecimal purchacePrice,
			BigDecimal sellingPrice, BigDecimal qty, BigDecimal discount, BigDecimal cusAdva, BigDecimal amount,
			InvHdr invHdr) {
		super();
		this.invDetNumber = invDetNumber;
		this.batchNumber = batchNumber;
		this.stockItem = stockItem;
		this.purchacePrice = purchacePrice;
		this.sellingPrice = sellingPrice;
		this.qty = qty;
		this.discount = discount;
		this.cusAdva = cusAdva;
		this.amount = amount;
		this.invHdr = invHdr;
	}

	public Integer getInvDetNumber() {
		return invDetNumber;
	}

	public void setInvDetNumber(Integer invDetNumber) {
		this.invDetNumber = invDetNumber;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Stock getStockItem() {
		return stockItem;
	}

	public void setStockItem(Stock stockItem) {
		this.stockItem = stockItem;
	}

	public BigDecimal getPurchacePrice() {
		return purchacePrice;
	}

	public void setPurchacePrice(BigDecimal purchacePrice) {
		this.purchacePrice = purchacePrice;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getCusAdva() {
		return cusAdva;
	}

	public void setCusAdva(BigDecimal cusAdva) {
		this.cusAdva = cusAdva;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public InvHdr getInvHdr() {
		return invHdr;
	}

	public void setInvHdr(InvHdr invHdr) {
		this.invHdr = invHdr;
	}

}
