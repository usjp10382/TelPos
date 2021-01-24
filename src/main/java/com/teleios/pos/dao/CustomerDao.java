package com.teleios.pos.dao;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Customer;

public interface CustomerDao {
	int createNewCustomer(Customer customer)
			throws SocketTimeoutException, DuplicateKeyException, DataAccessException, Exception;

	int updateCustomerBalance(Customer customer, BigDecimal value)
			throws SocketTimeoutException, DataAccessException, Exception;

	Customer getCustomerByNumber(Customer customer)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	List<Customer> getAllActiveCustomers()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

}
