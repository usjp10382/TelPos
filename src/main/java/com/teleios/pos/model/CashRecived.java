package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CashRecived implements Serializable {
	private static final long serialVersionUID = -7639710351410803250L;

	private Integer cashRecNumber;
	private BigDecimal amount;
	private BigDecimal cashier;
	private BigDecimal change;
	private Date createDate;
	private Receivable receivable;
	private String createBy;
	private short cashRecState;
	private Customer customer;
	private InvHdr invHdr;

	public CashRecived() {
		super();

	}

	public CashRecived(Integer cashRecNumber, BigDecimal amount, BigDecimal cashier, BigDecimal change, Date createDate,
			Receivable receivable, String createBy, short cashRecState, Customer customer, InvHdr invHdr) {
		super();
		this.cashRecNumber = cashRecNumber;
		this.amount = amount;
		this.cashier = cashier;
		this.change = change;
		this.createDate = createDate;
		this.receivable = receivable;
		this.createBy = createBy;
		this.cashRecState = cashRecState;
		this.customer = customer;
		this.invHdr = invHdr;
	}

	public Integer getCashRecNumber() {
		return cashRecNumber;
	}

	public void setCashRecNumber(Integer cashRecNumber) {
		this.cashRecNumber = cashRecNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCashier() {
		return cashier;
	}

	public void setCashier(BigDecimal cashier) {
		this.cashier = cashier;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Receivable getReceivable() {
		return receivable;
	}

	public void setReceivable(Receivable receivable) {
		this.receivable = receivable;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public short getCashRecState() {
		return cashRecState;
	}

	public void setCashRecState(short cashRecState) {
		this.cashRecState = cashRecState;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public InvHdr getInvHdr() {
		return invHdr;
	}

	public void setInvHdr(InvHdr invHdr) {
		this.invHdr = invHdr;
	}

}
