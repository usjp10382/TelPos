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
	private static final long serialVersionUID = 5385748129074158691L;

	private Integer invNumber;
	private Date createDate;
	private BigDecimal totalAmount;
	private BigDecimal labourCharge;
	private BigDecimal transaCharge;
	private BigDecimal totalDiscoount;
	private BigDecimal payblAmount;
	private BigDecimal cashValue;
	private BigDecimal chequAmount;
	private BigDecimal totalPaid;
	private BigDecimal balance;
	private PaymentType payType;
	private Customer customer;
	private String createBy;
	private String barcode;
	private CheqDetails cheqDetails;
	private short state;
	private List<InvDet> invDets;

	public InvHdr() {
		super();
	}

	public InvHdr(Integer invNumber, Date createDate, BigDecimal totalAmount, BigDecimal labourCharge,
			BigDecimal transaCharge, BigDecimal totalDiscoount, BigDecimal payblAmount, BigDecimal cashValue,
			BigDecimal chequAmount, BigDecimal totalPaid, BigDecimal balance, PaymentType payType, Customer customer,
			String createBy, String barcode, short state, List<InvDet> invDets) {
		super();
		this.invNumber = invNumber;
		this.createDate = createDate;
		this.totalAmount = totalAmount;
		this.labourCharge = labourCharge;
		this.transaCharge = transaCharge;
		this.totalDiscoount = totalDiscoount;
		this.payblAmount = payblAmount;
		this.cashValue = cashValue;
		this.chequAmount = chequAmount;
		this.totalPaid = totalPaid;
		this.balance = balance;
		this.payType = payType;
		this.customer = customer;
		this.createBy = createBy;
		this.barcode = barcode;
		this.state = state;
		this.invDets = invDets;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getLabourCharge() {
		return labourCharge;
	}

	public void setLabourCharge(BigDecimal labourCharge) {
		this.labourCharge = labourCharge;
	}

	public BigDecimal getTransaCharge() {
		return transaCharge;
	}

	public void setTransaCharge(BigDecimal transaCharge) {
		this.transaCharge = transaCharge;
	}

	public BigDecimal getTotalDiscoount() {
		return totalDiscoount;
	}

	public void setTotalDiscoount(BigDecimal totalDiscoount) {
		this.totalDiscoount = totalDiscoount;
	}

	public BigDecimal getPayblAmount() {
		return payblAmount;
	}

	public void setPayblAmount(BigDecimal payblAmount) {
		this.payblAmount = payblAmount;
	}

	public BigDecimal getCashValue() {
		return cashValue;
	}

	public void setCashValue(BigDecimal cashValue) {
		this.cashValue = cashValue;
	}

	public BigDecimal getChequAmount() {
		return chequAmount;
	}

	public void setChequAmount(BigDecimal chequAmount) {
		this.chequAmount = chequAmount;
	}

	public BigDecimal getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(BigDecimal totalPaid) {
		this.totalPaid = totalPaid;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public PaymentType getPayType() {
		return payType;
	}

	public void setPayType(PaymentType payType) {
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public CheqDetails getCheqDetails() {
		return cheqDetails;
	}

	public void setCheqDetails(CheqDetails cheqDetails) {
		this.cheqDetails = cheqDetails;
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
