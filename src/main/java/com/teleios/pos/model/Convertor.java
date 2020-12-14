package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

public class Convertor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6416214797476675806L;

	private int convId;
	private Uom baseUom;
	private Uom ratUom;
	private Double value;
	private String createby;
	private Date createDate;
	private short state;

	public Convertor() {
		super();
	}

	public Convertor(int convId, Uom baseUom, Uom ratUom, Double value, String createby, Date createDate, short state) {
		super();
		this.convId = convId;
		this.baseUom = baseUom;
		this.ratUom = ratUom;
		this.value = value;
		this.createby = createby;
		this.createDate = createDate;
		this.state = state;
	}

	public int getConvId() {
		return convId;
	}

	public void setConvId(int convId) {
		this.convId = convId;
	}

	public Uom getBaseUom() {
		return baseUom;
	}

	public void setBaseUom(Uom baseUom) {
		this.baseUom = baseUom;
	}

	public Uom getRatUom() {
		return ratUom;
	}

	public void setRatUom(Uom ratUom) {
		this.ratUom = ratUom;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

}
