/**
 * 
 */
package com.teleios.pos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class PayType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6124250710119155556L;
	
	private short payStateId;
	private String desc;
	private String createBy;
	private Date createDate;
	private short state;
	
	
	
	public PayType() {
		super();
		
	}
	public PayType(short payStateId, String desc, String createBy, Date createDate, short state) {
		super();
		this.payStateId = payStateId;
		this.desc = desc;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}
	public short getPayStateId() {
		return payStateId;
	}
	public void setPayStateId(short payStateId) {
		this.payStateId = payStateId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	
	@Override
	public String toString() {
		return "PayType [payStateId=" + payStateId + ", desc=" + desc + "]";
	}
	
	
}
