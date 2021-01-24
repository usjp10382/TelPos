package com.teleios.pos.dao.impl;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.CustomerDao;
import com.teleios.pos.dao.utill.CustomerResExecutor;
import com.teleios.pos.model.Customer;

@Repository
public class CustomerDaoImpl implements CustomerDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoImpl.class);

	// Define Create SQL Queries
	private static final String CREATE_CUSTOMER_SQL = "INSERT INTO customer_schema.customer(first_name,last_name,address,mobile_number,fw_balance,create_by,create_date,cus_state) "
			+ "VALUES(:first_name,:last_name,:address,:mobile_number,:fw_balance,:create_by,:create_date,:cus_state)";

	// Define Customer Update SQL
	private static final String UPD_CUS_BAL_SQL = "UPDATE customer_schema.customer SET fw_balance=fw_balance+? WHERE customer_id=?";

	// Define Customer rFetch SQL Queries
	private static final String SELECT_CUSTOMER_BY_NUMBER = "SELECT customer_id,first_name,last_name,address,mobile_number,fw_balance,create_by,create_date,cus_state FROM "
			+ "customer_schema.customer WHERE customer_id=?";
	private static final String SELECT_ALLACTIVE_CUSTOMERS = "SELECT customer_id,first_name,last_name,address,mobile_number,fw_balance,create_by,create_date,cus_state FROM "
			+ "customer_schema.customer WHERE cus_state=? ORDER BY customer_id ASC";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public CustomerDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public int createNewCustomer(Customer customer)
			throws SocketTimeoutException, DuplicateKeyException, DataAccessException, Exception {
		LOGGER.info("Execute Create New Customer Repositort Mobile Number: {}-----------> ",
				customer.getMobileNumber());
		int createstate = 0;

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("first_name", customer.getFirstName());
		paraMap.put("last_name", customer.getLastName());
		paraMap.put("address", customer.getAddress());
		paraMap.put("mobile_number", customer.getMobileNumber());
		paraMap.put("fw_balance", customer.getFwBalance());
		paraMap.put("create_by", customer.getCreateBy());
		paraMap.put("create_date", customer.getCreateDate());
		paraMap.put("cus_state", customer.getCustomerState());

		createstate = this.namedParameterJdbcTemplate.update(CREATE_CUSTOMER_SQL, paraMap);

		LOGGER.info("<---------- Successfuly Create New Cusstomer ------------>");

		return createstate;
	}

	@Override
	public int updateCustomerBalance(Customer customer, BigDecimal value)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Update Customer Balance Value Is:{} In Customer Repository", value);
		int updState = 0;

		updState = this.jdbcTemplate.update(UPD_CUS_BAL_SQL, value, customer.getCustomerId());

		LOGGER.info("Successfuly Update Customer Balance Value Is:{}", value);
		return updState;
	}

	@Override
	public Customer getCustomerByNumber(Customer customer)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> getAllActiveCustomers()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("Execut Get All Active Customers In Customer Repositiry -------->");
		return this.jdbcTemplate.query(SELECT_ALLACTIVE_CUSTOMERS, new CustomerResExecutor(), (short) 1);
	}

}
