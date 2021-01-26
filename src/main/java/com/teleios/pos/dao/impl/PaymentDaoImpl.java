package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.PaymentDao;
import com.teleios.pos.model.CashPayment;
import com.teleios.pos.model.CheqDetails;
import com.teleios.pos.model.Payeble;

@Repository
public class PaymentDaoImpl implements PaymentDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDaoImpl.class);
	private JdbcTemplate jdbcTemplate;

	// Define Create SQL
	private static final String CREATE_NEW_CASH_PAY_SQL = "INSERT INTO supplier_schema.cash_payment (amount,cashier,change,create_date,fk_pay_id,create_by,cash_pay_state,grn_no,exp_lst_id) "
			+ "VALUES (?,?,?,?,?,?,?,?,?)";
	private static final String CREATE_NEW_CHQPAY_SQL = "INSERT INTO supplier_schema.chq_payment (chq_number,chq_amount,chq_date,bank_name,fk_pay_id,fk_pay_state,create_date,"
			+ "create_by,fk_chq_type,grn_id,exp_lst_id,branch_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String CREATE_NEW_PAYBLE_SQL = "INSERT INTO supplier_schema.payble (balance,fk_grn_id,fk_pay_state_id,pay_state,fk_supp_id,create_date,create_by) "
			+ "VALUES (?,?,?,?,?,?,?)";

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

		LOGGER.info("<--- Succssesfuly Create New Cash Payment--->");

		return createState;
	}

	@Override
	public int createNewCheqePaymment(CheqDetails cheqDetails)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<---- Execute Create New Chequ Payment In Payment Repository Chequ Value: {} Chequ Number: {}",
				cheqDetails.getAmount(), cheqDetails.getAmount());
		int createState = 0;
		Integer paybleNumber = null;
		Integer grnNumber = null;
		Integer expLstNumber = null;
		Date chequeDate = null;

		if (cheqDetails.getPayeble() != null)
			paybleNumber = cheqDetails.getPayeble().getPaybNumber();

		if (cheqDetails.getGrnHdr() != null)
			grnNumber = cheqDetails.getGrnHdr().getGrnNumber();

		if (cheqDetails.getExpenditureList() != null)
			expLstNumber = cheqDetails.getExpenditureList().getExpLstNumber();

		if (cheqDetails.getCheckDate() != null)
			chequeDate = cheqDetails.getCheckDate();

		createState = this.jdbcTemplate.update(CREATE_NEW_CHQPAY_SQL, cheqDetails.getCheckNumber(),
				cheqDetails.getAmount(), chequeDate, cheqDetails.getBankName(), paybleNumber,
				cheqDetails.getCheckState(), cheqDetails.getCreateDate(), cheqDetails.getCreateBy(),
				cheqDetails.getCheqType().getCheqTypeNumber(), grnNumber, expLstNumber, cheqDetails.getBrancName()

		);

		LOGGER.info("<---- Successfuly Create New Chequ Payment In Payment Repository ----->");
		return createState;
	}

	@Override
	public int createNewPayble(Payeble payeble) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Create New Payble Payble Name: {} in Payment Repository ------->",
				payeble.getSupplier().getSupplierName());
		int createState = 0;

		createState = this.jdbcTemplate.update(CREATE_NEW_PAYBLE_SQL, payeble.getBalance(),
				payeble.getGrnHdr().getGrnNumber(), payeble.getPaymentType().getPayTypeId(), payeble.getState(),
				payeble.getSupplier().getSupplierId(), payeble.getCreateDate(), payeble.getCreateBy());

		LOGGER.info("<----- Successfuly Create New Payble Payment Repository ------->");

		return createState;
	}

}
