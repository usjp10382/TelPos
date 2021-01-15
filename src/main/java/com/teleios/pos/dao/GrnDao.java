package com.teleios.pos.dao;

import java.net.SocketTimeoutException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public interface GrnDao {
	Integer getNextBatchNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	Integer getNextGrnNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;
}
