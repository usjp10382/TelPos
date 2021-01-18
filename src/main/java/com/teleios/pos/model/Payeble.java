package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Payeble implements Serializable {
	private static final long serialVersionUID = 6124472518761023050L;

	private Integer paybNumber;
	private BigDecimal balance;
	private GrnHdr grnHdr;
	private PaymentType paymentType;
	private short state;
	private Supplier supplier;
	private Date createDate;
	private String createBy;

	public Payeble() {
		super();

	}

	public Payeble(Integer paybNumber, BigDecimal balance, GrnHdr grnHdr, PaymentType paymentType, short state,
			Supplier supplier, Date createDate, String createBy) {
		super();
		this.paybNumber = paybNumber;
		this.balance = balance;
		this.grnHdr = grnHdr;
		this.paymentType = paymentType;
		this.state = state;
		this.supplier = supplier;
		this.createDate = createDate;
		this.createBy = createBy;
	}

	public Integer getPaybNumber() {
		return paybNumber;
	}

	public void setPaybNumber(Integer paybNumber) {
		this.paybNumber = paybNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public GrnHdr getGrnHdr() {
		return grnHdr;
	}

	public void setGrnHdr(GrnHdr grnHdr) {
		this.grnHdr = grnHdr;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
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

}
