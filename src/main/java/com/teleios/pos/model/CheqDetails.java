package com.teleios.pos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class CheqDetails implements Serializable {
	private static final long serialVersionUID = -5646571216029966169L;

	private GrnHdr grnHdr;
	private CheqType cheqType;
	private String bankName;
	private String brancName;
	private String checkNumber;
	private String note;
	private BigDecimal amount;
	private Date checkDate;

	public CheqDetails() {
		super();
	}

	public CheqDetails(GrnHdr grnHdr, CheqType cheqType, String bankName, String brancName, String checkNumber,
			String note, BigDecimal amount, Date checkDate) {
		super();
		this.grnHdr = grnHdr;
		this.cheqType = cheqType;
		this.bankName = bankName;
		this.brancName = brancName;
		this.checkNumber = checkNumber;
		this.note = note;
		this.amount = amount;
		this.checkDate = checkDate;
	}

	public GrnHdr getGrnHdr() {
		return grnHdr;
	}

	public void setGrnHdr(GrnHdr grnHdr) {
		this.grnHdr = grnHdr;
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

}
