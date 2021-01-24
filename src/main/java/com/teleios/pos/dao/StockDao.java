package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.Stock;

public interface StockDao {
	int[] createNewStockItems(List<GrnDet> grnDets, Integer batchNumber)
			throws SocketTimeoutException, DataAccessException, Exception;

	int[] updateStockForNewInvoice(List<InvDet> invDets) throws SocketTimeoutException, DataAccessException, Exception;

	Stock getStockItemByNumber(Integer stockItem)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	List<Stock> getStockForPOS()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;
}
