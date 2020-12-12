/**
 * 
 */
package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class InvHdr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5385748129074158691L;

	private Integer invNumber;
	private Date createDate;
	private BigDecimal totalDiscoount;
	private BigDecimal totalAmount;
	private BigDecimal cashValue;
	private BigDecimal balance;
	private PayType payType;
	private Customer customer;
	private String createBy;
	private short state;
	private List<InvDet> invDets;

	public InvHdr() {
		super();
	}

	public InvHdr(Integer invNumber, Date createDate, BigDecimal totalDiscoount, BigDecimal totalAmount,
			BigDecimal cashValue, BigDecimal balance, PayType payType, Customer customer, String createBy,
			short state) {
		super();
		this.invNumber = invNumber;
		this.createDate = createDate;
		this.totalDiscoount = totalDiscoount;
		this.totalAmount = totalAmount;
		this.cashValue = cashValue;
		this.balance = balance;
		this.payType = payType;
		this.customer = customer;
		this.createBy = createBy;
		this.state = state;
	}

	public Integer getInvNumber() {
		return invNumber;
	}

	public void setInvNumber(Integer invNumber) {
		this.invNumber = invNumber;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getTotalDiscoount() {
		return totalDiscoount;
	}

	public void setTotalDiscoount(BigDecimal totalDiscoount) {
		this.totalDiscoount = totalDiscoount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getCashValue() {
		return cashValue;
	}

	public void setCashValue(BigDecimal cashValue) {
		this.cashValue = cashValue;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public List<InvDet> getInvDets() {
		return invDets;
	}

	public void setInvDets(List<InvDet> invDets) {
		this.invDets = invDets;
	}

}
