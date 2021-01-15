package com.teleios.pos.service;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.GrnDaoImpl;

@Service
public class GrnService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrnService.class);

	private GrnDaoImpl grnDaoImpl;

	@Autowired
	public GrnService(GrnDaoImpl grnDaoImpl) {
		this.grnDaoImpl = grnDaoImpl;
	}

	public Integer getNextBatchNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Grn Number In Service ---->");
		Integer nextGrnNum = null;
		try {
			nextGrnNum = this.grnDaoImpl.getNextGrnNumber() + 1;
		} catch (NullPointerException npe) {
			nextGrnNum = Integer.valueOf(1);
		}
		return nextGrnNum;
	}

	public Integer getNextGrnNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Batch Number In Service ---->");
		Integer nextBatchNum = null;
		try {
			nextBatchNum = this.grnDaoImpl.getNextBatchNumber() + 1;
		} catch (NullPointerException npe) {
			nextBatchNum = Integer.valueOf(1);
		}
		return nextBatchNum;
	}

}
