package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CheqDetails implements Serializable {
	private static final long serialVersionUID = -5646571216029966169L;

	private Integer chequeId;
	private GrnHdr grnHdr;
	private Payeble payeble;
	private ExpenditureList expenditureList;
	private CheqType cheqType;
	private String bankName;
	private String brancName;
	private String checkNumber;
	private String note;
	private BigDecimal amount;
	private Date checkDate;
	private short checkState;
	private Date createDate;
	private String createBy;

	public CheqDetails() {
		super();
	}

	public CheqDetails(Integer chequeId, GrnHdr grnHdr, Payeble payeble, ExpenditureList expenditureList,
			CheqType cheqType, String bankName, String brancName, String checkNumber, String note, BigDecimal amount,
			Date checkDate, short checkState, Date createDate, String createBy) {
		super();
		this.chequeId = chequeId;
		this.grnHdr = grnHdr;
		this.payeble = payeble;
		this.expenditureList = expenditureList;
		this.cheqType = cheqType;
		this.bankName = bankName;
		this.brancName = brancName;
		this.checkNumber = checkNumber;
		this.note = note;
		this.amount = amount;
		this.checkDate = checkDate;
		this.checkState = checkState;
		this.createDate = createDate;
		this.createBy = createBy;
	}

	public Integer getChequeId() {
		return chequeId;
	}

	public void setChequeId(Integer chequeId) {
		this.chequeId = chequeId;
	}

	public GrnHdr getGrnHdr() {
		return grnHdr;
	}

	public void setGrnHdr(GrnHdr grnHdr) {
		this.grnHdr = grnHdr;
	}

	public Payeble getPayeble() {
		return payeble;
	}

	public void setPayeble(Payeble payeble) {
		this.payeble = payeble;
	}

	public ExpenditureList getExpenditureList() {
		return expenditureList;
	}

	public void setExpenditureList(ExpenditureList expenditureList) {
		this.expenditureList = expenditureList;
	}

	public CheqType getCheqType() {
		return cheqType;
	}

	public void setCheqType(CheqType cheqType) {
		this.cheqType = cheqType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBrancName() {
		return brancName;
	}

	public void setBrancName(String brancName) {
		this.brancName = brancName;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public short getCheckState() {
		return checkState;
	}

	public void setCheckState(short checkState) {
		this.checkState = checkState;
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
