package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Receivable implements Serializable {
	private static final long serialVersionUID = 9095429757876225508L;

	private Integer receivableNumber;
	private BigDecimal balance;
	private InvHdr invHdr;
	private PaymentType paymentType;
	private short rcvState;
	private Customer customer;
	private Date createDate;
	private String createBy;

	public Receivable() {
		super();
	}

	public Receivable(Integer receivableNumber, BigDecimal balance, InvHdr invHdr, PaymentType paymentType,
			short rcvState, Customer customer, Date createDate, String createBy) {
		super();
		this.receivableNumber = receivableNumber;
		this.balance = balance;
		this.invHdr = invHdr;
		this.paymentType = paymentType;
		this.rcvState = rcvState;
		this.customer = customer;
		this.createDate = createDate;
		this.createBy = createBy;
	}

	public Integer getReceivableNumber() {
		return receivableNumber;
	}

	public void setReceivableNumber(Integer receivableNumber) {
		this.receivableNumber = receivableNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public InvHdr getInvHdr() {
		return invHdr;
	}

	public void setInvHdr(InvHdr invHdr) {
		this.invHdr = invHdr;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public short getRcvState() {
		return rcvState;
	}

	public void setRcvState(short rcvState) {
		this.rcvState = rcvState;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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
