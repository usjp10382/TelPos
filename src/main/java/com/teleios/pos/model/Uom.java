package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class Uom implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1852656541956636993L;

	private Integer uomId;
	private String uomName;
	private String uomChar;
	private String createBy;
	private Date createDate;
	private short state;

	public Uom() {
		super();

	}

	public Uom(Integer uomId, String uomName, String uomChar, String createBy, Date createDate, short state) {
		super();
		this.uomId = uomId;
		this.uomName = uomName;
		this.uomChar = uomChar;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomChar() {
		return uomChar;
	}

	public void setUomChar(String uomChar) {
		this.uomChar = uomChar;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
