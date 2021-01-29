package com.teleios.pos.dao.impl;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.InvoiceDao;
import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.InvHdr;

@Repository
public class InvoiceDaoImpl implements Serializable, InvoiceDao {
	private static final long serialVersionUID = 5501147584546179067L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDaoImpl.class);

	// Define Create New Invoice SQL
	private static final String CREATE_NEW_INVHDR_SQL = "INSERT INTO inv_schema.inv_hdr (inv_hdr_num,create_date,discount,total,cash,balance,fk_pay_type,fk_cus_no,create_by,"
			+ "inv_state,lab_cost,trans_cost,payble_amount,chequ_amount,total_paid,barcode) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String CREATE_NEW_INVDETS_SQL = "INSERT INTO inv_schema.inv_det (qty,discount,purchace_price,selling_price,amount,fk_inv_hdr_no,fk_inven_no,batch_number,cus_advantage) "
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	// Define Invoice Fetsh SQL
	private static final String GET_NEXT_INVNUMBER_SQL = "SELECT MAX(inv_hdr_num) FROM inv_schema.inv_hdr";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public InvoiceDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Integer createNewInvoiceHeader(InvHdr invHdr) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<-------- Execute Create New Invoice Header In Invoice Repository -------->");

		Integer invoiceNumber = getNextInvoiceNumber();
		if (invoiceNumber == null)
			invoiceNumber = Integer.valueOf(1);
		else
			invoiceNumber = invoiceNumber + 1;
		invHdr.setBarcode(invoiceNumber + "12541");

		this.jdbcTemplate.update(CREATE_NEW_INVHDR_SQL, invoiceNumber, invHdr.getCreateDate(),
				invHdr.getTotalDiscoount(), invHdr.getTotalAmount(), invHdr.getCashValue(), invHdr.getBalance(),
				invHdr.getPayType().getPayTypeId(), invHdr.getCustomer().getCustomerId(), invHdr.getCreateBy(),
				invHdr.getState(), invHdr.getLabourCharge(), invHdr.getTransaCharge(), invHdr.getPayblAmount(),
				invHdr.getChequAmount(), invHdr.getTotalPaid(), invHdr.getBarcode());
		LOGGER.info("<-------- Successfuly Create New Invoice Header Repository -------->");
		return invoiceNumber;
	}

	@Override
	public int[] createNewInvoiceDetails(List<InvDet> invDets, Integer invNumber)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----------- Execute Create New Invoice Details In Invoice Repositiry --------->");
		int[] saveStates = null;
		saveStates = this.jdbcTemplate.batchUpdate(CREATE_NEW_INVDETS_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setBigDecimal(1, invDets.get(i).getQty());
				ps.setBigDecimal(2, invDets.get(i).getDiscount());
				ps.setBigDecimal(3, invDets.get(i).getPurchacePrice());
				ps.setBigDecimal(4, invDets.get(i).getSellingPrice());
				ps.setBigDecimal(5, invDets.get(i).getAmount());
				ps.setInt(6, invNumber);
				ps.setInt(7, invDets.get(i).getStockItem().getStockId());
				ps.setInt(8, invDets.get(i).getStockItem().getBatchNumber());
				ps.setBigDecimal(9, invDets.get(i).getCusAdva());

			}

			@Override
			public int getBatchSize() {
				return invDets.size();
			}
		});
		LOGGER.info("<----------- Successfuly Create New Number Of :{} invoice Items In Invoice Repositiry --------->",
				saveStates.length);
		return saveStates;
	}

	@Override
	public Integer getNextInvoiceNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Get Next Invoice Number In Invoice Repository ------->");
		return this.jdbcTemplate.queryForObject(GET_NEXT_INVNUMBER_SQL, Integer.class);
	}

}
