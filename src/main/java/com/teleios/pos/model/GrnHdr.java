package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrnHdr implements Serializable {
	private static final long serialVersionUID = -1942218359864533665L;

	private Integer grnNumber;
	private String remark;
	private Integer batchNumber;
	private Supplier supplier;
	private Date grnDate;
	private CheqDetails cheqDetails;
	private PaymentType paymentType;
	private boolean isTotValDiscount;
	private BigDecimal grnValDiscount;
	private Float discPrecentage;
	private BigDecimal totalValue;
	private BigDecimal paybleAmount;
	private BigDecimal paidAmount;
	private BigDecimal balance;
	private Integer itemCount;
	private String createBy;
	private Date createDate;
	private short grnState;

	private List<GrnDet> grnDets = new ArrayList<GrnDet>();

	public GrnHdr() {
		super();
	}

	public GrnHdr(Integer grnNumber, String remark, Integer batchNumber, Supplier supplier, Date grnDate,
			CheqDetails cheqDetails, PaymentType paymentType, boolean isTotValDiscount, BigDecimal grnValDiscount,
			Float discPrecentage, BigDecimal totalValue, BigDecimal paybleAmount, BigDecimal paidAmount,
			BigDecimal balance, Integer itemCount, Date createDate, String createBy, short grnState) {
		super();
		this.grnNumber = grnNumber;
		this.remark = remark;
		this.batchNumber = batchNumber;
		this.supplier = supplier;
		this.grnDate = grnDate;
		this.cheqDetails = cheqDetails;
		this.paymentType = paymentType;
		this.isTotValDiscount = isTotValDiscount;
		this.grnValDiscount = grnValDiscount;
		this.discPrecentage = discPrecentage;
		this.totalValue = totalValue;
		this.paybleAmount = paybleAmount;
		this.paidAmount = paidAmount;
		this.balance = balance;
		this.itemCount = itemCount;
		this.createDate = createDate;
		this.createBy = createBy;
		this.grnState = grnState;
	}

	public Integer getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(Integer grnNumber) {
		this.grnNumber = grnNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public CheqDetails getCheqDetails() {
		return cheqDetails;
	}

	public void setCheqDetails(CheqDetails cheqDetails) {
		this.cheqDetails = cheqDetails;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public boolean isTotValDiscount() {
		return isTotValDiscount;
	}

	public void setTotValDiscount(boolean isTotValDiscount) {
		this.isTotValDiscount = isTotValDiscount;
	}

	public BigDecimal getGrnValDiscount() {
		return grnValDiscount;
	}

	public void setGrnValDiscount(BigDecimal grnValDiscount) {
		this.grnValDiscount = grnValDiscount;
	}

	public Float getDiscPrecentage() {
		return discPrecentage;
	}

	public void setDiscPrecentage(Float discPrecentage) {
		this.discPrecentage = discPrecentage;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getPaybleAmount() {
		return paybleAmount;
	}

	public void setPaybleAmount(BigDecimal paybleAmount) {
		this.paybleAmount = paybleAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
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

	public short getGrnState() {
		return grnState;
	}

	public void setGrnState(short grnState) {
		this.grnState = grnState;
	}

	public List<GrnDet> getGrnDets() {
		return grnDets;
	}

	public void setGrnDets(List<GrnDet> grnDets) {
		this.grnDets = grnDets;
	}

}
