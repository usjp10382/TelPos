package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.teleios.pos.model.GrnDet;

public interface StockDao {
	int[] createNewStockItems(List<GrnDet> grnDets,Integer batchNumber) throws SocketTimeoutException, DataAccessException, Exception;
}
