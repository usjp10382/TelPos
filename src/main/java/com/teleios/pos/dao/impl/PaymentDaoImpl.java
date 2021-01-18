package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.PaymentDao;
import com.teleios.pos.model.CashPayment;

@Repository
public class PaymentDaoImpl implements PaymentDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	private static final String CREATE_NEW_CASH_PAY_SQL = "INSERT INTO supplier_schema.cash_payment (amount,cashier,change,create_date,fk_pay_id,create_by,cash_pay_state,grn_no,exp_lst_id) "
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	@Autowired
	public PaymentDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int createNewCashPaymment(CashPayment cashPayment)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Executing Create New Cash Payment... Value: {} ", cashPayment.getAmount());
		int createState = 0;
		Integer paybleNumber = null;
		Integer grnNumber = null;
		Integer expLstNumber = null;

		if (cashPayment.getPayeble() != null)
			paybleNumber = cashPayment.getPayeble().getPaybNumber();

		if (cashPayment.getGrnHdr() != null)
			grnNumber = cashPayment.getGrnHdr().getGrnNumber();

		if (cashPayment.getExpenditureList() != null)
			expLstNumber = cashPayment.getExpenditureList().getExpLstNumber();

		createState = this.jdbcTemplate.update(CREATE_NEW_CASH_PAY_SQL, cashPayment.getAmount(),
				cashPayment.getCashier(), cashPayment.getChange(), cashPayment.getCreateDate(), paybleNumber,
				cashPayment.getCreateBy(), cashPayment.getState(), grnNumber, expLstNumber);

		LOGGER.info("Succssesfuly Create New Cash Payment--->");

		return createState;
	}

}
