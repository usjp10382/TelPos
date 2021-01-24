package com.teleios.pos.dao.impl;

import java.io.Serializable;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.RecevableDao;
import com.teleios.pos.model.Receivable;

@Repository
public class RecevableDaoImpl implements Serializable, RecevableDao {
	private static final long serialVersionUID = -4956170398942586025L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RecevableDaoImpl.class);

	// Define Recevable Create SQL
	private static final String CREATE_NEW_RECVB_SQL = "INSERT INTO customer_schema.receivable (balance,fk_inv_id,fk_pay_state_id,recv_state,fk_cus_no,create_date,create_by) "
			+ "VALUES (?,?,?,?,?,?,?)";

	private JdbcTemplate JdbcTemplate;

	@Autowired
	public RecevableDaoImpl(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
		JdbcTemplate = jdbcTemplate;
	}

	@Override
	public int createNewRecevable(Receivable receivable) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<------- Execute Create New Recevable In Recevable Repository ----->");
		int createState = 0;

		createState = this.JdbcTemplate.update(CREATE_NEW_RECVB_SQL, receivable.getBalance(),
				receivable.getInvHdr().getInvNumber(), receivable.getPaymentType().getPayTypeId(),
				receivable.getRcvState(), receivable.getCustomer().getCustomerId(), receivable.getCreateDate(),
				receivable.getCreateBy());

		LOGGER.info("<------- Successfuly Create New Recevable In Recevable Repository ----->");
		return createState;
	}

}
