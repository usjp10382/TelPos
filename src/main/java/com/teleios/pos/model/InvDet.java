/**
 * 
 */
package com.teleios.pos.model;

import java.math.BigDecimal;

/**
 * @author : Harith Ahangama
 * @Date :12/06/20
 * @version :1.0
 *
 */
public class InvDet {
	private Integer invDetNumber;
	private Double qty;
	private BigDecimal purchacePrice;
	private BigDecimal sellingPrice;
	private BigDecimal discount;
	private BigDecimal amount;
	private InvHdr invHdr;
	private Product product;
}
