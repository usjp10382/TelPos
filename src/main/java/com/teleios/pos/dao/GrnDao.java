package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.PaymentType;

public interface GrnDao {
	List<PaymentType> getPaymentTypes(short maxRes)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	Integer getNextBatchNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	Integer getNextGrnNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;
}
