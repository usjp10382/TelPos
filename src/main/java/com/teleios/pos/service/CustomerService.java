package com.teleios.pos.service;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.CustomerDaoImpl;
import com.teleios.pos.model.Customer;

@Service
public class CustomerService implements Serializable {
	private static final long serialVersionUID = 5902932151649915265L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private CustomerDaoImpl customerDaoImpl;

	@Autowired
	public CustomerService(CustomerDaoImpl customerDaoImpl) {
		this.customerDaoImpl = customerDaoImpl;
	}

	public int createNewCustomer(Customer customer)
			throws SocketTimeoutException, DuplicateKeyException, DataAccessException, Exception {
		LOGGER.info("<--------- Execute Create New Customer In Customer Service ---------->");
		return this.customerDaoImpl.createNewCustomer(customer);
	}

	public Customer getCustomerByNumber(Customer customer)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Customer> getAllActiveCustomers()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--------- Execute Get All Active Customers In Customer Service ---------->");
		return this.customerDaoImpl.getAllActiveCustomers();
	}

}
