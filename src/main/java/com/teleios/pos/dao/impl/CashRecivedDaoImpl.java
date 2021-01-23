package com.teleios.pos.dao.impl;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.CashRecivedDao;
import com.teleios.pos.model.CashRecived;

@Repository
public class CashRecivedDaoImpl implements Serializable, CashRecivedDao {
	private static final long serialVersionUID = -1480675869779024371L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CashRecivedDaoImpl.class);

	// Create Cash Recived SQL
	private static final String CREATE_NEW_CASH_REC_SQL = "INSERT INTO customer_schema.cash_rec (amount,cashier,change,create_date,fk_recv_id,create_by,cash_pay_state,fk_cus_no,fk_inv_no) "
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public CashRecivedDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int createNewCashRecived(CashRecived cashRecived, Integer invNumber)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<------ Execute Create New Cash Recived In Cash Recived Repository -------->");
		int createState = 0;
		Integer recivableNumber = null;
		if (cashRecived.getReceivable() != null)
			recivableNumber = cashRecived.getCashRecNumber();

		createState = this.jdbcTemplate.update(CREATE_NEW_CASH_REC_SQL, cashRecived.getAmount(),
				cashRecived.getCashier(), cashRecived.getChange(), cashRecived.getCreateDate(), recivableNumber,
				cashRecived.getCreateBy(), cashRecived.getCashRecState(), cashRecived.getCustomer().getCustomerId(),
				invNumber);
		LOGGER.info("<------ Successfuly Create New Cash Recived Amount Of :{} In Cash Recived Repository -------->",
				cashRecived.getAmount());
		return createState;
	}

}
