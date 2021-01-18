package com.teleios.pos.service;

import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.StockDaoImpl;
import com.teleios.pos.model.GrnDet;

@Service
public class StockService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

	private StockDaoImpl stockDaoImpl;

	@Autowired
	public StockService(StockDaoImpl stockDaoImpl) {
		this.stockDaoImpl = stockDaoImpl;
	}

	public int[] createNewStockItems(List<GrnDet> grnDets, final Integer batchNumber)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<------Execute Create New Stock Items In Service -------->");
		return this.stockDaoImpl.createNewStockItems(grnDets, batchNumber);
	}

}
