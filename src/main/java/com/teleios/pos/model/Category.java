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
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6810382920666062799L;

	private Integer categoryId;
	private String categoryName;
	private String createBy;
	private Date createDate;
	private short state;

	public Category() {
		super();
	}

	public Category(Integer categoryId, String categoryName, String createBy, Date createDate, short state) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.createBy = createBy;
		this.createDate = createDate;
		this.state = state;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + "]";
	}

}
