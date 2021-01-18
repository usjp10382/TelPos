package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CashPayment implements Serializable {
	private static final long serialVersionUID = 542713926994042578L;

	private Integer cashPayNumber;
	private BigDecimal amount;
	private BigDecimal cashier;
	private BigDecimal change;
	private Date createDate;
	private Payeble payeble;
	private String createBy;
	private short state;
	private GrnHdr grnHdr;
	private ExpenditureList expenditureList;

	public CashPayment() {
		super();

	}

	public CashPayment(Integer cashPayNumber, BigDecimal amount, BigDecimal cashier, BigDecimal change, Date createDate,
			Payeble payeble, String createBy, short state, GrnHdr grnHdr, ExpenditureList expenditureList) {
		super();
		this.cashPayNumber = cashPayNumber;
		this.amount = amount;
		this.cashier = cashier;
		this.change = change;
		this.createDate = createDate;
		this.payeble = payeble;
		this.createBy = createBy;
		this.state = state;
		this.grnHdr = grnHdr;
		this.expenditureList = expenditureList;
	}

	public Integer getCashPayNumber() {
		return cashPayNumber;
	}

	public void setCashPayNumber(Integer cashPayNumber) {
		this.cashPayNumber = cashPayNumber;
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

	public Payeble getPayeble() {
		return payeble;
	}

	public void setPayeble(Payeble payeble) {
		this.payeble = payeble;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

	public ExpenditureList getExpenditureList() {
		return expenditureList;
	}

	public void setExpenditureList(ExpenditureList expenditureList) {
		this.expenditureList = expenditureList;
	}

}
